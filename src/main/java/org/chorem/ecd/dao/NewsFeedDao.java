package org.chorem.ecd.dao;

import org.chorem.ecd.mapping.tables.records.NewsFeedRecord;
import org.chorem.ecd.model.news.NewsFeed;
import org.jooq.DSLContext;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

import static org.chorem.ecd.mapping.tables.NewsFeed.NEWS_FEED;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class NewsFeedDao extends AbstractDao<NewsFeed, UUID> {

    public NewsFeedDao() {
        super(NewsFeed.class, NEWS_FEED, NEWS_FEED.ID::eq);
    }

    public void addNewsFeed(NewsFeed newsFeed) {
        DSLContext create = dataSource.getSqlContext();
        NewsFeedRecord record = create.newRecord(NEWS_FEED);
        record.setId(newsFeed.getId());
        record.setName(newsFeed.getName());
        record.setUrl(newsFeed.getUrl());
        record.store();
    }

}
