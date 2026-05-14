package kr.co.teamo.comment.mapper;

import kr.co.teamo.comment.dto.CommentInsertDto;
import kr.co.teamo.comment.dto.CommentResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<CommentResponseDto> selectCommentsByPostId(
            @Param("postId") Long postId,
            @Param("userId") Long userId
    );

    void insertComment(CommentInsertDto dto);

    int softDeleteComment(
            @Param("commentId") Long commentId,
            @Param("userId") Long userId
    );
}
