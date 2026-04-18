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
                getRequiredEnv("TENNIS_DB_URL"),
                getRequiredEnv("TENNIS_DB_USERNAME"),
                getEnvOrDefault("TENNIS_DB_PASSWORD", ""),
                Integer.parseInt(getEnvOrDefault("TENNIS_CLEANUP_PERIOD_MINUTES", "30")),
                Integer.parseInt(getEnvOrDefault("TENNIS_STALE_MATCH_LIFETIME_MINUTES", "60"))
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