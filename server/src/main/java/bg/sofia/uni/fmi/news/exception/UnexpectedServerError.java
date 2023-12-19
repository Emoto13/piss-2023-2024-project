package bg.sofia.uni.fmi.news.exception;

public class UnexpectedServerError extends ApiError {
    public UnexpectedServerError(String message) {
        super(message);
    }

    @Override
    public boolean isCritical() {
        return false;
    }
}
