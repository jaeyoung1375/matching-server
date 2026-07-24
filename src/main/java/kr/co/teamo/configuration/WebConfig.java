package kr.co.teamo.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.teamo.common.interceptor.ApiAccessLogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final ApiAccessLogInterceptor apiAccessLogInterceptor;

	@Value("${file.upload.path}")
	private String uploadPath;

	@Value("${file.upload.server}")
	private String uploadServer;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api/v1",
            c -> c.isAnnotationPresent(RestController.class)
            && !c.getPackage().getName().startsWith("org.springdoc"));
    }

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(apiAccessLogInterceptor)
				.addPathPatterns("/api/**")
				.excludePathPatterns(
						"/api/v1/public/client-logs",
						"/api/v1/admin/logs/**"
				);

		// 업로드 정적 리소스는 프론트(localhost:3000)에서 <img>로 크로스 오리진 로드하므로
		// Chrome ORB(Opaque Response Blocking)에 걸리지 않도록 CORP 헤더를 명시한다.
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
				response.setHeader("Cross-Origin-Resource-Policy", "cross-origin");
				return true;
			}
		}).addPathPatterns(uploadServer + "/**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry
			.addResourceHandler(uploadServer + "/**") // 브라우저에서 접근할 URL
			.addResourceLocations("file:"+uploadPath + "/"); // 실제 서버 파일 경로
	}


}
