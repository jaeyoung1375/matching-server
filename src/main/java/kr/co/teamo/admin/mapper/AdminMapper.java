package kr.co.teamo.admin.mapper;

import kr.co.teamo.admin.code.dto.AdminCodeDtlDto;
import kr.co.teamo.admin.code.dto.AdminCodeDtlRequest;
import kr.co.teamo.admin.code.dto.AdminCodeDtlUpdateRequest;
import kr.co.teamo.admin.code.dto.AdminCodeGroupDto;
import kr.co.teamo.admin.code.dto.AdminCodeGroupRequest;
import kr.co.teamo.admin.code.dto.AdminCodeGroupUpdateRequest;
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

    // 공통코드 그룹
    List<AdminCodeGroupDto> selectCodeGroups(AdminCodeGroupRequest req);
    void insertCodeGroup(AdminCodeGroupRequest req);
    void updateCodeGroup(AdminCodeGroupUpdateRequest req);
    void deleteCodeGroup(@Param("comCdId") String comCdId, @Param("modId") String modId);

    // 상세코드
    List<AdminCodeDtlDto> selectCodeDetails(@Param("comCdId") String comCdId);
    void insertCodeDetail(AdminCodeDtlRequest req);
    void updateCodeDetail(AdminCodeDtlUpdateRequest req);
    void deleteCodeDetail(@Param("comCdId") String comCdId, @Param("dtlCdId") String dtlCdId, @Param("modId") String modId);
    void updateAllCodeDetailsUseYn(@Param("comCdId") String comCdId, @Param("useYn") String useYn, @Param("modId") String modId);
}
