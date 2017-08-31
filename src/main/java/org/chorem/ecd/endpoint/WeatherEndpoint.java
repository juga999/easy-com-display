package org.chorem.ecd.endpoint;

import org.chorem.ecd.model.settings.Location;
import org.chorem.ecd.service.WeatherService;
import spark.Request;
import spark.Response;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class WeatherEndpoint extends Endpoint {

    @Inject
    protected WeatherService weatherService;

    @Override
    protected void init() {
        registerJsonProducer(ecdApiPath("/cms/weather"), this::getWeatherForecast);
        registerJsonProducer(ecdApiPath("/settings/location"), this::getLocation);
        registerFormDataConsumer(ecdApiPath("/settings/location"), this::setLocation);
    }

    private Object getWeatherForecast(Request request, Response response) {
        return weatherService.getWeatherForecast();
    }

    private Object getLocation(Request req, Response resp) {
        return weatherService.getLocation();
    }

    private Object setLocation(Request req, Response resp) throws Exception {
        Location location = getFormDataObjectOrNull(req, "location", Location.class);
        weatherService.setLocation(location);
        return SUCCESS;
    }
}
