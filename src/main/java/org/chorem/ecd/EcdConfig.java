package org.chorem.ecd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class EcdConfig {

    private static final Logger logger = LoggerFactory.getLogger(EcdConfig.class);

    private final Properties props;

    public static final String STREAMS_DIR = "streams";
    public static final String NEWS_DIR = "news";
    public static final String WEATHER_DIR = "weather";

    public EcdConfig() {
        props = new Properties();
        try {
            props.load(getClass().getClassLoader().getResourceAsStream("ecd.properties"));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public String getApiPathPrefix() {
        return "/api/ecd";
    }

    public String getMediaPathPrefix() {
        return "/media/ecd";
    }

    public String getDataDir() {
        return props.getProperty("ecd.data.dir");
    }

    public String getUploadDir() { return props.getProperty("ecd.upload.dir"); }

    public String getUnoconvCmd() {
        return props.getProperty("ecd.cmd.unoconv");
    }

    public String getResizeCmd() {
        return props.getProperty("ecd.cmd.resize");
    }

    public Integer getDefaultStreamTiming() {
        return Integer.valueOf(props.getProperty("ecd.default.streamTiming"));
    }

    public Path getStreamsPath() {
        return Paths.get(getDataDir(), STREAMS_DIR);
    }

    public Path getAggregatedNewsPath() {
        return Paths.get(getDataDir(), NEWS_DIR, "aggregated.json");
    }

    public Path getWeatherForecastPath() {
        return Paths.get(getDataDir(), WEATHER_DIR, "forecast.json");
    }
}
