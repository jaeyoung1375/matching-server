package kr.co.teamo.admin.log.mapper;

import kr.co.teamo.admin.log.dto.AdminLogDto;
import kr.co.teamo.admin.log.dto.AdminLogSearchRequest;
import kr.co.teamo.admin.log.dto.SystemLogCreateDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemLogMapper {
    List<AdminLogDto> selectSystemLogs(AdminLogSearchRequest request);
    void insertSystemLog(SystemLogCreateDto systemLogCreateDto);
}
