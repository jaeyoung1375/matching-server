package kr.co.teamo.configuration;

import com.p6spy.engine.spy.P6SpyOptions;
import org.springframework.context.annotation.Configuration;

@Configuration
public class P6SpyConfig {

    public P6SpyConfig() {
        // Formatter 지정
        P6SpyOptions.getActiveInstance().setLogMessageFormat(kr.co.teamo.configuration.P6spyPrettySqlFormatter.class.getName());
        // Appender 지정
        P6SpyOptions.getActiveInstance().setAppender(kr.co.teamo.configuration.CustomP6SpyLogger.class.getName());
    }
}