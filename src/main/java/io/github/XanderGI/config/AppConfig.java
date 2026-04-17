package io.github.XanderGI.config;

public record AppConfig(
        String dbUrl,
        String dbUsername,
        String dbPassword,
        int cleanupPeriodMinutes,
        int staleMatchLifetimeMinutes
) {
    public static AppConfig loadFromEnvironment() {
        return new AppConfig(
                getRequiredEnv("DB_URL"),
                getRequiredEnv("DB_USERNAME"),
                getEnvOrDefault("DB_PASSWORD", ""),
                Integer.parseInt(getEnvOrDefault("CLEANUP_PERIOD_MINUTES", "30")),
                Integer.parseInt(getEnvOrDefault("STALE_MATCH_LIFETIME_MINUTES", "60"))
        );
    }

    private static String getRequiredEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("CRITICAL: Missing required environment variable: " + key);
        }
        return value;
    }

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value != null && !value.isBlank()) {
            return value;
        }
        return defaultValue;
    }
}