package kr.co.teamo.apply.service;

import kr.co.teamo.apply.dto.ApplyRequestDto;
import kr.co.teamo.apply.dto.ApplyResponseDto;
import kr.co.teamo.apply.enums.ApplyStatus;
import kr.co.teamo.apply.mapper.ApplyMapper;
import kr.co.teamo.common.code.CommonErrorCode;
import kr.co.teamo.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final ApplyMapper applyMapper;

    /**
     * 지원 등록
     */
    @Transactional
    public void createApply(ApplyRequestDto req) {

        // 중복 지원 체크
        int count = applyMapper.countApply(req.getPostId(), req.getUserId());
        if (count > 0) {
            throw new CustomException(CommonErrorCode.DUPLICATE_APPLY);
        }

        // 기술스택 List → 콤마 구분 문자열 변환
        if (!ObjectUtils.isEmpty(req.getTechStackCd())) {
            req.setTechStackCdStr(String.join(",", req.getTechStackCd()));
        }

        // 지원 등록
        applyMapper.insertApply(req, ApplyStatus.WAIT.getCode());
    }

    /**
     * 게시글별 지원 목록 조회
     */
    public List<ApplyResponseDto> getApplyList(Long postId) {
        return applyMapper.selectApplyListByPostId(postId);
    }

    /**
     * 지원 상태 변경(수락/거절)
     * @param req
     */
    public void updateApply(Long applyId, ApplyStatus statusCd) {
    	applyMapper.updateApplyStatus(applyId, statusCd.getCode());

    }

    public ApplyResponseDto findByApplyId(Long applyId) {
    	return applyMapper.findByApplyId(applyId);
    }
}
