package org.chorem.ecd.endpoint;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.chorem.ecd.service.MediaConverterService;
import spark.Request;
import spark.Response;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class DebugEndpoint extends Endpoint {

    @Inject
    private MediaConverterService mediaConverterService;

    @Override
    protected void init() {
        registerJsonProducer(ecdApiPath("/debug/ping/:name"), this::ping);
        registerJsonProducer(ecdApiPath("/debug/check-install"), this::checkInstallation);
    }

    public Object ping(Request req, Response resp) {
        String user = req.attribute("user");
        if (user != null) {
            return ImmutableMap.of("message", "Hello authenticated " + req.params("name"));
        } else {
            return ImmutableMap.of("message", "Hello " + req.params("name"));
        }
    }

    public Object checkInstallation(Request req, Response resp) {
        Map<String, String> result = Maps.newHashMap();

        result.put("dataDir", config.getDataDir());
        result.put("unoconv", mediaConverterService.getUnoconvVersion());
        result.put("convert", mediaConverterService.getConvertVersion());

        return result;
    }
}
