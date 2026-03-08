package kr.co.teamo.code.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.teamo.code.dto.CodeRequestDto;
import kr.co.teamo.code.dto.CodeResponseDto;
import kr.co.teamo.code.service.CodeService;
import kr.co.teamo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "공통코드 Controller", description = "공통코드 컨트롤러")
public class CodeController {

	private final CodeService codeService;

	@Operation(summary = "공통코드조회", description = "공통코드를 조회한다")
	@GetMapping("/codes")
	public ApiResponse<List<CodeResponseDto>> getCodeList(@ModelAttribute CodeRequestDto requestDto) {

		return ApiResponse.ok(codeService.getCodeList(requestDto));
	}

}
