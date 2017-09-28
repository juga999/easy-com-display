package org.chorem.ecd.service;

import com.google.common.collect.Maps;
import org.chorem.ecd.dao.SettingsDao;
import org.chorem.ecd.dao.TransactionRequired;
import org.chorem.ecd.model.settings.Location;
import org.chorem.ecd.model.weather.WeatherForecast;
import org.chorem.ecd.task.WeatherForecastBuilderPeriodicTask;
import org.chorem.ecd.EcdConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Optional;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private static final int TIMEOUT_MS = 10 * 1000;

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
        settingsDao.setLocation(location);

        periodicTasksExecutorService.schedule(getWeatherForecastBuilderTask());
    }

    private WeatherForecastBuilderPeriodicTask getWeatherForecastBuilderTask() {
        URL forecastUrl = settingsDao.getLocation().getWeatherForecastParsedUrl();
        WeatherForecastBuilderPeriodicTask task = new WeatherForecastBuilderPeriodicTask(forecastUrl);
        task.setConfig(config);
        task.setJsonService(jsonService);
        return task;
    }

    public Map<String, String> searchLocation(String name) {
        Map<String, String> results = Maps.newHashMap();
        try {
            String searchUrl = String.format(config.getLocationSearchUrlFormat(), URLEncoder.encode(name, "UTF-8"));
            URL parsedSearchUrl = new URL(searchUrl);
            Document document = Jsoup.parse(parsedSearchUrl, TIMEOUT_MS);
            Element resultElt = document.select("table#res_entite").first();
            if (resultElt != null) {
                results = parseLocationSearchResult(resultElt);
            }
        } catch(IOException e) {
            logger.error(e.getMessage(), e);
        }
        return results;
    }

    private  Map<String, String> parseLocationSearchResult(Element resultElt) {
        Map<String, String> results = Maps.newHashMap();
        Elements resultRows = resultElt.select("tr.res");
        for (Element rowElt : resultRows) {
            Element typeElt = rowElt.select("td.type").first();
            if ("Ville".equals(typeElt.text())) {
                String onclickAttr = rowElt.attr("onclick");
                String href = Optional.ofNullable(onclickAttr)
                        .filter(s -> s.contains("="))
                        .map(s -> s.split("="))
                        .map(s -> s[1].trim().replace("'", ""))
                        .map(s -> s.replace(";", ""))
                        .orElse("");
                Element nameElt = rowElt.select("td.nom").first();
                String city = Optional.ofNullable(nameElt)
                        .map(Element::text)
                        .map(s -> s.replace("<b>", ""))
                        .map(s -> s.replace("</b>", ""))
                        .orElse("");
                if (href.length() > 0 && city.length() > 0) {
                    results.put(city, href);
                }
            }
        }
        return results;
    }
}
