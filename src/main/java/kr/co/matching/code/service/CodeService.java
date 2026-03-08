package kr.co.matching.code.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.matching.code.dto.CodeRequestDto;
import kr.co.matching.code.dto.CodeResponseDto;
import kr.co.matching.code.mapper.CodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeService {

	private final CodeMapper codeMapper;

	public List<CodeResponseDto> getCodeList(CodeRequestDto requestDto){

		return codeMapper.getCodeList(requestDto);
	}

}
