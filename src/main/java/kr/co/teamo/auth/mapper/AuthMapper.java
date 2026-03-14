package kr.co.teamo.auth.mapper;

import kr.co.teamo.auth.dto.LoginDto;
import kr.co.teamo.auth.dto.TechStackResponse;
import kr.co.teamo.auth.dto.User;
import kr.co.teamo.auth.dto.UserInsertDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AuthMapper {
    int countByEmail(String email);
    void insertUser(UserInsertDto dto);
    void insertUserLanguage(@Param("userId") Long userId,
                            @Param("dtlCdId") String dtlCdId);
    LoginDto findByEmail(String email);
    void withdrawUser(Long userId);
    int existsEmail(String Email);
    User findById(Long userId);
    List<TechStackResponse> findAll();
    void updateUser(@Param("userId") Long userId, @Param("name") String name, @Param("passwordHash") String passwordHash);
    void deleteUserLanguage(Long userId);
}
