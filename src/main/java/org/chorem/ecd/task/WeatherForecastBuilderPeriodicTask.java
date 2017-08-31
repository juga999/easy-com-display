package org.chorem.ecd.task;

import org.chorem.ecd.model.weather.WeatherForecast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public class WeatherForecastBuilderPeriodicTask extends EcdPeriodicTask {

    private static final Logger logger = LoggerFactory.getLogger(WeatherForecastBuilderPeriodicTask.class);

    private final URL forecastUrl;

    public static final String NAME = "WeatherForecastBuilder";

    private static final int TIMEOUT_MS = 10 * 1000;

    public WeatherForecastBuilderPeriodicTask(URL forecastUrl) {
        this.forecastUrl = forecastUrl;
    }

    @Override
    public void run() {
        WeatherForecast forecast = fetchWeatherForecast();

        try {
            jsonService.saveToJson(forecast, config.getWeatherForecastPath());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Integer getPeriodicity() {
        return 1;
    }

    @Override
    public TimeUnit getPeriodicityUnit() {
        return TimeUnit.HOURS;
    }

    private WeatherForecast fetchWeatherForecast() {
        WeatherForecast forecast = new WeatherForecast();
        if (forecastUrl == null) {
            return forecast;
        }

        try {
            Document document;
            if (forecastUrl.getProtocol().startsWith("file")) {
                document = Jsoup.parse(new File(forecastUrl.toURI()), StandardCharsets.ISO_8859_1.name());
            } else {
                document = Jsoup.parse(forecastUrl, TIMEOUT_MS);
            }
            Element forecastElt = document.select("div#previs_quart_jour_0").first();
            if (forecastElt != null) {
                forecast = parseForecast(forecastElt);
            }
        } catch (URISyntaxException | IOException e) {
            logger.error(e.getMessage(), e);
        }

        return forecast;
    }

    private WeatherForecast parseForecast(Element forecastElt) {
        Element dayPeriodElt = forecastElt.select("div.nom_quart_jour").first();
        String dayPeriod = dayPeriodElt.text();

        Element tempElt = forecastElt.select("div.tempe").first();
        String temperature = tempElt.text();

        Element feltTempElt = forecastElt.select("div.ressentie").first();
        String feltTemperature = feltTempElt.text();

        Element skyElt = forecastElt.select("span.phrase_ciel").first();
        String sky = skyElt.text();

        Element rainElt = forecastElt.select("span.phrase_precip").first();
        String rain = rainElt.text();

        Element windElt = forecastElt.select("div.phrase_dir_vent").first();
        String wind = windElt.text();

        WeatherForecast forecast = new WeatherForecast();
        forecast.setPeriod(dayPeriod);
        forecast.setTemperature(temperature);
        forecast.setFeltTemperature(feltTemperature);
        forecast.setSky(sky);
        forecast.setRain(rain);
        forecast.setWind(wind);

        return forecast;
    }

}
