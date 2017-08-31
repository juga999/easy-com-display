package org.chorem.ecd.endpoint;

import com.google.common.collect.ImmutableMap;
import org.chorem.ecd.service.CmsService;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.Part;
import java.util.Map;
import java.util.UUID;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class NewsFeedEndpoint extends Endpoint {

    @Inject
    protected CmsService cmsService;

    @Override
    protected void init() {
        registerJsonProducer(ecdApiPath("/cms/news"), this::getNews);
        registerJsonProducer(ecdApiPath("/cms/newsfeeds"), this::getNewsFeeds);
        registerFormDataConsumer(ecdApiPath("/cms/newsfeeds"), this::addNewsFeed);
        Spark.delete(ecdApiPath("/cms/newsfeeds/:id"), this::deleteNewsFeed, jsonService);
    }

    private Object getNews(Request req, Response resp) {
        return cmsService.getAggregatedNews();
    }

    private Object getNewsFeeds(Request req, Response resp) {
        return cmsService.getNewsFeeds();
    }

    private Object addNewsFeed(Request req, Response resp) throws Exception {
        Map<String, Part> partsMap = getPartsMap(req);

        String newsFeedName = getFormDataStringOrNull(req, "newsFeedName");
        String newsFeedUrl = getFormDataStringOrNull(req, "newsFeedUrl");

        UUID newsFeedId = cmsService.addNewsFeed(newsFeedName, newsFeedUrl);

        return ImmutableMap.of("newsFeedId", newsFeedId);
    }

    private Object deleteNewsFeed(Request req, Response resp) {
        UUID id = UUID.fromString(req.params("id"));
        cmsService.deleteNewsFeed(id);
        withJsonResponseType(resp);
        return SUCCESS;
    }
}
