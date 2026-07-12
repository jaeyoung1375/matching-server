package kr.co.teamo.apply.enums;

public enum ApplyStatusCd {

	WAIT("10"),
	ACCEPT("20"),
	REJECT("30");

	private final String code;

	 ApplyStatusCd(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
