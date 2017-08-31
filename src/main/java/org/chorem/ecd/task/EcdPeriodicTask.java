package org.chorem.ecd.task;

import org.chorem.ecd.service.JsonService;
import org.chorem.ecd.EcdConfig;

import java.util.concurrent.TimeUnit;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public abstract class EcdPeriodicTask implements Runnable {

    protected EcdConfig config;

    protected JsonService jsonService;

    public abstract String getName();

    public abstract Integer getPeriodicity();

    public abstract TimeUnit getPeriodicityUnit();

    public void setConfig(EcdConfig config) {
        this.config = config;
    }

    public void setJsonService(JsonService jsonService) {
        this.jsonService = jsonService;
    }

}
