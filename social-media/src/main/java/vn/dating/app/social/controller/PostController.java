package vn.dating.app.social.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.dating.app.social.dto.ResponseMessage;
import vn.dating.app.social.dto.ResponseObject;
import vn.dating.app.social.dto.community.CommunityResultDto;
import vn.dating.app.social.dto.post.PostCreateSuccDto;
import vn.dating.app.social.dto.post.PostDetailsDto;
import vn.dating.app.social.models.*;
import vn.dating.app.social.models.eenum.NotificationType;
import vn.dating.app.social.models.eenum.PostStatus;
import vn.dating.app.social.services.*;
import vn.dating.app.social.utils.PagedResponse;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/api/social/posts")
public class PostController {

    @Value("${upload.api-get-image}")
    private  String API_GET_IMAGE;




    @Autowired
    private AuthService authService;



    @Autowired
    private UserCommunityService userCommunityService;
    @Autowired
    private CommunityService communityService;



    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private NotifyActorService notifyActorService;

    @Autowired
    private NotifyEntityService notifyEntityService;

    @Autowired
    private NotifyService notifyService;

    private final ResourceLoader resourceLoader;

    public PostController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }





    @GetMapping()
    public ResponseEntity getPagePosts(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        PagedResponse pagedResponse = postService.getPagesTopPostAndComment(page,size);
        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
    }

//    @GetMapping("/url/{url}")
//    public ResponseEntity getPost(@PathVariable("url") String url,
//                                  @RequestParam(defaultValue = "0") int page,
//                                  @RequestParam(defaultValue = "10") int size) {
//
//        Post post = postService.findByUrl(url);
//        if(post==null) return new ResponseEntity<>("Post not exist",HttpStatus.BAD_REQUEST);
//        return new ResponseEntity<>(commentService.getDetailsPost(post,page,size), HttpStatus.OK);
//    }

    @GetMapping("/url/{url}")
    public ResponseEntity<ResponseObject> getPost(@PathVariable("url") String url,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", ResponseMessage.SUCCESSFUL,
                        postService.getPostDetailsByUrl(url,page,size))

        );
    }
    @GetMapping("/{url}/comment")
    public ResponseEntity getCommentOfPost(@PathVariable("url") String url,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Post post = postService.findByUrl(url);
        if(post==null) return new ResponseEntity<>("Post not exist",HttpStatus.BAD_REQUEST);

        PagedResponse pagedResponse = commentService.findCommentsByPostUrl(url,page,size);

        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
    }



    @GetMapping(value = "/public")
    public ResponseEntity testPublic() {

        return new ResponseEntity<>("Public", HttpStatus.OK);
    }

//    @PostMapping(value = "/upload")
//    public ResponseEntity upload(@RequestPart("files") Flux<FilePart> files,
//                                 @RequestPart("name") String name){
//
//        System.out.println("upload file");
//        System.out.println(name);
//        files.subscribe(filePart -> {
//            String originalFilename = filePart.filename();
//            long size = filePart.headers().getContentLength();
//
//
//            System.out.println("Uploaded file: " + originalFilename + " (Size: " + size + " bytes)");
//        });
//
//        return new ResponseEntity<>("Uploaded", HttpStatus.OK);
//    }

//    @PostMapping(value = "/upload", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public Mono<String> uploadJson(@RequestBody AuthLoginDto authLoginDto) {
//        // Handle the JSON data here
//        return Mono.just("upload");
//    }

