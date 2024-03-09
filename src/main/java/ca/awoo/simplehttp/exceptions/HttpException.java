package ca.awoo.simplehttp.exceptions;

public class HttpException extends Exception{

    private final int code;

    public HttpException(int code, String message, Throwable cause){
        super(message, cause);
        this.code = code;
    }

    public HttpException(int code, String message){
        super(message);
        this.code = code;
    }

    public HttpException(int code, Throwable cause){
        super("HTTP error " + code, cause);
        this.code = code;
    }

    public HttpException(int code){
        super("HTTP error " + code);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
