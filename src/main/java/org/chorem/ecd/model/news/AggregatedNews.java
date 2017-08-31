package org.chorem.ecd.model.news;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public class AggregatedNews {

    private Map<String, List<String>> newsPerFeed = Maps.newHashMap();

    public void addNews(String newsFeedTitle, List<String> newsFeedNews) {
        newsPerFeed.put(newsFeedTitle, newsFeedNews);
    }

}