//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ResponseObject> uploadFile(Principal principal,
//                                                     @RequestPart("files") List<MultipartFile> files,
//                                                     @RequestPart("content") @NotBlank(message = "Title must not be blank")
//                                                         @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters") String content,
//                                                     @RequestPart("anonymous") @Pattern(regexp = "^(true|false)$",
//                                                             message = "Anonymous must be 'true' or 'false'") String anonymous,
//                                                     @RequestPart("type") @Pattern(regexp = "^(PRIVATE|PUBLIC)$",
//                                                             message = "Type must be 'PRIVATE' or 'PUBLIC'") String type,
//                                                     @RequestPart("community") @Pattern(regexp = "^[a-z0-9]+$",
//                                                             message = "Name must consist of lowercase letters (a-z) and digits (0-9) only, with no spaces") String community,
//                                                     @RequestPart("title") @NotBlank(message = "Title must not be blank")
//                                                         @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters") String title) {
//
//
//
//        boolean isAnonymous = Boolean.parseBoolean(anonymous);
//        PostType postType = PostType.valueOf(type);
//
//
//
//        User user = authService.getCurrentUserById(principal);
//
//
//        if(community.compareTo("000")==0){
//
//        }else{
//            boolean check = userCommunityService.doesUserCommunityExistByUserIdAndCommunityName(user.getId(), community);
//            if(!check){
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                        new ResponseObject("NOTOK", ResponseMessage.NOT_A_MEMBER, "")
//
//                );
//            }else{
//                Optional<Community> communityOptional = communityService.getCommunityByName(community);
//                boolean approval = communityOptional.get().isApproval();
//
//                Post newPost = new Post();
//                newPost.setAuthor(user);
//                newPost.setTitle(title);
//                newPost.setContent(content);
//                newPost.setAnonymous(isAnonymous);
//
//                if(approval){
//
//                }
//
//                newPost.setState(PostStatus.PENDING);
//                newPost.setType(postType);
//
//                newPost = postService.save(newPost);
//
//                List<String> listMedia =  mediaService.onlySaveFileToLocal(files,false);
//                mediaService.saveImagesToPost(newPost,listMedia);
//
//
//            }
//        }
//
//
//
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                new ResponseObject("NOTOK", ResponseMessage.NOT_FOUND, "")
//        );
//
//
//        // Handle the file upload here
//    }

    @PostMapping(value = "/create" , consumes = "multipart/form-data")
    public ResponseEntity<ResponseObject> createPostToCommunity(Principal principal,
                                                     @RequestPart(value ="files") List<MultipartFile> files,
                                                     @RequestPart("content") @NotBlank(message = "Title must not be blank")
                                                     @Size(min = 3, max = 3000, message = "Title must be between 3 and 3000 characters") String content,
                                                     @RequestPart("anonymous") @Pattern(regexp = "^(true|false)$",
                                                             message = "Anonymous must be 'true' or 'false'") String anonymous,
                                                     @RequestPart("community") @Pattern(regexp = "^[a-z0-9]+$",
                                                             message = "Name must consist of lowercase letters (a-z) and digits (0-9) only, with no spaces") String community,
                                                     @RequestPart("title") @NotBlank(message = "Title must not be blank")
                                                     @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters") String title) {

        boolean isAnonymous = Boolean.parseBoolean(anonymous);
        User user = authService.getCurrentUserById(principal);


        boolean check = userCommunityService.doesUserCommunityExistByUserIdAndCommunityName(user.getId(), community);
        if(!check){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("NOTOK", ResponseMessage.NOT_A_MEMBER, "")

            );
        }else{
            Optional<Community> communityOptional = communityService.getCommunityByName(community);
            boolean approval = communityOptional.get().isApproval();

            Post newPost = new Post();
            newPost.setAuthor(user);
            newPost.setTitle(title);
            newPost.setContent(content);
            newPost.setAnonymous(isAnonymous);
            newPost.setCommunity(communityOptional.get());
            newPost.setAuth(user.getId());

            if(approval){
                newPost.setState(PostStatus.ACCEPTED);
            }else{
                newPost.setState(PostStatus.PENDING);
            }


            if (files == null || files.isEmpty()) {
            }else{
                List<String> listMedia =  mediaService.onlySaveListFileToLocal(files,false);
                Set<Media> mediaItems = new HashSet<>();
                for (String mediaPath : listMedia) {
                    Media media = new Media();
                    media.setPath(mediaPath);
                    media.setPost(newPost); // Associate media with the post
                    mediaItems.add(media);
                }
                if(mediaItems.size()>0){
                    newPost.setMedia(mediaItems);
                }
            }




            newPost = postService.save(newPost);


            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", ResponseMessage.CREATED, PostCreateSuccDto.fromEntity(newPost))
            );

        }
    }

    @PostMapping(value = "/createWithoutFile" , consumes = "multipart/form-data")
    public ResponseEntity<ResponseObject> createPostToCommunityWithoutFile(Principal principal,
                                                                @RequestPart("content") @NotBlank(message = "Title must not be blank")
                                                                @Size(min = 3, max = 3000, message = "Title must be between 3 and 3000 characters") String content,
                                                                @RequestPart("anonymous") @Pattern(regexp = "^(true|false)$",
                                                                        message = "Anonymous must be 'true' or 'false'") String anonymous,
                                                                @RequestPart("community") @Pattern(regexp = "^[a-z0-9]+$",
                                                                        message = "Name must consist of lowercase letters (a-z) and digits (0-9) only, with no spaces") String community,
                                                                @RequestPart("title") @NotBlank(message = "Title must not be blank")
                                                                @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters") String title) {

        boolean isAnonymous = Boolean.parseBoolean(anonymous);
        User user = authService.getCurrentUserById(principal);


        boolean check = userCommunityService.doesUserCommunityExistByUserIdAndCommunityName(user.getId(), community);
        if(!check){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("NOTOK", ResponseMessage.NOT_A_MEMBER, "")

            );
        }else{
            Optional<Community> communityOptional = communityService.getCommunityByName(community);
            boolean approval = communityOptional.get().isApproval();

            Post newPost = new Post();
            newPost.setAuthor(user);
            newPost.setTitle(title);
            newPost.setContent(content);
            newPost.setAnonymous(isAnonymous);
            newPost.setCommunity(communityOptional.get());
            newPost.setAuth(user.getId());

            if(approval){
                newPost.setState(PostStatus.ACCEPTED);
            }else{
                newPost.setState(PostStatus.PENDING);
            }

            newPost = postService.save(newPost);


            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", ResponseMessage.CREATED, PostCreateSuccDto.fromEntity(newPost))
            );

        }
    }








