package bg.sofia.uni.fmi.news.exception;

public class ApiKeyDisabledError extends ApiError {
    public ApiKeyDisabledError(String message) {
        super(message);
    }

    @Override
    public boolean isCritical() {
        return true;
    }
}
