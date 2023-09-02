package vn.dating.app.social.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.dating.app.social.dto.reply.PageReplyDto;
import vn.dating.app.social.models.Comment;
import vn.dating.app.social.models.Reply;
import vn.dating.app.social.models.User;
import vn.dating.app.social.repositories.ReplyRepository;

import java.time.Instant;

@Service
@Slf4j
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private PostService postService;

    public Reply saveReply(User createBy, Comment comment, String replyContent){
        Reply reply = new Reply();
        reply.setCreatedAt(Instant.now());
        reply.setUpdatedAt(Instant.now());
        reply.setComment(comment);
        reply.setUser(createBy);
        reply.setContent(replyContent);

//        postService.postUpdateTime(comment.getPost());

        return replyRepository.save(reply);
    }

    public Reply findReplyById(Long replyId){
        return replyRepository.findById(replyId).orElse(null);
    }
    public PageReplyDto getPageReplyByCommentId(long commentId,int page, int size){
         Pageable pageable = PageRequest.of(page, size);
        Page<Reply> replyPage =replyRepository.findByCommentId(commentId,pageable);
        PageReplyDto pageReplyDto = new PageReplyDto(replyPage);
        return pageReplyDto;
    }
}
