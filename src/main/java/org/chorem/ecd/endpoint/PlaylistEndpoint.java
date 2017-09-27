package org.chorem.ecd.endpoint;

import org.chorem.ecd.model.ModelId;
import org.chorem.ecd.model.Playlist;
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
public class PlaylistEndpoint extends Endpoint {

    @Inject
    protected CmsService cmsService;

    @Override
    protected void init() {
        registerJsonProducer(ecdApiPath("/playlist"), this::getPlaylist);
        registerJsonConsumer(ecdApiPath("/playlist/add-stream"), this::addStream);
        registerJsonConsumer(ecdApiPath("/playlist/remove-stream"), this::removeStream);
        registerJsonConsumer(ecdApiPath("/playlist/add-newsfeed"), this::addNewsFeed);
        registerJsonConsumer(ecdApiPath("/playlist/remove-newsfeed"), this::removeNewsFeed);
    }

    private Object getPlaylist(Request request, Response response) {
        return cmsService.getPlaylist();
    }

    private Object addStream(Request request, Response response) {
        UUID streamId = getObject(request, ModelId.class).getId();
        Playlist playlist = cmsService.addPlaylistStream(streamId);
        return playlist;
    }

    private Object removeStream(Request request, Response response) {
        UUID streamId = getObject(request, ModelId.class).getId();
        Playlist playlist = cmsService.removePlaylistStream(streamId);
        return playlist;
    }

    private Object addNewsFeed(Request request, Response response) {
        UUID newsFeedId = getObject(request, ModelId.class).getId();
        Playlist playlist = cmsService.addPlaylistNewsFeed(newsFeedId);
        return playlist;
    }

    private Object removeNewsFeed(Request request, Response response) {
        UUID newsFeedId = getObject(request, ModelId.class).getId();
        Playlist playlist = cmsService.removePlaylistNewsFeed(newsFeedId);
        return playlist;
    }
}
