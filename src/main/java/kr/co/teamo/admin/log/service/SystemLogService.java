package kr.co.teamo.admin.log.service;

import kr.co.teamo.admin.log.dto.AdminLogDto;
import kr.co.teamo.admin.log.dto.AdminLogSearchRequest;
import kr.co.teamo.admin.log.dto.SystemLogCreateDto;
import kr.co.teamo.admin.log.mapper.SystemLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SystemLogService {
    private final SystemLogMapper systemLogMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(SystemLogCreateDto systemLogCreateDto) {
        try {
            systemLogMapper.insertSystemLog(systemLogCreateDto);
        } catch (Exception e) {
            log.warn("System log save failed. logTypeCd={}, actionCd={}, userId={}, reason={}",
                    systemLogCreateDto.getLogTypeCd(),
                    systemLogCreateDto.getActionCd(),
                    systemLogCreateDto.getUserId(),
                    e.getMessage(),
                    e);
        }
    }

    public List<AdminLogDto> searchLogs(AdminLogSearchRequest adminLogSearchRequest) {
        return systemLogMapper.selectSystemLogs(adminLogSearchRequest);
    }
}
