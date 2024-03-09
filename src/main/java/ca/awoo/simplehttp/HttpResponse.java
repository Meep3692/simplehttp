package ca.awoo.simplehttp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

public class HttpResponse {
    private int code;
    private String message;
    private final HashMap<String, String> headers = new HashMap<>();
    private final OutputStream stream;
    private boolean headersWritten = false;

    public HttpResponse(int code, String message, OutputStream stream){
        this.code = code;
        this.message = message;
        this.stream = stream;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public void write(byte[] data) throws IOException {
        if(!headersWritten){
            stream.write(("HTTP/1.1 " + code + " " + message + "\r\n").getBytes());
            for(String key : headers.keySet()){
                stream.write((key + ": " + headers.get(key) + "\r\n").getBytes());
            }
            stream.write("\r\n".getBytes());
            headersWritten = true;
        }
        stream.write(data);
    }

    public PrintWriter getWriter() throws IOException{
        write(new byte[0]);
        return new PrintWriter(stream);
    }

    public void close() throws IOException {
        stream.close();
    }
}
