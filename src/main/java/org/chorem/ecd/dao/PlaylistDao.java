package org.chorem.ecd.dao;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import org.chorem.ecd.model.Playlist;
import org.chorem.ecd.model.news.NewsFeed;
import org.chorem.ecd.model.stream.SignageStream;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.util.postgres.PostgresDSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import static org.chorem.ecd.mapping.tables.Playlist.PLAYLIST;
import static org.chorem.ecd.mapping.tables.SignageStream.SIGNAGE_STREAM;
import static org.chorem.ecd.mapping.tables.NewsFeed.NEWS_FEED;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class PlaylistDao extends AbstractDao<Playlist, UUID> {

    private static final Logger logger = LoggerFactory.getLogger(PlaylistDao.class);

    private final Type streamsListType = new TypeToken<List<SignageStream>>(){}.getType();
    private final Type newsFeedsListType = new TypeToken<List<NewsFeed>>(){}.getType();

    public PlaylistDao() {
        super(Playlist.class, PLAYLIST, PLAYLIST.ID::eq);
    }

    public Playlist getPlaylist() {
        Playlist playlist = new Playlist();
        DSLContext create = dataSource.getSqlContext();
        UUID playlistId = create.select(PLAYLIST.ID).from(PLAYLIST).fetchOneInto(UUID.class);
        if (playlistId != null) {
            playlist.setId(playlistId);
            playlist.setStreams(getPlaylistStreams());
            playlist.setNewsFeeds(getPlaylistNewsFeeds());
        } else {
            UUID newPlaylistId = UUID.randomUUID();
            create.insertInto(PLAYLIST, PLAYLIST.ID, PLAYLIST.STREAM_IDS, PLAYLIST.NEWS_FEED_IDS)
                    .values(newPlaylistId, new UUID[]{}, new UUID[]{})
                    .execute();

            playlist.setId(newPlaylistId);
        }
        return playlist;
    }

    public void addStreamId(Playlist playlist, UUID streamId) {
        Preconditions.checkNotNull(playlist.getId());
        DSLContext create = dataSource.getSqlContext();
        create.update(PLAYLIST)
                .set(PLAYLIST.STREAM_IDS, PostgresDSL.arrayAppend(PLAYLIST.STREAM_IDS, streamId))
                .execute();
        playlist.setStreams(getPlaylistStreams());
    }

    public void removeStreamId(Playlist playlist, UUID streamId) {
        Preconditions.checkNotNull(playlist.getId());
        DSLContext create = dataSource.getSqlContext();
        create.update(PLAYLIST)
                .set(PLAYLIST.STREAM_IDS, PostgresDSL.arrayRemove(PLAYLIST.STREAM_IDS, streamId))
                .execute();
        playlist.setStreams(getPlaylistStreams());
    }

    public void addNewsFeedId(Playlist playlist, UUID newsFeedId) {
        Preconditions.checkNotNull(playlist.getId());
        DSLContext create = dataSource.getSqlContext();
        create.update(PLAYLIST)
                .set(PLAYLIST.NEWS_FEED_IDS, PostgresDSL.arrayAppend(PLAYLIST.NEWS_FEED_IDS, newsFeedId))
                .execute();
        playlist.setNewsFeeds(getPlaylistNewsFeeds());
    }

    public void removeNewsFeedId(Playlist playlist, UUID newsFeedId) {
        Preconditions.checkNotNull(playlist.getId());
        DSLContext create = dataSource.getSqlContext();
        create.update(PLAYLIST)
                .set(PLAYLIST.NEWS_FEED_IDS, PostgresDSL.arrayRemove(PLAYLIST.NEWS_FEED_IDS, newsFeedId))
                .execute();
        playlist.setNewsFeeds(getPlaylistNewsFeeds());
    }

    @Override
    protected RecordMapper<Record, Playlist> getRecordMapper() {
        throw new UnsupportedOperationException();
    }

    private List<SignageStream> getPlaylistStreams() {
        DSLContext create = dataSource.getSqlContext();
        Table<?> streamIdTable = DSL.unnest(PLAYLIST.STREAM_IDS).as("unnested_stream_id");
        List<SignageStream> streams = create.select(DSL.field("json_agg(s.*)").as("streams_json"))
                .from(PLAYLIST, streamIdTable)
                .join(SIGNAGE_STREAM.as("s")).on(DSL.field("s.id", UUID.class).eq(DSL.field("unnested_stream_id", UUID.class)))
                .fetchOptional("streams_json", String.class)
                .map(s -> gson.<List<SignageStream>>fromJson(s, streamsListType))
                .orElse(Lists.newArrayList());
        return streams;
    }

    private List<NewsFeed> getPlaylistNewsFeeds() {
        DSLContext create = dataSource.getSqlContext();
        Table<?> newsFeedIdTable = DSL.unnest(PLAYLIST.NEWS_FEED_IDS).as("unnested_news_feed_id");
        List<NewsFeed> newsFeeds = create.select(DSL.field("json_agg(n.*)").as("news_feeds_json"))
                .from(PLAYLIST, newsFeedIdTable)
                .join(NEWS_FEED.as("n")).on(DSL.field("n.id", UUID.class).eq(DSL.field("unnested_news_feed_id", UUID.class)))
                .fetchOptional("news_feeds_json", String.class)
                .map(s -> gson.<List<NewsFeed>>fromJson(s, newsFeedsListType))
                .orElse(Lists.newArrayList());
        return newsFeeds;
    }
}
