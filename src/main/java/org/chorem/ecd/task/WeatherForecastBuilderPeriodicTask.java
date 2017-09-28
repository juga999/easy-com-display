package org.chorem.ecd.task;

import com.google.common.collect.Range;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.chorem.ecd.model.weather.WeatherForecast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public class WeatherForecastBuilderPeriodicTask extends EcdPeriodicTask {

    private static final Logger logger = LoggerFactory.getLogger(WeatherForecastBuilderPeriodicTask.class);

    private URL forecastUrl;

    public static final String NAME = "WeatherForecastBuilder";

    private static final Range<Integer> MORNING_RANGE = Range.closed(4, 12);
    private static final Range<Integer> AFTERNOON_RANGE = Range.closed(13, 18);

    public WeatherForecastBuilderPeriodicTask(String forecastUrl) {
        String url = forecastUrl.replace("/meteo/localite/", "/services/json/");
        try {
            this.forecastUrl = new URL(url);
        } catch (MalformedURLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void run() {
        try {
            WeatherForecast forecast = fetchWeatherForecast();
            Files.deleteIfExists(config.getWeatherForecastPath());
            jsonService.saveToJson(forecast, config.getWeatherForecastPath());
        } catch (Exception e) {
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
            InputStream inputStream = forecastUrl.openStream();
            String jsonData = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(jsonData);
            forecast = parseForecast(jsonElement);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return forecast;
    }

    private WeatherForecast parseForecast(JsonElement rootElement) {
        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hourOfDay < 23) {
            hourOfDay += 1;
        }
        String hourOfDayMemberName = String.format("%dH00", hourOfDay);
        JsonObject hourlyDataJsonObject = rootElement.getAsJsonObject()
                .get("fcst_day_0").getAsJsonObject()
                .get("hourly_data").getAsJsonObject()
                .get(hourOfDayMemberName).getAsJsonObject();

        String condition = hourlyDataJsonObject.get("CONDITION").getAsString();
        String temperature = String.valueOf(hourlyDataJsonObject.get("TMP2m").getAsFloat()) + "\u00b0";
        String humidity = String.valueOf(hourlyDataJsonObject.get("RH2m").getAsFloat());


        String period;
        if (MORNING_RANGE.contains(hourOfDay)) {
            period = "morning";
        } else if (AFTERNOON_RANGE.contains(hourOfDay)) {
            period = "afternoon";
        } else {
            period = "evening";
        }

        WeatherForecast forecast = new WeatherForecast();
        forecast.setProvider("www.prevision-meteo.ch");
        forecast.setPeriod(period);
        forecast.setSky(condition);
        forecast.setTemperature(temperature);
        forecast.setHumidity(humidity);
        return forecast;
    }

}
