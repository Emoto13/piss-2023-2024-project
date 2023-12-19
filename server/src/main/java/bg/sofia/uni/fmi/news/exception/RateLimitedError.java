package bg.sofia.uni.fmi.news.exception;

public class RateLimitedError extends ApiError {
    public RateLimitedError(String message) {
        super(message);
    }

    @Override
    public boolean isCritical() {
        return true;
    }
}
