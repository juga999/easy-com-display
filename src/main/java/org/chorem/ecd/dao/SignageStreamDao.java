package org.chorem.ecd.dao;

import com.google.gson.reflect.TypeToken;
import org.chorem.ecd.model.stream.SignageStream;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.chorem.ecd.model.stream.SignageStreamFrame;
import org.jooq.impl.DSL;
import org.jooq.util.postgres.PostgresDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.chorem.ecd.mapping.tables.SignageStream.SIGNAGE_STREAM;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class SignageStreamDao extends AbstractDao<SignageStream, UUID> {

    private static final Logger logger = LoggerFactory.getLogger(SignageStreamDao.class);

    private final Type framesListType = new TypeToken<List<SignageStreamFrame>>(){}.getType();

    public SignageStreamDao() {
        super(SignageStream.class, SIGNAGE_STREAM, SIGNAGE_STREAM.ID::eq);
    }

    public void addSignageStream(SignageStream signageStream) {
        DSLContext create = dataSource.getSqlContext();
        Timestamp now = Timestamp.from(Instant.now());
        create.insertInto(SIGNAGE_STREAM, SIGNAGE_STREAM.ID, SIGNAGE_STREAM.NAME, SIGNAGE_STREAM.TIMING, SIGNAGE_STREAM.CREATION_DATE_TIME, SIGNAGE_STREAM.LAST_UPDATE_DATE_TIME, SIGNAGE_STREAM.FRAMES)
                .values(signageStream.getId(), signageStream.getName(), signageStream.getTiming(), now, now, DSL.cast(gson.toJson(signageStream.getFrames()), PostgresDataType.JSON))
                .execute();
    }

    public void setSignageStreamTiming(UUID streamId, Integer timing) {
        DSLContext create = dataSource.getSqlContext();
        create.update(SIGNAGE_STREAM)
                .set(SIGNAGE_STREAM.TIMING, timing)
                .where(SIGNAGE_STREAM.ID.eq(streamId))
                .execute();
    }

    public void setSignageStreamFrames(UUID streamId, List<SignageStreamFrame> frames) {
        DSLContext create = dataSource.getSqlContext();
        create.update(SIGNAGE_STREAM)
                .set(SIGNAGE_STREAM.FRAMES, (Object)DSL.cast(gson.toJson(frames), PostgresDataType.JSON))
                .where(SIGNAGE_STREAM.ID.eq(streamId))
                .execute();
    }

    @Override
    protected RecordMapper<Record, SignageStream> getRecordMapper() {
        return this::fromRecord;
    }

    protected SignageStream fromRecord(Record record) {
        SignageStream signageStream = new SignageStream();

        signageStream.setId(record.get(SIGNAGE_STREAM.ID));
        signageStream.setName(record.get(SIGNAGE_STREAM.NAME));
        signageStream.setTiming(record.get(SIGNAGE_STREAM.TIMING));

        Timestamp timestamp;
        timestamp = record.get(SIGNAGE_STREAM.CREATION_DATE_TIME);
        signageStream.setCreationDateTime(timestamp.toLocalDateTime());
        timestamp = record.get(SIGNAGE_STREAM.LAST_UPDATE_DATE_TIME);
        signageStream.setLastUpdateDateTime(timestamp.toLocalDateTime());

        String framesJson = record.get(SIGNAGE_STREAM.FRAMES).toString();
        signageStream.setFrames(gson.fromJson(framesJson, framesListType));

        return signageStream;
    }
}
