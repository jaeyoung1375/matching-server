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
    int countByEmail(@Param("email") String email);
    void insertUser(UserInsertDto dto);
    void insertUserLanguage(@Param("userId") Long userId,
                            @Param("languageId") Long languageId);
    LoginDto findByEmail(@Param("email") String email);
    void withdrawUser(@Param("userId") Long userId);
    int existsEmail(@Param("email") String Email);
    User findById(Long userId);
    List<TechStackResponse> findAll();
}
