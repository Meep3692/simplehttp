# Simple HTTP
A simple, lightweight, but powerful http server written in Java 8

## HttpListener
This is your server. The constructor takes the port to bind to and a Handler to handle requests. Optionally, it can take an ErrorHandler to handle errors.
```java
HttpListener listener = new HttpListener(8080 (request, response) -> {
    response.setHeader("Content-Type", "text/html");
    response.write("<h1>Hello world!</h1>".getBytes());
});
```

## Handler
The handler interface is where the real fun happens. All it does it take an HTTPRequest and HTTPResponse object. Everything else is up to implementation. Take a look at [RouterHandler](/src/main/java/ca/awoo/simplehttp/router/RouterHandler.java) for example. It implements a router using the Handler interface. If you wanted a root router to route requests coming to the server, it's as easy as:
```java
RouterHandler router = new RouterHandler();
//Use router.addRoute to add routes
router.addRoute(new Path("/foo"), fooHandler);
//The router can route based on a predicate over Path, and can take a Function<Path, Path> to change the path passed to the downstream handler.
router.addRoute(path -> path.getPathParts()[0].equals("bar") && path.getPathParts()[2].equals("create"), barCreateHandler);
HttpListener listener = new HttpListener(port, router);
```
This method allows for high level concepts like routing and mvc without shackling youself to them. This makes simplehttp simple, lightweight, but still very powerful.

The `handle` method of Handler may throw an HTTPException, which is then handled by the ErrorHandler on your HttpListener. You could, of course, have your root Handler catch HttpExceptions from downstream handlers and handle them there if you wanted.