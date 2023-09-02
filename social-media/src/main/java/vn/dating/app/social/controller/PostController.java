package vn.dating.app.social.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.bouncycastle.util.encoders.UTF8;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.dating.app.social.dto.auth.AuthLoginDto;
import vn.dating.app.social.dto.post.PostViewDto;
import vn.dating.app.social.mapper.PostMapper;
import vn.dating.app.social.models.Notify;
import vn.dating.app.social.models.NotifyEntity;
import vn.dating.app.social.models.Post;
import vn.dating.app.social.models.User;
import vn.dating.app.social.models.eenum.NotificationType;
import vn.dating.app.social.models.eenum.PostStatus;
import vn.dating.app.social.models.eenum.PostType;
import vn.dating.app.social.services.*;
import vn.dating.app.social.utils.PagedResponse;
import vn.dating.app.social.utils.TimeHelper;


import java.time.Instant;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/social/posts")
public class PostController {

    @Value("${upload.api-get-image}")
    private  String API_GET_IMAGE;



    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadFile(@RequestPart("files") List<MultipartFile> files, @RequestPart("content") String content,
                                   @RequestPart("title") String title) {

       List<String> listMedia =  mediaService.onlySaveFile(files);


        return Mono.just("Received content: " + content + ", title: " + title +" List "+listMedia.toString());

        // Handle the file upload here
    }






    @PostMapping( value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<PostViewDto> createPost(
                                        @RequestPart("title") String title,
                                        @RequestPart("content") String content,
                                        @RequestPart("files") Flux<FilePart> filePartFlux) {

        log.info("abc");
        Post newPost = new Post();

        User getUser = userService.findById("abc");

        if (getUser == null) {
            return Mono.error(new RuntimeException("User not found"));
        }


        newPost.setAuthor(getUser);
        newPost.setDelete(false);
        Instant jpTime = TimeHelper.getCurrentInstantSystemDefault();
        newPost.setCreatedAt(jpTime);
        newPost.setUpdatedAt(jpTime);
        newPost.setContent(content);
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

