package org.chorem.ecd.endpoint;

import org.chorem.ecd.model.settings.Location;
import org.chorem.ecd.service.LocationService;
import spark.Request;
import spark.Response;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class LocationEndpoint extends Endpoint {

    @Inject
    protected LocationService locationService;

    @Override
    protected void init() {
        registerJsonProducer(ecdApiPath("/cms/weather"), this::getWeatherForecast);
        registerJsonProducer(ecdApiPath("/settings/location"), this::getLocation);
        registerFormDataConsumer(ecdApiPath("/settings/location"), this::setLocation);
    }

    private Object getWeatherForecast(Request request, Response response) {
        return locationService.getWeatherForecast();
    }

    private Object getLocation(Request req, Response resp) {
        return locationService.getLocation();
    }

    private Object setLocation(Request req, Response resp) throws Exception {
        Location location = getObject(req, Location.class);
        locationService.setLocation(location);
        return SUCCESS;
    }

    private static final class LocationName {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
