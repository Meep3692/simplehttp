package ca.awoo.simplehttp.exceptions;

public class InternalServerException extends HttpException {
    public InternalServerException(String message) {
        super(500, message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(500, message, cause);
    }

    public InternalServerException(Throwable cause) {
        super(500, "Internal Server Error", cause);
    }

    public InternalServerException() {
        super(500, "Internal Server Error");
    }
}
