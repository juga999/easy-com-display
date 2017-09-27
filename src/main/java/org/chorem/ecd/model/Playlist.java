package org.chorem.ecd.model;

import com.google.common.collect.Lists;
import org.chorem.ecd.model.news.NewsFeed;
import org.chorem.ecd.model.stream.SignageStream;

import java.util.List;
import java.util.UUID;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public class Playlist {

    private UUID id;

    private List<SignageStream> streams = Lists.newArrayList();

    private List<NewsFeed> newsFeeds = Lists.newArrayList();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<SignageStream> getStreams() {
        return streams;
    }

    public void setStreams(List<SignageStream> streams) {
        this.streams = streams;
    }

    public List<NewsFeed> getNewsFeeds() {
        return newsFeeds;
    }

    public void setNewsFeeds(List<NewsFeed> newsFeeds) {
        this.newsFeeds = newsFeeds;
    }

}
