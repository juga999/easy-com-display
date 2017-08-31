package org.chorem.ecd.model.news;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public class NewsFeed {

    private UUID id;

    private String name;

    private String url;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public URL getParsedUrl() {
        URL parsedUrl = null;
        try {
            parsedUrl = new URL(getUrl());
        } catch (MalformedURLException e) {
        }
        return parsedUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
