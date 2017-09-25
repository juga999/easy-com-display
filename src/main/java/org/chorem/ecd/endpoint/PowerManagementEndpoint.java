package org.chorem.ecd.endpoint;

import org.chorem.ecd.service.PowerManagementService;
import org.chorem.ecd.model.settings.TvTimes;
import spark.Request;
import spark.Response;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class PowerManagementEndpoint extends Endpoint {

    @Inject
    protected PowerManagementService powerManagementService;

    @Override
    protected void init() {
        registerJsonProducer(ecdApiPath("/settings/tv-times"), this::getTvTimes);
        registerJsonConsumer(ecdApiPath("/settings/tv-times"), this::setTvTimes);
    }

    private Object getTvTimes(Request req, Response resp) {
        return powerManagementService.getTvTimes();
    }

    private Object setTvTimes(Request req, Response resp) throws Exception {
        TvTimes tvTimes = getObject(req, TvTimes.class);
        powerManagementService.setTvTimes(tvTimes);
        return SUCCESS;
    }

}
