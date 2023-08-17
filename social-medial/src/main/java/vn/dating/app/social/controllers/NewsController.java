package vn.dating.app.social.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dating.app.social.models.Post;
import vn.dating.app.social.services.CommentService;
import vn.dating.app.social.services.PostService;
import vn.dating.app.social.services.UserService;
import vn.dating.app.social.utils.PagedResponse;

@RestController
@RequestMapping("/api/v1/social/news")
@Slf4j
public class NewsController {

    @Autowired
    private  PostService postService;
    @Autowired
    private  UserService userService;
    @Autowired
    private  CommentService commentService;




    @GetMapping()
    public ResponseEntity getPageNews(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        PagedResponse pagedResponse = postService.getPagesDetailsAndFist10Comments(page,size);
        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
    }

//    @GetMapping("/get")
//    public ResponseEntity getViewPageNews() {
//        log.info("get api");
//        int page = 0;
//        int size =10;
//        PagedResponse pagedResponse = postService.getPagesNews(page,size);
//        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
//    }

//    @GetMapping()
//    public ResponseEntity getViewPageNews(@RequestParam(defaultValue = "0") int page,
//                                          @RequestParam(defaultValue = "10") int size) {
//        log.info("get api");
//        PagedResponse pagedResponse = postService.getPagesNews(page,size);
//        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
//    }

    @GetMapping("/{url}")
    public ResponseEntity getPost(@PathVariable("url") String url,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info(url);
        Post post = postService.findByUrl(url);
        if(post==null) return new ResponseEntity<>("Post not exist",HttpStatus.BAD_REQUEST);
//        PagedResponse pagedResponse = commentService.getDetailsNews(url, page, size);
        return new ResponseEntity<>(commentService.getDetailsPost(post,page,size), HttpStatus.OK);
    }


}
