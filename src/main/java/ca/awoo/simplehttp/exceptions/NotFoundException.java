package ca.awoo.simplehttp.exceptions;

public class NotFoundException extends HttpException {
    public NotFoundException(String message) {
        super(404, message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(404, message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(404, "Not Found", cause);
    }

    public NotFoundException() {
        super(404, "Not Found");
    }
}
