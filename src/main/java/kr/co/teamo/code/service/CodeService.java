package kr.co.teamo.code.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.teamo.code.dto.CodeRequestDto;
import kr.co.teamo.code.dto.CodeResponseDto;
import kr.co.teamo.code.mapper.CodeMapper;
import kr.co.teamo.common.code.UserErrorCode;
import kr.co.teamo.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeService {

	private final CodeMapper codeMapper;

	public List<CodeResponseDto> getCodeList(CodeRequestDto requestDto){

		throw  new CustomException(UserErrorCode.INVALID_INFO);

		// return codeMapper.getCodeList(requestDto);
	}

}
