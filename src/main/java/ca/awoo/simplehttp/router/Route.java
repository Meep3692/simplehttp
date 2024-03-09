package ca.awoo.simplehttp.router;

import java.util.function.Function;
import java.util.function.Predicate;

import ca.awoo.simplehttp.Handler;
import ca.awoo.simplehttp.Path;

public class Route {
    private final Predicate<Path> predicate;
    private final Function<Path, Path> pathConverter;
    private final Handler handler;

    public Route(Predicate<Path> predicate, Function<Path, Path> pathConverter, Handler handler) {
        this.predicate = predicate;
        this.pathConverter = pathConverter;
        this.handler = handler;
    }

    public Route(Predicate<Path> predicate, Handler handler) {
        this(predicate, Function.identity(), handler);
    }

    public boolean matches(Path path) {
        return predicate.test(path);
    }

    public Path convert(Path path) {
        return pathConverter.apply(path);
    }

    public Handler getHandler() {
        return handler;
    }
}
