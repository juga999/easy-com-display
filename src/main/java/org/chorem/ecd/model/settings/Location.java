package org.chorem.ecd.model.settings;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public class Location {
    private int id;

    private String name;

    private String weatherForecastUrl;

    public Location() {
    }

    public Location(String name, String weatherForecastUrl) {
        this.name = name;
        this.weatherForecastUrl = weatherForecastUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeatherForecastUrl() {
        return weatherForecastUrl;
    }

    public URL getWeatherForecastParsedUrl() {
        URL parsedUrl = null;
        try {
            parsedUrl = new URL(getWeatherForecastUrl());
        } catch (MalformedURLException e) {
        }
        return parsedUrl;
    }

    public void setWeatherForecastUrl(String weatherForecastUrl) {
        this.weatherForecastUrl = weatherForecastUrl;
    }

}
