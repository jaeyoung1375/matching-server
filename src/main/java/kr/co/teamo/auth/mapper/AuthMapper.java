package kr.co.teamo.auth.mapper;

import kr.co.teamo.auth.dto.UserInsertDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface AuthMapper {
    int countByEmail(@Param("email") String email);
    void insertUser(UserInsertDto dto);
    Map<String,Object> findByEmail(@Param("email") String email);
    void withdrawUser(@Param("userId") Long userId);
}
