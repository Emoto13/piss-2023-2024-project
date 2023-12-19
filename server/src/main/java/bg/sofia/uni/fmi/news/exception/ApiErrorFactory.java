package bg.sofia.uni.fmi.news.exception;

public class ApiErrorFactory {
    public static ApiError createError(String code) {
        ApiErrorType apiErrorType = ApiErrorType.fromString(code.trim());
        return switch (apiErrorType) {
            case API_KEY_DISABLED -> new ApiKeyDisabledError("API key was disabled");
            case API_KEY_EXHAUSTED -> new ApiKeyExhaustedError("API key was exhausted");
            case API_KEY_INVALID -> new InvalidApiKeyError("Invalid API key");
            case API_KEY_MISSING -> new MissingApiKeyError("Missing API key");
            case PARAMETER_INVALID -> new InvalidParameterError("Invalid parameter API key");
            case PARAMETERS_MISSING -> new MissingParametersError("Missing parameters");
            case RATE_LIMITED -> new RateLimitedError("API key was rate limited");
            case SOURCES_TOO_MANY -> new TooManySourcesError("Too many sources specified");
            case SOURCE_DOES_NOT_EXIST -> new NotExistingSourceError("Source doesn't exist");
            case UNEXPECTED_ERROR -> new UnexpectedServerError("Unexpected server error");
        };
    }
}
