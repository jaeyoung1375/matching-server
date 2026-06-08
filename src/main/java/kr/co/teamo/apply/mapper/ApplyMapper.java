package kr.co.teamo.apply.mapper;

import kr.co.teamo.apply.dto.ApplyRequestDto;
import kr.co.teamo.apply.dto.ApplyResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApplyMapper {

    /**
     * 지원 등록
     */
    void insertApply(ApplyRequestDto req);

    /**
     * 게시글별 지원 목록 조회
     */
    List<ApplyResponseDto> selectApplyListByPostId(@Param("postId") Long postId);

    /**
     * 중복 지원 여부 확인
     */
    int countApply(@Param("postId") Long postId, @Param("userId") Long userId);
}
