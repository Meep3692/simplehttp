package ca.awoo.simplehttp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

public class Path {
    private final String original;
    private final String path;
    private final HashMap<String, String> params = new HashMap<>();
    private final String[] pathParts;

    public Path(String path) {
        this.original = path;
        this.path = path.split("\\?")[0];
        this.pathParts = this.path.split("/");
        String queryString = path.split("\\?").length > 1 ? path.split("\\?")[1] : "";
        for (String part : queryString.split("&")) {
            String[] pair = part.split("=");
            if (pair.length == 2) {
                try {
                    params.put(URLDecoder.decode(pair[0], "UTF-8"), URLDecoder.decode(pair[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("You don't have UTF-8 support.", e);
                }
                
            }
        }
    }

    public String getOriginal() {
        return original;
    }

    public String getPath() {
        return path;
    }

    public String getParam(String name) {
        return params.get(name);
    }

    public String[] getPathParts() {
        return pathParts;
    }

    public boolean contains(Path other){
        String[] otherParts = other.getPathParts();
        if(otherParts.length > pathParts.length){
            return false;
        }
        for(int i = 0; i < otherParts.length; i++){
            if(!otherParts[i].equals(pathParts[i])){
                return false;
            }
        }
        return true;
    }

    public Path from(Path other){
        String[] otherParts = other.getPathParts();
        if(!contains(other)){
            throw new IllegalArgumentException("Path does not contain other path");
        }
        String newPath = "";
        for(int i = otherParts.length; i < pathParts.length; i++){
            newPath += "/" + pathParts[i];
        }
        return new Path(newPath);
    }
}
