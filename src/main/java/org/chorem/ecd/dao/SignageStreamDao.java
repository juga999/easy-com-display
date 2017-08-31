package org.chorem.ecd.dao;

import com.google.gson.reflect.TypeToken;
import org.chorem.ecd.model.stream.SignageStream;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.chorem.ecd.model.stream.SignageStreamFrame;
import org.chorem.ecd.mapping.tables.records.SignageStreamRecord;

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

    private final Type framesListType = new TypeToken<List<SignageStreamFrame>>(){}.getType();

    public SignageStreamDao() {
        super(SignageStream.class, SIGNAGE_STREAM, SIGNAGE_STREAM.ID::eq);
    }

    public void addSignageStream(SignageStream signageStream) {
        DSLContext create = dataSource.getSqlContext();
        SignageStreamRecord record = create.newRecord(SIGNAGE_STREAM);
        record.setId(signageStream.getId());
        record.setName(signageStream.getName());
        record.setTiming(signageStream.getTiming());
        Timestamp now = Timestamp.from(Instant.now());
        record.setCreationDateTime(now);
        record.setLastUpdateDateTime(now);
        record.setFrames(gson.toJson(signageStream.getFrames()));
        record.store();
    }

    public void setSignageStreamTiming(UUID streamId, Integer timing) {
        DSLContext create = dataSource.getSqlContext();
        create.update(SIGNAGE_STREAM)
                .set(SIGNAGE_STREAM.TIMING, timing)
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

        String framesJson = record.get(SIGNAGE_STREAM.FRAMES);
        signageStream.setFrames(gson.fromJson(framesJson, framesListType));

        return signageStream;
    }
}
