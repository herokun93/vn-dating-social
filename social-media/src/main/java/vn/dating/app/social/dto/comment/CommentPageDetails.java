package vn.dating.app.social.dto.comment;

import lombok.*;
import org.springframework.data.domain.Page;
import vn.dating.app.social.models.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentPageDetails {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private List<CommentDetails> data = new ArrayList<>();

    public CommentPageDetails(Page<Comment> commentPage) {
        this.page = commentPage.getNumber();
        this.totalPages=commentPage.getTotalPages();
        this.size=commentPage.getSize();
        this.last=commentPage.isLast();
        this.totalElements = commentPage.getTotalElements();
        this.data  = CommentDetails.fromEntities(commentPage.stream().toList());
    }
}
