package ca.awoo.simplehttp;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import ca.awoo.praser.ParseContext;
import ca.awoo.praser.ParseException;
import ca.awoo.praser.character.CharacterStream;

public class HttpConnection {
    private static final HttpRequestParser parser = new HttpRequestParser();

    private final Socket socket;
    private final HttpRequest request;

    public HttpConnection(Socket socket) throws IOException {
        this.socket = socket;
        HttpRequest request;
        try{
            request = parser.parse(new ParseContext<>(new CharacterStream(socket.getInputStream(), "UTF-8")));
            request.setStream(socket.getInputStream());
        } catch (ParseException e){
            request = new HttpRequest("INVALID", new Path("INVALID"), "INVALID", new HashMap<>());
        }
        this.request = request;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public Socket getSocket() {
        return socket;
    }

    public HttpResponse createResponse() throws IOException {
        return new HttpResponse(200, "OK", socket.getOutputStream());
    }

    public void close() throws IOException {
        socket.close();
    }
    
}
