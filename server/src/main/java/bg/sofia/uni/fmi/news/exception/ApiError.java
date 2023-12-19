package bg.sofia.uni.fmi.news.exception;

public abstract class ApiError extends Exception {
    public ApiError(String message) {
        super(message);
    }

    abstract public boolean isCritical();
}