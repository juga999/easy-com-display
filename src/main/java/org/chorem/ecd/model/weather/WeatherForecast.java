package org.chorem.ecd.model.weather;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public class WeatherForecast {

    private String provider;

    private String period;

    private String temperature;

    private String feltTemperature;

    private String sky;

    private String rain;

    private String wind;

    private String humidity;

    public WeatherForecast() {
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getFeltTemperature() {
        return feltTemperature;
    }

    public void setFeltTemperature(String feltTemperature) {
        this.feltTemperature = feltTemperature;
    }

    public String getSky() {
        return sky;
    }

    public void setSky(String sky) {
        this.sky = sky;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
