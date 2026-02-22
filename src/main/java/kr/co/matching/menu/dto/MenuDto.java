package kr.co.matching.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {

	private String menuId; // 메뉴아이디

	private String menuNm; // 메뉴명

	private String menuUrl; // 메뉴URL

	private String menuLevel; // 메뉴레벨

	private String menuOrd; // 메뉴정렬순서

	private String upMenuId; // 상위메뉴아이디

	private String topYn; // 상위여부

	private String useYn; // 사용여부

	private String regId; // 동록자

	private String regDt; // 등록일

}
