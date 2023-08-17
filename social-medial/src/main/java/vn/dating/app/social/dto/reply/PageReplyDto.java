package vn.dating.app.social.dto.reply;

import lombok.*;
import org.springframework.data.domain.Page;
import vn.dating.app.social.dto.comment.CommentDto;
import vn.dating.app.social.mapper.CommentMapper;
import vn.dating.app.social.mapper.ReplyMapper;
import vn.dating.app.social.models.Comment;
import vn.dating.app.social.models.Reply;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageReplyDto implements Serializable {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private List<ReplyDto> data = new ArrayList<>();

    public PageReplyDto(Page<Reply> replyPage){

        this.page = replyPage.getNumber();
        this.totalPages=replyPage.getTotalPages();
        this.size=replyPage.getSize();
        this.last=replyPage.isLast();
        this.totalElements = replyPage.getTotalElements();

        List<ReplyDto> replyDtos =
                ReplyMapper.toListReplies(replyPage.stream().collect(Collectors.toList()));

        this.data = replyDtos;

    }
}
