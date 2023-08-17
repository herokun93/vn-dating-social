package vn.dating.app.social.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import vn.dating.app.social.dto.MediaDetailsDto;
import vn.dating.app.social.dto.post.PostDetailDto;
import vn.dating.app.social.dto.post.PostViewDto;
import vn.dating.app.social.mapper.MediaMapper;
import vn.dating.app.social.mapper.PostMapper;
import vn.dating.app.social.models.Comment;
import vn.dating.app.social.models.Post;
import vn.dating.app.social.models.User;
import vn.dating.app.social.repositories.PostRepository;
import vn.dating.app.social.utils.PagedResponse;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class PostService {


    @Autowired
    private  PostRepository postRepository;

    @Autowired
    private  CommentService commentService;
    @Autowired
    private  LikeService likeService;

    @Autowired
    private MediaService mediaService;


    @Autowired
    private EntityManager entityManager;



    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public Post findById(long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post findByUrl(String url){
        return  postRepository.findByUrl(url).orElse(null);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public boolean existsPostById(Long id){
        List post = postRepository.findPostById(id);
        if(post.size()==1) return true;
        else  return  false;
    }

    public void postUpdateTime(Post post){
        post.setUpdatedAt(Instant.now());
        postRepository.save(post);
    }

    public PagedResponse findByAuthorId(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByAuthorId(id,pageable);

        if(posts.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(), posts.getNumber(), posts.getSize(),
                    posts.getTotalElements(), posts.getTotalPages(), posts.isLast());
        }

        return new PagedResponse<>(PostMapper.toGetPosts(posts.stream().toList()), posts.getNumber(), posts.getSize(), posts.getTotalElements(),
                posts.getTotalPages(), posts.isLast());
    }

    public Page<Post> findPagedPosts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        return postRepository.findAll(pageable);
    }

    public PostViewDto getPostView(long postId){
        long commentCount = commentService.countCommentsByPostId(postId);
        long likeCount = likeService.countLikesByPostId(postId);
        Post post = postRepository.findById(postId).orElse(null);
        if(post ==null) return null;
        return PostMapper.toPostView(post,commentCount,likeCount);
    }

    public PagedResponse getPagesPostViewAndFist10Comments(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<Post> postPage = postRepository.findAll(pageable);
        List<PostViewDto> postViewDtos = new ArrayList<>();
        postPage.forEach(post -> {
            postViewDtos.add(getPostView(post.getId()));
        });

        if(postPage.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(), postPage.getNumber(), postPage.getSize(),
                    postPage.getTotalElements(), postPage.getTotalPages(), postPage.isLast());
        }
        return new PagedResponse<>(postViewDtos, postPage.getNumber(), postPage.getSize(), postPage.getTotalElements(),
                postPage.getTotalPages(), postPage.isLast());

    }

    public PostDetailDto getDetailAndAsc10CommentsPost(long postId){
        Post post = postRepository.findById(postId).orElse(null);
        if(post==null) return null;
        Page<Comment> commentPage = commentService.getPageAscCommentsOfPost(postId,0,10);
        long countComments = commentPage.getTotalElements();
        long countLikes = likeService.countLikesByPostId(postId);

        List<MediaDetailsDto> mediaDetailsDtos = MediaMapper.toMediaOfPost(post);



        return PostMapper.toPostDetail(post,commentPage,countComments,countLikes,mediaDetailsDtos);
    }

    public PostDetailDto getDetailAndAscCommentsPost(Post post, int page, int size){
        if(post==null) return null;
        Long postId = post.getId();
        Page<Comment> commentPage = commentService.getPageAscCommentsOfPost(postId,page,size);
        long countComments = commentService.countCommentsByPostId(postId);
        long countLikes = likeService.countLikesByPostId(postId);
        List<MediaDetailsDto> mediaDetailsDtos = MediaMapper.toMediaOfPost(post);

        return PostMapper.toPostDetail(post,commentPage,countComments,countLikes,mediaDetailsDtos);
    }

