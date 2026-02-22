package kr.co.matching.menu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.matching.menu.dto.MenuDto;
import kr.co.matching.menu.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuService {

	private final MenuMapper menuMapper;

	/**
	 * 메뉴목록 조회
	 * @return List<MenuDto>
	 */
	public List<MenuDto> menuList(){
		return menuMapper.menuList();
	}



}
