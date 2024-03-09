package ca.awoo.simplehttp;

import java.util.HashMap;

import ca.awoo.praser.ParseContext;
import ca.awoo.praser.ParseException;
import ca.awoo.praser.Parser;
import ca.awoo.praser.StreamException;

public class HttpRequestParser implements Parser<Character, HttpRequest> {

    @Override
    public HttpRequest parse(ParseContext<Character> context) throws ParseException {
        try{
            //Read method
            String method = "";
            while(context.peek() != ' '){
                method += context.read();
            }
            context.read();
            //Read path
            String pathString = "";
            while(context.peek() != ' '){
                pathString += context.read();
            }
            Path path = new Path(pathString);
            context.read();
            //Read version
            String version = "";
            while(context.peek() != '\r'){
                version += context.read();
            }
            //Skip \r\n
            context.read();
            context.read();

            HashMap<String, String> headers = new HashMap<>();

            while(context.peek() != '\r'){
                String header = "";
                while(context.peek() != ':'){
                    header += context.read();
                }
                context.read();
                context.read();
                String value = "";
                while(context.peek() != '\r'){
                    value += context.read();
                }
                context.read();
                context.read();
                headers.put(header, value);
            }
            //Skip \r\n
            context.read();
            context.read();

            return new HttpRequest(method, path, version, headers);
            
        } catch (StreamException e){
            throw new ParseException("Exception while reading stream", e);
        }
    }
    
}
