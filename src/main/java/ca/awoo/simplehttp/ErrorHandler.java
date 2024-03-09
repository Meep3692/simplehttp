package ca.awoo.simplehttp;

import ca.awoo.simplehttp.exceptions.HttpException;

public interface ErrorHandler {
    public void handle(HttpRequest request, HttpResponse response, HttpException e);
}
