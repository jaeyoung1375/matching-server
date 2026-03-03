package kr.co.matching.auth.mapper;

import kr.co.matching.auth.dto.UserInsertDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthMapper {
    int countByEmail(@Param("email") String email);
    void insertUser(UserInsertDto dto);
}
