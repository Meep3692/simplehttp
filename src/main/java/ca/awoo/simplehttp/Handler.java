package ca.awoo.simplehttp;

import ca.awoo.simplehttp.exceptions.HttpException;

public interface Handler {
    public void handle(HttpRequest request, HttpResponse response) throws HttpException;
}
