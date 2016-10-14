package se.root.ordersystem.exception;

public final class ServiceException extends Exception {

    private static final long serialVersionUID = 5574356107657852853L;

    public ServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ServiceException(String message) {
        super(message);
    }
}
