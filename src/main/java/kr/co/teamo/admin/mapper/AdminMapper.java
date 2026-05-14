package kr.co.teamo.admin.mapper;

import kr.co.teamo.admin.dto.UserCountDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    UserCountDto selectUserCount();
}
