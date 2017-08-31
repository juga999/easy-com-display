package org.chorem.ecd;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.chorem.ecd.exception.InvalidArgumentException;
import org.chorem.ecd.service.PeriodicTasksExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.net.HttpURLConnection;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String args[]) throws Exception {
        Spark.threadPool(4);
        Spark.port(8084);

        Spark.exception(InvalidArgumentException.class, (e, req, resp) -> {
            logger.error(e.getMessage(), e);
            resp.status(HttpURLConnection.HTTP_BAD_REQUEST);
            resp.type("application/json");
            resp.body(String.format("{\"message\":\"%s\"}", e.getMessage()));
        });

        Spark.staticFileLocation("/public");

        Weld.newInstance().initialize();
        WeldContainer.current().select(PeriodicTasksExecutorService.class).get();

    }

}
