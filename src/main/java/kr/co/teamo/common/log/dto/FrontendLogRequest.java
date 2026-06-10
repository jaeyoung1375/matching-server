package kr.co.teamo.common.log.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FrontendLogRequest {
    @NotBlank
    private String level;

    @NotBlank
    private String message;

    private String pageUrl;
    private String componentName;
    private String stackTrace;
    private String userAgent;
}
