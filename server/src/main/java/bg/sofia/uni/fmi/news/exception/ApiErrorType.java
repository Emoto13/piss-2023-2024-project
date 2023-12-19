package bg.sofia.uni.fmi.news.exception;

public enum ApiErrorType {
    API_KEY_DISABLED("apiKeyDisabled"),
    API_KEY_EXHAUSTED("apiKeyExhausted"),
    API_KEY_INVALID("apiKeyInvalid"),
    API_KEY_MISSING("apiKeyMissing"),
    PARAMETER_INVALID("parameterInvalid"),
    PARAMETERS_MISSING("parametersMissing"),
    RATE_LIMITED("rateLimited"),
    SOURCES_TOO_MANY("sourcesTooMany"),
    SOURCE_DOES_NOT_EXIST("sourceDoesNotExist"),
    UNEXPECTED_ERROR("unexpectedError");
    private final String name;

    ApiErrorType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ApiErrorType fromString(String value) {
        for (ApiErrorType errorType : values()) {
            if (errorType.getName().equalsIgnoreCase(value)) {
                return errorType;
            }
        }
        return UNEXPECTED_ERROR;
    }
}
