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

/**
 * A handler that routes requests to other handlers based on the request path.
 */
public class RouterHandler implements Handler {
    HashSet<Route> routes = new HashSet<Route>();

    /**
     * Add a route to the router.
     * @param route The route to add.
     */
    public void addRoute(Route route) {
        routes.add(route);
    }

    /**
     * Add a route to the router that matches a predicate.
     * The path in the request passed to the handler will not be changed.
     * @param predicate The predicate to match.
     * @param handler The handler to call if the predicate matches.
     */
    public void addRoute(Predicate<Path> predicate, Handler handler) {
        routes.add(new Route(predicate, handler));
    }

    /**
     * Add a route to the router that matches a predicate while converting the path.
     * @param predicate The predicate to match.
     * @param pathConverter The function to convert the path.
     * @param handler The handler to call if the predicate matches.
     */
    public void addRoute(Predicate<Path> predicate, Function<Path, Path> pathConverter, Handler handler) {
        routes.add(new Route(predicate, pathConverter, handler));
    }

    /**
     * Add a route based on a path prefix.
     * The path passed to the downstream handler will have the prefix removed.
     * @param prefix The prefix to match.
     * @param handler The handler to call if the prefix matches.
     */
    public void addRoute(Path prefix, Handler handler) {
        routes.add(new Route(p -> p.contains(prefix), p -> p.from(prefix), handler));
    }

    /**
     * Remove a route from the router.
     * @param route The route to remove.
     */
    public void removeRoute(Route route) {
        routes.remove(route);
    }

    /**
     * Get the route that matches a path. If multiple routes match, the first one added is returned. If no routes match, an empty optional is returned.
     * @param path The path to match.
     * @return The matching route, or an empty optional if no route matches.
     */
    public Optional<Route> getRoute(Path path) {
        for (Route route : routes) {
            if (route.matches(path)) {
                return Optional.of(route);
            }
        }
        return Optional.empty();
    }

    /**
     * Handle a request by routing it to the appropriate handler.
     */
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