//    @PostMapping( value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public Mono<PostViewDto> createPost(
//                                        @RequestPart("title") String title,
//                                        @RequestPart("content") String content,
//                                        @RequestPart("files") Flux<FilePart> filePartFlux) {
//
//        Post newPost = new Post();
//
//        User getUser = userService.findById("abc");
//
//        if (getUser == null) {
//            return Mono.error(new RuntimeException("User not found"));
//        }
//
//
//        newPost.setAuthor(getUser);
//        newPost.setDelete(false);
//        Instant jpTime = TimeHelper.getCurrentInstantSystemDefault();
//        newPost.setCreatedAt(jpTime);
//        newPost.setUpdatedAt(jpTime);
//        newPost.setContent(content);
//        newPost.setState(PostStatus.PENDING);
//        newPost.setTitle(title);
//
//        newPost = postService.save(newPost);
//
//
//        PostViewDto postViewDto = PostMapper.toPostCreateView(newPost);
//
//
//        filePartFlux.count().flatMap(count -> {
//            if (count == 0) {
//                return Mono.just(postViewDto);
//            } else {
//
//                Mono<List<String>> listMono = filePartFlux
//                        .flatMap(filePart -> mediaService.onlySaveFileToLocal(filePart))
//                        .collectList();
//
//                return listMono.flatMap(filePaths -> {
//                    postViewDto.setMedia(filePaths);
//                    mediaService.saveMediaPost(postViewDto.getId(), filePaths);
//                    return Mono.just(postViewDto);
//                });
//
//            }
//        });
//
//        return Mono.just(postViewDto);
//    }






    private boolean checkAcceptMedial(String filename){
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
       if(extension.equals("png"))return true;
       else if (extension.equals("jpg"))  return true;
       else if (extension.equals("gif"))  return true;
       else if (extension.equals("mp4"))  return true;
       else if (extension.equals("avi"))  return true;
       else if (extension.equals("mov"))  return true;
       else if (extension.equals("mkv"))  return true;
       else return false;
    }

    private void sendPostNotifications(Post post) {
        // Retrieve the post author
        User author = post.getAuthor();

        // Create a new notification entity for post creation
        NotifyEntity notifyEntity = new NotifyEntity();
        notifyEntity.setType(NotificationType.POST_CREATE);
        notifyEntity.setPost(post);

        // Save the notification entity
        NotifyEntity savedNotifyEntity = notifyEntityService.saveCreatePostNotifyEntity(post);

        // Create notifications for the post author
        Notify notify = new Notify();
        notify.setUser(author);
        notify.setNotifyEntity(savedNotifyEntity);

        // Save the notification
        notifyService.saveNotify(notify);
    }







//    @GetMapping("/{id}")
//    public ResponseEntity getCommentsByPostId(@PathVariable("id") long postId,
//                                                             @RequestParam(defaultValue = "0") int page,
//                                                             @RequestParam(defaultValue = "10") int size) {
//
//        Post post = postService.findById(postId);
//        if(post==null) return new ResponseEntity<>("Post not exist",HttpStatus.BAD_REQUEST);
//
//        PostDetailDto postDetailDto = postService.getDetailAndAscCommentsPost(post, page, size);
//        return new ResponseEntity<>(postDetailDto, HttpStatus.OK);
//    }
}

