package org.chorem.ecd.service;

import com.google.common.collect.Maps;
import org.chorem.ecd.task.EcdPeriodicTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@Singleton
public class PeriodicTasksExecutorService {

    private static final Logger logger = LoggerFactory.getLogger(PeriodicTasksExecutorService.class);

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);

    private Map<String, ScheduledFuture<?>> taskFuturesMap = Maps.newConcurrentMap();

    @PostConstruct
    public void init() {
    }

    @PreDestroy
    public void shutdown() {
        taskFuturesMap.forEach((name, future) -> {
            future.cancel(false);
            logger.info("Cancelled " + name);
        });
        taskFuturesMap.clear();
    }

    public void schedule(EcdPeriodicTask task) {
        ScheduledFuture<?> future;
        if (taskFuturesMap.containsKey(task.getName())) {
            future = taskFuturesMap.get(task.getName());
            future.cancel(false);
            taskFuturesMap.remove(task.getName());
        }
        future = executor.scheduleWithFixedDelay(task, 0, task.getPeriodicity(), task.getPeriodicityUnit());
        taskFuturesMap.put(task.getName(), future);
        logger.info("Scheduled " + task.getName());
    }
}
