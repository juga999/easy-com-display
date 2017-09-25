package org.chorem.ecd.endpoint;

import com.google.common.base.Verify;
import com.google.common.collect.ImmutableMap;
import org.chorem.ecd.model.news.NewsFeed;
import org.chorem.ecd.service.CmsService;
import spark.Request;
import spark.Response;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
        registerJsonConsumer(ecdApiPath("/cms/newsfeeds"), this::addNewsFeed);
        registerJsonDestructor(ecdApiPath("/cms/newsfeeds/:id"), this::deleteNewsFeed);
    }

    private Object getNews(Request req, Response resp) {
        return cmsService.getAggregatedNews();
    }

    private Object getNewsFeeds(Request req, Response resp) {
        return cmsService.getNewsFeeds();
    }

    private Object addNewsFeed(Request req, Response resp) throws Exception {
        NewsFeed newsFeed = getObject(req, NewsFeed.class);
        Verify.verifyNotNull(newsFeed);

        UUID newsFeedId = cmsService.addNewsFeed(newsFeed);

        return ImmutableMap.of("newsFeedId", newsFeedId);
    }

    private Object deleteNewsFeed(Request req, Response resp) {
        UUID id = UUID.fromString(req.params("id"));
        cmsService.deleteNewsFeed(id);

        return SUCCESS;
    }
}
