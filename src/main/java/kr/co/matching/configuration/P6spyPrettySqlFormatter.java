package kr.co.matching.configuration;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import java.util.Locale;

public class P6spyPrettySqlFormatter implements MessageFormattingStrategy {

    private static final String NEW_LINE = System.lineSeparator();

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category,
            String prepared, String sql, String url) {

		if (sql == null || sql.trim().isEmpty() || !"statement".equalsIgnoreCase(category)) {
		return ""; // null 반환 금지
		}

		 // 키워드 기준 줄바꿈 + 대문자
	    String formattedSql = sql.trim()
	                             .replaceAll("(?i)\\b(FROM|WHERE|GROUP BY|ORDER BY|JOIN|ON|AND|OR)\\b", "\n$1")
	                             .toUpperCase(Locale.ROOT);

	    return formattedSql;
		}

    private String formatSql(String sql) {
        String trimmed = sql.trim();

        // DDL은 CREATE, ALTER, COMMENT 기준 줄바꿈
        if (trimmed.toLowerCase(Locale.ROOT).startsWith("create") ||
            trimmed.toLowerCase(Locale.ROOT).startsWith("alter") ||
            trimmed.toLowerCase(Locale.ROOT).startsWith("comment")) {

            return trimmed.replaceAll("(?i)\\b(CREATE|ALTER|COMMENT)\\b", "\n$1")
                          .toUpperCase(Locale.ROOT);
        }

        // SELECT, UPDATE, DELETE 등 일반 쿼리 키워드 기준 줄바꿈
        return trimmed.replaceAll("(?i)\\b(FROM|WHERE|GROUP BY|ORDER BY|JOIN|ON|AND|OR)\\b", "\n$1")
                      .toUpperCase(Locale.ROOT);
    }


    private boolean isStatement(String category) {
        return category != null && category.equalsIgnoreCase("statement");
    }
}