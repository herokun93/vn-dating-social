package vn.dating.app.social.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.dating.app.social.dto.comment.CommentPageDetails;
import vn.dating.app.social.dto.community.CommunityPageDto;
import vn.dating.app.social.dto.post.PostDetailDto;
import vn.dating.app.social.mapper.CommentMapper;
import vn.dating.app.social.mapper.PostMapper;
import vn.dating.app.social.models.Comment;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.Post;
import vn.dating.app.social.models.User;
import vn.dating.app.social.repositories.CommentRepository;
import vn.dating.app.social.utils.PagedResponse;
import vn.dating.app.social.utils.TimeHelper;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

//    @Autowired
//    private PostService postService;


    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment saveComment(User user, Post post, String commentContent) {
        Comment newComment = new Comment();
        newComment.setContent(commentContent);
        newComment.setPost(post);
        newComment.setUser(user);
        Instant jpTime = TimeHelper.getCurrentInstantSystemDefault();
        newComment.setCreatedAt(jpTime);
        newComment.setUpdatedAt(jpTime);

        newComment = commentRepository.save(newComment);

        if(newComment==null) return null;
//        postService.postUpdateTime(post);

        return newComment;
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    public Comment findCommentById(long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }
    public Page<Comment> findByPostId(Long id, Pageable pageable){return  commentRepository.findByPostId(id,pageable);}


    public PagedResponse findCommentsByPostId(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findByPostId(id,pageable);

        if(comments.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(), comments.getNumber(), comments.getSize(),
                    comments.getTotalElements(), comments.getTotalPages(), comments.isLast());
        }

        return new PagedResponse<>(CommentMapper.toGetComments(comments.stream().toList()), comments.getNumber(), comments.getSize(), comments.getTotalElements(),
                comments.getTotalPages(), comments.isLast());
    }

    public PagedResponse findCommentsByPostUrl(String postUrl, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findByPost_Url(postUrl,pageable);

        if(comments.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(), comments.getNumber(), comments.getSize(),
                    comments.getTotalElements(), comments.getTotalPages(), comments.isLast());
        }

        return new PagedResponse<>(CommentMapper.toGetComments(comments.stream().toList()), comments.getNumber(), comments.getSize(), comments.getTotalElements(),
                comments.getTotalPages(), comments.isLast());
    }

//    public CommunityPageDto getCommunitiesByCreatorId(String creatorId, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        // Implement the logic to fetch communities created by the user based on creatorId
//        Page<Community> communityPage = communityRepository.findCommunitiesByCreator_Id( creatorId,  pageable);
//        CommunityPageDto communityPageDto = new CommunityPageDto(communityPage);
//        return communityPageDto;
//    }
//    public CommentPageDetails getCommentsByPostUrl(String postUrl, int page, int size){
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Comment> pageComments = commentRepository.findByPost_Url(postUrl,pageable);
//
//        CommentPageDetails commentPageDetails = new CommentPageDetails(pageComments);
//        return commentPageDetails;
//    }

    public PagedResponse getDetailsNews(String postUrl, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findByPost_Url(postUrl,pageable);

        if(comments.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(), comments.getNumber(), comments.getSize(),
                    comments.getTotalElements(), comments.getTotalPages(), comments.isLast());
        }

        return new PagedResponse<>(CommentMapper.toGetComments(comments.stream().toList()), comments.getNumber(), comments.getSize(), comments.getTotalElements(),
                comments.getTotalPages(), comments.isLast());
    }

    public PostDetailDto getDetailsPost(Post post, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findByPost_Url(post.getUrl(), pageable);
        return PostMapper.toPostDetail(post,comments);
    }

    public Page<Comment> getPageCommentByPostUrl(String postUrl,int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findByPost_Url(postUrl, pageable);
    }

    public PagedResponse findCommentsByUserId(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findByUserId(id,pageable);

        if(comments.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(), comments.getNumber(), comments.getSize(),
                    comments.getTotalElements(), comments.getTotalPages(), comments.isLast());
        }

        return new PagedResponse<>(CommentMapper.toGetComments(comments.stream().toList()), comments.getNumber(), comments.getSize(), comments.getTotalElements(),
                comments.getTotalPages(), comments.isLast());
    }

    public Page<Comment> getPageDesCommentsOfPost(Long postId, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> commentPage = commentRepository.findByPostId(postId,pageable);
        return commentPage;
    }

    public Page<Comment> getPageAscCommentsOfPost(Long postId, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Comment> commentPage = commentRepository.findByPostId(postId,pageable);
        return commentPage;
    }

    public long countCommentsByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }
}

