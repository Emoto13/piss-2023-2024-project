package bg.sofia.uni.fmi.news.exception;

public class InvalidParameterError extends ApiError {

    public InvalidParameterError(String message) {
        super(message);
    }

    @Override
    public boolean isCritical() {
        return true;
    }
}
