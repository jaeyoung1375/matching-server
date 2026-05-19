package kr.co.teamo.admin.mapper;

import kr.co.teamo.admin.dashboard.dto.NewUserCountDto;
import kr.co.teamo.admin.dashboard.dto.PostCountDto;
import kr.co.teamo.admin.dashboard.dto.UserCountDto;
import kr.co.teamo.admin.user.dto.AdminUserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface AdminMapper {
    UserCountDto selectUserCount();
    NewUserCountDto selectNewUserCount();
    PostCountDto selectPostCount();
    List<AdminUserDto> searchUsers(@RequestParam String name, @RequestParam String email);
}
