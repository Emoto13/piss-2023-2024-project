package bg.sofia.uni.fmi.news.exception;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ApiErrorFactoryTest {
    @Test
    void TestCreateErrorWorksCorrectly() {
        List<String> errorCodes = Arrays.asList(
                ApiErrorType.API_KEY_DISABLED.getName(), ApiErrorType.API_KEY_EXHAUSTED.getName(),
                ApiErrorType.API_KEY_INVALID.getName(), ApiErrorType.API_KEY_MISSING.getName(),
                ApiErrorType.PARAMETER_INVALID.getName(), ApiErrorType.PARAMETERS_MISSING.getName(),
                ApiErrorType.RATE_LIMITED.getName(), ApiErrorType.SOURCE_DOES_NOT_EXIST.getName(),
                ApiErrorType.SOURCES_TOO_MANY.getName(), ApiErrorType.UNEXPECTED_ERROR.getName(),
                "non-existing-error-code"
        );
        List<ApiError> errors = Arrays.asList(new ApiKeyDisabledError(""), new ApiKeyExhaustedError(""),
                new InvalidApiKeyError(""), new MissingApiKeyError(""), new InvalidParameterError(""),
                new MissingParametersError(""), new RateLimitedError(""), new NotExistingSourceError(""),
                new TooManySourcesError(""), new UnexpectedServerError(""), new UnexpectedServerError("")
        );
        List<Boolean> shouldBeCritical = Arrays.asList(true, true, true, true, true, true, true, true, true, false,
                false);

        for (int i = 0; i < errorCodes.size(); i++) {
            String errorCode = errorCodes.get(i);
            ApiError expectedError = errors.get(i);
            boolean isCritical = shouldBeCritical.get(i);
            ApiError error = ApiErrorFactory.createError(errorCode);
            assertInstanceOf(expectedError.getClass(), error, "should create api error correctly");
            assertEquals(isCritical, error.isCritical(), "should have correct critical status correctly");
        }
    }
}
