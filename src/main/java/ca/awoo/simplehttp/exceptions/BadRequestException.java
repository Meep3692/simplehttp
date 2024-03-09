package ca.awoo.simplehttp.exceptions;

public class BadRequestException extends HttpException {
    public BadRequestException(String message) {
        super(400, message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(400, message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(400, "Invalid Request", cause);
    }

    public BadRequestException() {
        super(400, "Invalid Request");
    }
}
