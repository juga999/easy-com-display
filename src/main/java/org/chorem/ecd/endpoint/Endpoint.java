package org.chorem.ecd.endpoint;

import org.apache.commons.io.IOUtils;
import org.jboss.weld.util.collections.ImmutableMap;
import org.chorem.ecd.EcdConfig;
import org.chorem.ecd.service.AuthService;
import org.chorem.ecd.service.JsonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public abstract class Endpoint {

    private static final Logger logger = LoggerFactory.getLogger(Endpoint.class);

    protected static final Map<String, String> SUCCESS = ImmutableMap.of("result", "ok");

    @Inject
    protected EcdConfig config;

    @Inject
    protected AuthService authService;

    @Inject
    protected JsonService jsonService;

    @SuppressWarnings("unused")
    void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        init();
    }

    protected abstract void init();

    protected String ecdApiPath(String path) {
        return config.getApiPathPrefix() + path;
    }

    protected String ecdMediaPath(String path) {
        return config.getMediaPathPrefix() + path;
    }

    protected Response withJsonResponseType(Response resp) {
        resp.type("application/json");
        return resp;
    }

    protected void registerJsonProducer(String path, Route route) {
        Spark.get(path, (Request req, Response resp) ->
                        route.handle(req, withJsonResponseType(resp)),
                jsonService);
    }

    protected void registerJsonConsumer(String path, Route route) {
        Spark.post(path, (Request req, Response resp) ->
                        route.handle(req, withJsonResponseType(resp)),
                jsonService);
    }

    protected void registerJsonDestructor(String path, Route route) {
        Spark.delete(path, (Request req, Response resp) ->
                        route.handle(req, withJsonResponseType(resp)),
                jsonService);
    }

    protected void registerFormDataConsumer(String path, Route route) {
        Spark.post(path, "multipart/form-data", (Request req, Response resp) ->
                        route.handle(req, withJsonResponseType(resp)),
                jsonService);
    }

    protected <T> T getObject(Request req, Class<T> klass) {
        T obj = jsonService.getGson().fromJson(req.body(), klass);
        return obj;
    }

    protected Map<String, Part> getPartsMap(Request req) throws Exception {
        Map<String, Part> partsMap = req.attribute(Endpoint.class.getName());
        if (partsMap == null) {
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement(config.getUploadDir());
            req.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

            partsMap = req.raw().getParts().stream()
                    .collect(Collectors.toMap(Part::getName, Function.identity()));

            req.attribute(Endpoint.class.getName(), partsMap);
        }

        return partsMap;
    }

    protected String getFormDataStringOrNull(Request req, String field) throws Exception {
        String value = Optional.ofNullable(getPartsMap(req).get(field))
                .map(this::getStringOrNull)
                .orElse(null);
        return value;
    }

    protected Integer getFormDataIntegerOrNull(Request req, String field) throws Exception {
        Integer value = Optional.ofNullable(getPartsMap(req).get(field))
                .map(this::getIntegerOrNull)
                .orElse(null);
        return value;
    }

    protected InputStream getInputStreamOrNull(Part part) {
        InputStream inputStream = null;
        if (part != null) {
            try {
                inputStream = part.getInputStream();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return inputStream;
    }

    private String getStringOrNull(Part part) {
        String value = null;
        if (part != null) {
            try {
                InputStream inputStream = part.getInputStream();
                value = IOUtils.toString(inputStream, "UTF-8");
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return value;
    }

    private Integer getIntegerOrNull(Part part) {
        return Optional.ofNullable(getStringOrNull(part))
                .map(Integer::parseInt)
                .orElse(null);
    }

    private <T> T getObjectOrNull(Part part, Class<T> classOf) {
        T obj = null;
        try {
            InputStream inputStream = part.getInputStream();
            String json = IOUtils.toString(inputStream, "UTF-8");
            obj = jsonService.getGson().fromJson(json, classOf);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return obj;
    }

}
