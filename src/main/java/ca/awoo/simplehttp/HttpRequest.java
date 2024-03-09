package ca.awoo.simplehttp;

import java.io.InputStream;
import java.util.HashMap;

public class HttpRequest {
    private final String method;
    private Path path;
    private final String version;
    private final HashMap<String, String> headers;
    private InputStream stream;

    public HttpRequest(String method, Path path, String version, HashMap<String, String> headers) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public boolean isValid(){
        return !method.equals("INVALID");
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public String toString() {
        return method + " " + path + " " + version + "\n" + headers;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HttpRequest)) {
            return false;
        }
        HttpRequest other = (HttpRequest) o;
        return method.equals(other.method) && path.equals(other.path) && version.equals(other.version) && headers.equals(other.headers);
    }

    public int hashCode() {
        return method.hashCode() ^ path.hashCode() ^ version.hashCode() ^ headers.hashCode();
    }
    
}
