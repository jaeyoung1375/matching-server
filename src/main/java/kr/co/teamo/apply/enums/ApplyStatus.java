package kr.co.teamo.apply.enums;

import java.util.Arrays;

import kr.co.teamo.common.code.CommonErrorCode;
import kr.co.teamo.common.exception.CustomException;

public enum ApplyStatus {

	WAIT("10"),
	ACCEPT("20"),
	REJECT("30");

	private final String code;

	 ApplyStatus(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static ApplyStatus from(String code) {
		return Arrays.stream(values())
				.filter(s -> s.code.equals(code))
				.findFirst()
				.orElseThrow(() -> new CustomException(CommonErrorCode.INVALID_STATUS_CD));
	}



}
