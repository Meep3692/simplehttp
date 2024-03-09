package ca.awoo.simplehttp;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import ca.awoo.simplehttp.exceptions.BadRequestException;
import ca.awoo.simplehttp.exceptions.HttpException;

public class BadRequestTest {
    @Test
    public void testBadRequest() throws Exception {
        HttpListener listener = new HttpListener(8080, new Handler() {
            @Override
            public void handle(HttpRequest request, HttpResponse response) throws HttpException {
                throw new BadRequestException();
            }
        });
        Thread listenerThread = new Thread(() -> {
            listener.start();
        });
        listenerThread.start();
        while(!listener.isReady()){
            Thread.sleep(100);
        }
        URL url = new URL("http://localhost:8080");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        assert connection.getResponseCode() == 400;
        listener.close();
        listenerThread.join();
    }
}
