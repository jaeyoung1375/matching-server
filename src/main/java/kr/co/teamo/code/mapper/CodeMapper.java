package kr.co.teamo.code.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.teamo.code.dto.CodeRequestDto;
import kr.co.teamo.code.dto.CodeResponseDto;

@Mapper
public interface CodeMapper {

	List<CodeResponseDto> getCodeList(@Param("comCdIds") List<String> comCdIds);

}
