package bg.sofia.uni.fmi.news.exception;

public class MissingParametersError extends ApiError {
    public MissingParametersError(String message) {
        super(message);
    }

    @Override
    public boolean isCritical() {
        return true;
    }
}
