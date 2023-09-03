package vn.dating.app.social.dto.comment;

import lombok.*;
import org.springframework.data.domain.Page;
import vn.dating.app.social.mapper.CommentMapper;
import vn.dating.app.social.models.Comment;

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
public class PageCommentDetailDto implements Serializable {
    private int page;
    private int size;
    private int pages;
    private boolean last;
    private List<CommentSuccDto> data = new ArrayList<>();

    public PageCommentDetailDto(Page<Comment> commentPage){

        this.page = commentPage.getNumber();
        this.pages=commentPage.getTotalPages();
        this.size=commentPage.getSize();
        this.last=commentPage.isLast();

        List<CommentSuccDto> commentSuccDtoList =
                CommentMapper.toGetCommentDtos(commentPage.stream().collect(Collectors.toList()));

        this.data = commentSuccDtoList;

    }
}
