package kr.co.matching.menu.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.matching.common.ApiResponse;
import kr.co.matching.menu.dto.MenuDto;
import kr.co.matching.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MenuController {

	private final MenuService menuService;

	@GetMapping("/menuList")
	public ApiResponse<List<MenuDto>> menuList() {
		return ApiResponse.ok(menuService.menuList());

	}

}
