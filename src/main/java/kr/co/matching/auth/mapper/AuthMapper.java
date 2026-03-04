package kr.co.matching.auth.mapper;

import kr.co.matching.auth.dto.UserInsertDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface AuthMapper {
    int countByEmail(@Param("email") String email);
    void insertUser(UserInsertDto dto);
    Map<String,Object> findByEmail(@Param("email") String email);
    void upsertRefreshToken(@Param("userId") long userId, @Param("refreshToken") String refreshToken);
    String findRefreshToken(@Param("userId") Long userId);
    void withdrawUser(@Param("userId") Long userId);
    void deleteRefreshToken(@Param("userId") Long userId);
}
