package kr.co.teamo.admin.mapper;

import kr.co.teamo.admin.dashboard.dto.NewUserCountDto;
import kr.co.teamo.admin.dashboard.dto.PostCountDto;
import kr.co.teamo.admin.dashboard.dto.UserCountDto;
import kr.co.teamo.admin.user.dto.AdminUserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {
    UserCountDto selectUserCount();
    NewUserCountDto selectNewUserCount();
    PostCountDto selectPostCount();
    List<AdminUserDto> searchUsers(@Param("name") String name, @Param("email") String email);
    String selectUserStatus(@Param("userId") Long userId);
    void updateUserRole(@Param("userId") Long userId, @Param("role") String role);
}