//    public PostDetailDto getDetailByLastCommentTime(Post post){
//        if(post==null) return null;
//        Page<Comment> commentPage = commentService.getPageDesCommentsOfPost(post.getId());
//        long countComments = commentPage.getTotalElements();
//
//        return PostMapper.toPostDetail(post,commentPage.stream().toList(),countComments,10);
//    }

    public PagedResponse getPagesDetailsAndFist10Comments(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
//        Page<Post> postPage = postRepository.findAll(pageable);
        Page<Post> postPage = findPostsByLatestCommentTime(pageable);

        List<PostDetailDto> postDetailDtos = new ArrayList<>();

        postPage.forEach(post -> {
           postDetailDtos.add(getDetailAndAsc10CommentsPost(post.getId()));
        });
        if(postPage.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(), postPage.getNumber(), postPage.getSize(),
                    postPage.getTotalElements(), postPage.getTotalPages(), postPage.isLast());
        }
        return new PagedResponse<>(postDetailDtos, postPage.getNumber(), postPage.getSize(), postPage.getTotalElements(),
                postPage.getTotalPages(), postPage.isLast());
    }

    public PagedResponse getPagesTopPostAndComment(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Post> postPage = postRepository.findAllByOrderByUpdatedAtDesc(pageable);

        List<PostDetailDto> postDetailDtos = new ArrayList<>();

        List<Post> postList = postPage.getContent();
        int postListSize = postList.size();

        for (int index =0;index<postListSize;index++){
            Post post = postList.get(index);
            postDetailDtos.add(getDetailAndAsc10CommentsPost(post));
        }


        if(postPage.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(), postPage.getNumber(), postPage.getSize(),
                    postPage.getTotalElements(), postPage.getTotalPages(), postPage.isLast());
        }
        return new PagedResponse<>(postDetailDtos, postPage.getNumber(), postPage.getSize(), postPage.getTotalElements(),
                postPage.getTotalPages(), postPage.isLast());
    }

    public PostDetailDto getDetailAndAsc10CommentsPost(Post post){
       long postId = post.getId();
        Page<Comment> commentPage = commentService.getPageAscCommentsOfPost(postId,0,10);
        long countComments = commentPage.getTotalElements();
        long countLikes = likeService.countLikesByPostId(postId);

        List<MediaDetailsDto> mediaDetailsDtos = MediaMapper.toMediaOfPost(post);



        return PostMapper.toPostDetail(post,commentPage,countComments,countLikes,mediaDetailsDtos);
    }

    public PagedResponse getPagesNews(int pageNumber, int pageSize){

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<Post> postPage = findPostsByLatestCommentTime(pageable);



        List<PostViewDto> postViewDtos = PostMapper.toListPostDtos(postPage.stream().toList());

        if(postPage.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(), postPage.getNumber(), postPage.getSize(),
                    postPage.getTotalElements(), postPage.getTotalPages(), postPage.isLast());
        }
        return new PagedResponse<>(postViewDtos, postPage.getNumber(), postPage.getSize(), postPage.getTotalElements(),
                postPage.getTotalPages(), postPage.isLast());
    }

    public PagedResponse getDetailsNews(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<Post> postPage = findPostsByLatestCommentTime(pageable);

        List<PostViewDto> postViewDtos = PostMapper.toListPostDtos(postPage.stream().toList());

        if(postPage.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(), postPage.getNumber(), postPage.getSize(),
                    postPage.getTotalElements(), postPage.getTotalPages(), postPage.isLast());
        }
        return new PagedResponse<>(postViewDtos, postPage.getNumber(), postPage.getSize(), postPage.getTotalElements(),
                postPage.getTotalPages(), postPage.isLast());
    }

    public Page<Post> findPostsByLatestCommentTime(Pageable pageable) {
        String sql = "SELECT p.*, COALESCE(MAX(c.created_at), p.created_at) as latest_comment_time " +
                "FROM post p " +
                "LEFT JOIN comment c ON p.id = c.post_id " +
                "GROUP BY p.id " +
                "ORDER BY latest_comment_time DESC";
        Query query = entityManager.createNativeQuery(sql, Post.class);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        List<Post> posts = query.getResultList();
        long total = countPostsByLatestCommentTime();

        return new PageImpl<>(posts, pageable, total);
    }

    private long countPostsByLatestCommentTime() {
        String sql = "SELECT COUNT(*) FROM (" +
                "SELECT p.*, COALESCE(MAX(c.created_at), p.created_at) as latest_comment_time " +
                "FROM post p " +
                "LEFT JOIN comment c ON p.id = c.post_id " +
                "GROUP BY p.id" +
                ") as subquery";
        Query query = entityManager.createNativeQuery(sql);

        return ((Number) query.getSingleResult()).longValue();
    }

//    public PagedResponse getNotifyCommentOfPost(Long postId,int pageNumber, int pageSize){
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
//
//        Page<User> userPage = getUsersByPostId(postId,pageable);
//
//
//        if(userPage.getNumberOfElements()==0){
//            return new PagedResponse<>(Collections.emptyList(), userPage.getNumber(), userPage.getSize(),
//                    userPage.getTotalElements(), userPage.getTotalPages(), userPage.isLast());
//        }
//        return new PagedResponse<>(postDetailDtos, userPage.getNumber(), userPage.getSize(), userPage.getTotalElements(),
//                userPage.getTotalPages(), userPage.isLast());
//
//    }

    public Page<User> getListUserCmtOfFollowPost(Long postId,int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        return  getUsersByPostId(postId,pageNumber,pageSize);
    }

    public Page<User> getUsersByPostId(Long postId, int pageNumber, int pageSize) {
        TypedQuery<Long> countQuery = entityManager.createQuery(
                "SELECT COUNT(DISTINCT n.user.id) " +
                        "FROM Notification n " +
                        "WHERE n.post.id = :postId OR (n.type = 'POST_FOLLOW' AND n.post.id = :postId)", Long.class);
        countQuery.setParameter("postId", postId);
        Long totalResults = countQuery.getSingleResult();

        TypedQuery<String> idQuery = entityManager.createQuery(
                "SELECT DISTINCT n.user.id " +
                        "FROM Notification n " +
                        "WHERE n.post.id = :postId OR (n.type = 'POST_FOLLOW' AND n.post.id = :postId) " +
                        "ORDER BY n.user.id ASC", String.class);
        idQuery.setParameter("postId", postId);
        idQuery.setFirstResult((pageNumber - 1) * pageSize);
        idQuery.setMaxResults(pageSize);
        List<String> userIdList = idQuery.getResultList();

        List<User> userList = entityManager.createQuery(
                        "SELECT u " +
                                "FROM User u " +
                                "WHERE u.id IN :userIdList", User.class)
                .setParameter("userIdList", userIdList)
                .getResultList();

        return new PageImpl<>(userList, PageRequest.of(pageNumber - 1, pageSize), totalResults);
    }







}
