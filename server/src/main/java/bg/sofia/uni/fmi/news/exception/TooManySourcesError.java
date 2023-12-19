package bg.sofia.uni.fmi.news.exception;

public class TooManySourcesError extends ApiError {
    public TooManySourcesError(String message) {
        super(message);
    }

    @Override
    public boolean isCritical() {
        return true;
    }
}