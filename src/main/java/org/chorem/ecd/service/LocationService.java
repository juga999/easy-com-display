package org.chorem.ecd.service;

import org.chorem.ecd.dao.SettingsDao;
import org.chorem.ecd.dao.TransactionRequired;
import org.chorem.ecd.model.settings.Location;
import org.chorem.ecd.model.weather.WeatherForecast;
import org.chorem.ecd.task.WeatherForecastBuilderPeriodicTask;
import org.chorem.ecd.EcdConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Inject
    protected SettingsDao settingsDao;

    @Inject
    protected EcdConfig config;

    @Inject
    protected JsonService jsonService;

    @Inject
    protected PeriodicTasksExecutorService periodicTasksExecutorService;

    @PostConstruct
    public void init() {
        periodicTasksExecutorService.schedule(getWeatherForecastBuilderTask());
    }

    public WeatherForecast getWeatherForecast() {
        try {
            WeatherForecast weatherForecast = jsonService.loadFromJson(WeatherForecast.class, config.getWeatherForecastPath());
            return Optional.ofNullable(weatherForecast).orElse(new WeatherForecast());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public Location getLocation() {
        return settingsDao.getLocation();
    }

    @TransactionRequired
    public void setLocation(Location location) {
        String weatherForecastUrl = location.getWeatherForecastUrl().replace("/meteo/localite/", "/services/json/");
        location.setWeatherForecastUrl(weatherForecastUrl);
        settingsDao.setLocation(location);

        periodicTasksExecutorService.schedule(getWeatherForecastBuilderTask());
    }

    private WeatherForecastBuilderPeriodicTask getWeatherForecastBuilderTask() {
        String forecastUrl = settingsDao.getLocation().getWeatherForecastUrl();
        WeatherForecastBuilderPeriodicTask task = new WeatherForecastBuilderPeriodicTask(forecastUrl);
        task.setConfig(config);
        task.setJsonService(jsonService);
        return task;
    }

}
