package ca.awoo.simplehttp.router;

import java.util.HashSet;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import ca.awoo.simplehttp.Handler;
import ca.awoo.simplehttp.HttpRequest;
import ca.awoo.simplehttp.HttpResponse;
import ca.awoo.simplehttp.Path;
import ca.awoo.simplehttp.exceptions.HttpException;
import ca.awoo.simplehttp.exceptions.NotFoundException;

public class RouterHandler implements Handler {
    HashSet<Route> routes = new HashSet<Route>();

    public void addRoute(Route route) {
        routes.add(route);
    }

    public void addRoute(Predicate<Path> predicate, Handler handler) {
        routes.add(new Route(predicate, handler));
    }

    public void addRoute(Predicate<Path> predicate, Function<Path, Path> pathConverter, Handler handler) {
        routes.add(new Route(predicate, pathConverter, handler));
    }

    public void addRoute(Path path, Handler handler) {
        routes.add(new Route(p -> p.contains(path), p -> p.from(path), handler));
    }

    public void removeRoute(Route route) {
        routes.remove(route);
    }

    public Optional<Route> getRoute(Path path) {
        for (Route route : routes) {
            if (route.matches(path)) {
                return Optional.of(route);
            }
        }
        return Optional.empty();
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws HttpException {
        Path path = request.getPath();
        Optional<Route> route = getRoute(path);
        if (route.isPresent()) {
            request.setPath(route.get().convert(path));
            route.get().getHandler().handle(request, response);
        } else {
            throw new NotFoundException();
        }
    }

}
