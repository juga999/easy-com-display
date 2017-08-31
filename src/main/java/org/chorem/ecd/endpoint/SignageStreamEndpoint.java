package org.chorem.ecd.endpoint;

import org.apache.commons.io.IOUtils;
import org.jboss.weld.util.collections.ImmutableMap;
import org.chorem.ecd.EcdConfig;
import org.chorem.ecd.service.CmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Part;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class SignageStreamEndpoint extends Endpoint {

    private static final Logger logger = LoggerFactory.getLogger(EcdConfig.class);

    @Inject
    protected CmsService cmsService;

    @Override
    protected void init() {
        registerJsonProducer(ecdApiPath("/cms/streams"), this::getSignageStreams);
        registerJsonProducer(ecdApiPath("/cms/streams/:id"), this::getSignageStream);
        Spark.delete(ecdApiPath("/cms/streams/:id"), this::deleteSignageStream, jsonService);
        registerFormDataConsumer(ecdApiPath("/cms/streams"), this::addSignageStream);
        registerFormDataConsumer(ecdApiPath("/cms/streams/timing"), this::setStreamTiming);
        // Renvoit directement une image
        Spark.get(ecdMediaPath("/streams/:id/:resource"), this::getSignageStreamResource);
    }

    private Object getSignageStreams(Request req, Response resp) {
        return cmsService.getSignageStreams();
    }

    private Object getSignageStream(Request req, Response resp) {
        UUID id = UUID.fromString(req.params("id"));
        return cmsService.getSignageStream(id);
    }

    private Object deleteSignageStream(Request req, Response resp) {
        UUID id = UUID.fromString(req.params("id"));
        cmsService.deleteSignageStream(id);
        withJsonResponseType(resp);
        return SUCCESS;
    }

    private Object addSignageStream(Request req, Response resp) throws Exception {
        Map<String, Part> partsMap = getPartsMap(req);

        Part filePart = partsMap.get("presentationFile");
        InputStream inputStream = getInputStreamOrNull(filePart);
        String fileName = Optional.ofNullable(filePart).map(Part::getSubmittedFileName).orElse(null);
        String streamName = getFormDataStringOrNull(req, "streamName");

        UUID streamId = cmsService.importSignageStreamFromPresentation(inputStream, fileName, streamName);

        return ImmutableMap.of("streamId", streamId);
    }

    private Object setStreamTiming(Request req, Response resp) throws Exception {
        UUID streamId = Optional.ofNullable(getFormDataStringOrNull(req, "streamId"))
                .map(UUID::fromString)
                .orElse(null);
        Integer timing = getFormDataIntegerOrNull(req, "timing");
        cmsService.setStreamTiming(streamId, timing);

        return SUCCESS;
    }

    private Object getSignageStreamResource(Request req, Response resp) throws Exception {
        UUID id = UUID.fromString(req.params("id"));
        String resource = req.params("resource");
        File resourceFile = cmsService.getSignageStreamResourceFile(id, resource);
        String contentType = URLConnection.guessContentTypeFromName(resourceFile.getName());
        resp.type(contentType);
        resp.raw().setContentLength((int)resourceFile.length());
        resp.status(HttpURLConnection.HTTP_OK);

        try(ServletOutputStream os = resp.raw().getOutputStream()) {
            try (FileInputStream in = new FileInputStream(resourceFile)) {
                IOUtils.copy(in, os);
                in.close();
            }
            os.close();
        }

        return new Object[0];
    }
}
