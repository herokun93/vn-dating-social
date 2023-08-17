package vn.dating.app.social.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.dating.app.gateway.utils.UserCustom;
import vn.dating.app.social.dto.post.PostDetailDto;
import vn.dating.app.social.dto.post.PostViewDto;
import vn.dating.app.social.mapper.PostMapper;
import vn.dating.app.social.models.*;
import vn.dating.app.social.models.eenum.NotificationType;
import vn.dating.app.social.models.eenum.PostStatus;
import vn.dating.app.social.models.eenum.PostType;
import vn.dating.app.social.services.*;
import vn.dating.app.social.utils.PagedResponse;
import vn.dating.app.social.utils.TimeHelper;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/v1/social/posts")
public class PostController {

    @Value("${upload.api-get-image}")
    private  String API_GET_IMAGE;



    @Autowired
    private  PostService postService;
    @Autowired
    private  UserService userService;
    @Autowired
    private  CommentService commentService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuthService authService;

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

    @GetMapping("/url/{url}")
    public ResponseEntity getPost(@PathVariable("url") String url,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {

        Post post = postService.findByUrl(url);
        if(post==null) return new ResponseEntity<>("Post not exist",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(commentService.getDetailsPost(post,page,size), HttpStatus.OK);
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

    @GetMapping("/check")
    public ResponseEntity check(JwtAuthenticationToken authenticationToken) {
        UserCustom userCustom = authService.getUserCustom(authenticationToken);
        return new ResponseEntity<>(userCustom, HttpStatus.OK);
    }

    @GetMapping("/public")
    public ResponseEntity testPublic() {

        return new ResponseEntity<>("Public", HttpStatus.OK);
    }


    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<PostViewDto> createPost(JwtAuthenticationToken authenticationToken,
                                              @RequestPart("files") Flux<FilePart> filePartFlux,
                                              @RequestPart("title") String title,
                                              @RequestPart("content") String content) {

        UserCustom userCustom = authService.getUserCustom(authenticationToken);

        if (!userCustom.isUser()) {
            return Mono.error(new RuntimeException("User not found. Need to login first."));
        }

        Post newPost = new Post();

        User getUser = userService.findById(userCustom.getId());

        if (getUser == null) {
            return Mono.error(new RuntimeException("User not found"));
        }

        newPost.setAuthor(getUser);
        newPost.setDelete(false);
        Instant jpTime = TimeHelper.getCurrentInstantSystemDefault();
        newPost.setCreatedAt(jpTime);
        newPost.setUpdatedAt(jpTime);
        newPost.setContent(content);
        newPost.setType(PostType.NEWS);
        newPost.setState(PostStatus.PENDING);
        newPost.setTitle(title);

        newPost = postService.save(newPost);


        PostViewDto postViewDto = PostMapper.toPostCreateView(newPost);


        filePartFlux.count().flatMap(count -> {
            if (count == 0) {
                return Mono.just(postViewDto);
            } else {

                Mono<List<String>> listMono = filePartFlux
                        .flatMap(filePart -> mediaService.onlySaveFile(filePart))
                        .collectList();

                return listMono.flatMap(filePaths -> {
                    postViewDto.setMedia(filePaths);
                    mediaService.saveMediaPost(postViewDto.getId(), filePaths);
                    return Mono.just(postViewDto);
                });

            }
        });

        return Mono.just(postViewDto);
    }


    private void saveFilePathsToDatabase(List<String> filePaths) {

    }

//    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
//    public Mono<List<String>> createPost(JwtAuthenticationToken authenticationToken,
//                                         @RequestPart("files") Flux<FilePart> filePartMono,
//                                         @RequestPart("title") String title,
//                                         @RequestPart("content") String content) {
//
//        UserCustom userCustom = authService.getUserCustom(authenticationToken);
////        if(!userCustom.isUser()) return  new ResponseEntity<>("Need login",HttpStatus.FORBIDDEN);
//
//
//        User getUser = userService.findById(userCustom.getId());
//
//        if(getUser==null){
////            return new ResponseEntity<>("User not exist",HttpStatus.BAD_REQUEST);
//        }
//
//
//
////        Post newPost = new Post();
////        newPost.setAuthor(getUser);
////        newPost.setDelete(false);
////        Instant jpTime = TimeHelper.getCurrentInstantSystemDefault();
////        newPost.setCreatedAt(jpTime);
////        newPost.setUpdatedAt(jpTime);
////        newPost.setContent(content);
////        newPost.setType(PostType.NEWS);
////        newPost.setState(PostStatus.PENDING);
////        newPost.setTitle(title);
////
////
////        newPost = postService.save(newPost);
//
////        List<String> medialList = new ArrayList<>();
////
////        files.flatMap(filePart -> {
////            String originalFilename = filePart.filename();
////            if(!checkAcceptMedial(originalFilename)){
////                return Mono.empty();
////            }
////
////            String extension = FilenameUtils.getExtension(originalFilename).toLowerCase();
////            String newFileName = generateRandomStringByTime() + "." + extension;
////
////            createUploadsDirectory(UPLOAD_DIR);
////
////            Path destinationPath = Paths.get(UPLOAD_DIR+ newFileName);
////
////            return filePart.transferTo(destinationPath)
////                    .doOnSuccessOrError((aVoid, throwable) -> {
////                        if (throwable != null) {
////                            throwable.printStackTrace();
////                        } else {
////                            medialList.add(newFileName);
////                        }
////                    })
////                    .then();
////        }).collectList().subscribe(
////                fileList -> {
////                    // All files have been processed, and the results are available in the fileList
////                    System.out.println("Image size: " + fileList.size());
////
////                },
////                throwable -> throwable.printStackTrace(),
////                () -> System.out.println("All files processed successfully.")
////        );
////        System.out.println("Image size "+medialList.size());
//
//        //mediaService.saveImages(files).collectList().subscribe();
//
////        PostViewDto postViewDto = PostMapper.toPostCreateView(newPost);
//
//
////        Mono<List<String>> mediaList = mediaService.getLines(filePartMono).collectList();
//
//
//
////        return new ResponseEntity<>(mediaService.getLines(filePartMono).collectList(), HttpStatus.OK);
//
////        return new ResponseEntity<>(postViewDto, HttpStatus.OK);
//        return mediaService.getLines(filePartMono).collectList();
//
//    }

//    @PostMapping("/upload")
//    public Mono<PostViewDto> uploadFiles(
//            @RequestPart("content") String content,
//            @RequestPart("files") Flux<FilePart> filePartFlux) {
//        List<Mono<String>> savedFileMonos = new ArrayList<>();
//
//        return filePartFlux.flatMap(filePart -> {
//                    // Process each uploaded file concurrently
//                    String originalFilename = filePart.filename();
//                    long size = filePart.headers().getContentLength();
//                    System.out.println("Uploaded file: " + originalFilename + " (Size: " + size + " bytes)");
//
//                    // Save the file to a specific location asynchronously using MediaService
//                    // For example:
//                    Path destinationPath = Paths.get("YOUR_DESTINATION_PATH" + originalFilename);
//                    Mono<String> savedFileMono = mediaService.saveFile(filePart,destinationPath);
//                    savedFileMonos.add(savedFileMono);
//
//                    return savedFileMono;
//                })
//                .collectList()
//                .map(savedFileNames -> {
//                    // Create the PostViewDto and set the list of saved file paths in it
//                    PostViewDto postViewDto = new PostViewDto();
//                    postViewDto.setMedia(savedFileNames);
//
//                    // Add other necessary fields to the PostViewDto
//                    // For example:
//                    postViewDto.setTitle("Your title");
//                    postViewDto.setContent("Your content");
//
//                    return postViewDto;
//                });
//
//    }







    public static String generateRandomStringByTime() {
        long currentTimeMillis = System.currentTimeMillis();
        String randomString = UUID.randomUUID().toString().replace("-", "");
        String result = currentTimeMillis + "-" + randomString;
        return result;
    }




//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity createNewPost(
//                                        @RequestPart("files") MultipartFile[] files,
//                                        @RequestPart("title") String title,
//                                        @RequestPart("content")  String content) {
//
//        log.info("Media test");
//        log.info("Files size: " + files.toString());

//        UserCustom userCustom = authService.getUserCustom(authenticationToken);
//        if(!userCustom.isUser()) return  new ResponseEntity<>("Need login",HttpStatus.FORBIDDEN);
//
//
//        Post newPost = new Post();
//
//        User getUser = userService.findById(userCustom.getId());
//
//        if(getUser==null){
//            return new ResponseEntity<>("User not exist",HttpStatus.BAD_REQUEST);
//        }
//

//
//        newPost.setAuthor(getUser);
//        newPost.setDelete(false);
//        Instant jpTime = TimeHelper.getCurrentInstantSystemDefault();
//        newPost.setCreatedAt(jpTime);
//        newPost.setUpdatedAt(jpTime);
//        newPost.setContent(content);
//        newPost.setType(PostType.NEWS);
//        newPost.setState(PostStatus.PENDING);
//        newPost.setTitle(title);
//
//        newPost = postService.save(newPost);
//
//        sendPostNotifications(newPost);
//
//        PostViewDto postViewDto = PostMapper.toPostCreateView(newPost);
//
//        return new ResponseEntity<>(postViewDto, HttpStatus.OK);
//        return new ResponseEntity<>(null, HttpStatus.OK);
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

