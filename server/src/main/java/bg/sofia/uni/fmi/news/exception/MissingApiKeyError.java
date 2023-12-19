package bg.sofia.uni.fmi.news.exception;

public class MissingApiKeyError extends ApiError {
    public MissingApiKeyError(String message) {
        super(message);
    }

    @Override
    public boolean isCritical() {
        return true;
    }
}
