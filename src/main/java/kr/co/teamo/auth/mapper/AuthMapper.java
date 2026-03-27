package kr.co.teamo.auth.mapper;

import kr.co.teamo.auth.dto.*;
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
    SocialAccount findSocialAccount(
            @Param("provider") String provider,
            @Param("providerUserId") String providerUserId
    );
    void insertSocialAccount(
            @Param("userId") Long userId,
            @Param("provider") String provider,
            @Param("providerUserId") String providerUserId
    );
    boolean existsByUserId(Long userId);
}
