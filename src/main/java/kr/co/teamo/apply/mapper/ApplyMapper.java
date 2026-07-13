package kr.co.teamo.apply.mapper;

import kr.co.teamo.apply.dto.ApplyRequestDto;
import kr.co.teamo.apply.dto.ApplyResponseDto;
import kr.co.teamo.apply.enums.ApplyStatus;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApplyMapper {

    /**
     * 지원 등록
     */
    void insertApply(@Param("req") ApplyRequestDto req, @Param("statusCd") String statusCd);

    /**
     * 게시글별 지원 목록 조회
     */
    List<ApplyResponseDto> selectApplyListByPostId(@Param("postId") Long postId);

    /**
     * 중복 지원 여부 확인
     */
    int countApply(@Param("postId") Long postId, @Param("userId") Long userId);

    /**
     * 지원 상태 변경(수락/거절)
     * @param applyId 지원 아이디
     * @param statusCd 상태코드
     */
    void updateApplyStatus(@Param("applyId") Long applyId, @Param("statusCd") String statusCd);

    /**
     * 지원 상세 조회
     * @param applyId
     * @return
     */
    ApplyResponseDto findByApplyId(Long applyId);
}
