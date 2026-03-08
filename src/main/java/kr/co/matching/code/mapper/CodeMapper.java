package kr.co.matching.code.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.matching.code.dto.CodeRequestDto;
import kr.co.matching.code.dto.CodeResponseDto;

@Mapper
public interface CodeMapper {

	List<CodeResponseDto> getCodeList(CodeRequestDto requestDto);

}
