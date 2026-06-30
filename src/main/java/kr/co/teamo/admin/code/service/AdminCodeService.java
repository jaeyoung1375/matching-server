package kr.co.teamo.admin.code.service;

import kr.co.teamo.admin.code.dto.AdminCodeDtlDto;
import kr.co.teamo.admin.code.dto.AdminCodeDtlRequest;
import kr.co.teamo.admin.code.dto.AdminCodeDtlUpdateRequest;
import kr.co.teamo.admin.code.dto.AdminCodeGroupDto;
import kr.co.teamo.admin.code.dto.AdminCodeGroupRequest;
import kr.co.teamo.admin.code.dto.AdminCodeGroupUpdateRequest;
import kr.co.teamo.admin.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCodeService {

    private final AdminMapper adminMapper;

    public List<AdminCodeGroupDto> getCodeGroups(AdminCodeGroupRequest req) {
        return adminMapper.selectCodeGroups(req);
    }

    public void createCodeGroup(AdminCodeGroupRequest req, String adminId) {
        req.setRegId(adminId);
        adminMapper.insertCodeGroup(req);
    }

    public void modifyCodeGroup(String comCdId, AdminCodeGroupUpdateRequest req, String adminId) {
        req.setComCdId(comCdId);
        req.setModId(adminId);
        adminMapper.updateCodeGroup(req);
    }

    public void removeCodeGroup(String comCdId, String adminId) {
        adminMapper.deleteCodeGroup(comCdId, adminId);
    }

    public List<AdminCodeDtlDto> getCodeDetails(String comCdId) {
        return adminMapper.selectCodeDetails(comCdId);
    }

    public void createCodeDetail(String comCdId, AdminCodeDtlRequest req, String adminId) {
        req.setComCdId(comCdId);
        req.setRegId(adminId);
        adminMapper.insertCodeDetail(req);
    }

    public void modifyCodeDetail(String comCdId, String dtlCdId, AdminCodeDtlUpdateRequest req, String adminId) {
        req.setComCdId(comCdId);
        req.setDtlCdId(dtlCdId);
        req.setModId(adminId);
        adminMapper.updateCodeDetail(req);
    }

    public void removeCodeDetail(String comCdId, String dtlCdId, String adminId) {
        adminMapper.deleteCodeDetail(comCdId, dtlCdId, adminId);
    }

    public void modifyAllCodeDetailsUseYn(String comCdId, String useYn, String adminId) {
        adminMapper.updateAllCodeDetailsUseYn(comCdId, useYn, adminId);
    }
}
