package kr.co.teamo.code.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import kr.co.teamo.code.dto.CodeResponseDto;
import kr.co.teamo.code.mapper.CodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeService {

	private final CodeMapper codeMapper;

	public Map<String,List<CodeResponseDto>> getCodeList(List<String> comCdIds){

		List<CodeResponseDto> codes = codeMapper.getCodeList(comCdIds);

		 return codes.stream()
				 .collect(Collectors.groupingBy(CodeResponseDto::getComCdId));
	}

}
