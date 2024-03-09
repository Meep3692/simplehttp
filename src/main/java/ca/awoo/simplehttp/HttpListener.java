package ca.awoo.simplehttp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

import ca.awoo.simplehttp.exceptions.BadRequestException;
import ca.awoo.simplehttp.exceptions.HttpException;
import ca.awoo.simplehttp.exceptions.InternalServerException;

public class HttpListener {
    private ServerSocket serverSocket;
    private Handler handler;
    private ErrorHandler errorHandler;

    public HttpListener(int port, Handler handler, ErrorHandler errorHandler) throws IOException {
        serverSocket = new ServerSocket(port);
        this.handler = handler;
        this.errorHandler = errorHandler;
    }

    public HttpListener(int port, Handler handler) throws IOException {
        this(port, handler, (request, response, e) -> {
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
            response.setHeader("Content-Type", "text/plain");
            try {
                response.write("There was an error processing your request\n".getBytes());
                e.printStackTrace(response.getWriter());
            } catch (IOException e1) {
                //We're in a bad spot if we get here
                e1.printStackTrace();
            }
        });
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public HttpConnection accept() throws IOException {
        return new HttpConnection(serverSocket.accept());
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    public boolean isClosed() {
        return serverSocket.isClosed();
    }

    public boolean isReady(){
        return !isClosed() && serverSocket.isBound();
    }

    public void start(){
        while(!isClosed()){
            try{
                HttpConnection connection = accept();
                new Thread(() -> {
                    try{
                        HttpRequest request = connection.getRequest();
                        HttpResponse response = connection.createResponse();
                        try {
                            if(!request.isValid()){
                                throw new BadRequestException();
                            }
                            try{
                                handler.handle(request, response);
                            } catch(Exception e) {
                                throw new InternalServerException(e);
                            }
                        } catch(HttpException e) {
                            errorHandler.handle(request, response, e);
                        }
                        connection.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }).start();
            } catch (SocketException e){
                //We're closing the server
                return;
            } catch (IOException e){
                //TODO: we should do something here
                e.printStackTrace();
            }
        }
    }
}
