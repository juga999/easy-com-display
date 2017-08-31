package org.chorem.ecd.dao;

import org.chorem.ecd.model.settings.Location;
import org.chorem.ecd.model.settings.Settings;
import org.chorem.ecd.model.settings.TvTimes;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;

import javax.enterprise.context.ApplicationScoped;

import java.util.Optional;

import static org.chorem.ecd.mapping.tables.Settings.SETTINGS;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class SettingsDao extends AbstractDao<Settings, Integer> {

    public SettingsDao() {
        super(Settings.class, SETTINGS, SETTINGS.ID::eq);
    }

    public TvTimes getTvTimes() {
        DSLContext create = dataSource.getSqlContext();
        TvTimes result = create.select(SETTINGS.TV_TIMES).from(SETTINGS).where(SETTINGS.ID.eq(1))
                .fetchOptionalInto(String.class)
                .map(json -> gson.fromJson(json, TvTimes.class))
                .orElse(new TvTimes());
        return result;
    }

    public Location getLocation() {
        DSLContext create = dataSource.getSqlContext();
        Location result = create.select(SETTINGS.LOCATION).from(SETTINGS).where(SETTINGS.ID.eq(1))
                .fetchOptionalInto(String.class)
                .map(json -> gson.fromJson(json, Location.class))
                .orElse(new Location());
        return result;
    }

    public void setTvTimes(TvTimes tvTimes) {
        String tvTimesJson = gson.toJson(tvTimes);
        DSLContext create = dataSource.getSqlContext();
        create.insertInto(SETTINGS, SETTINGS.ID, SETTINGS.TV_TIMES)
                .values(1, tvTimesJson)
                .onDuplicateKeyUpdate()
                .set(SETTINGS.TV_TIMES, tvTimesJson)
                .execute();
    }

    public void setLocation(Location location) {
        String locationJson = gson.toJson(location);
        DSLContext create = dataSource.getSqlContext();
        create.insertInto(SETTINGS, SETTINGS.ID, SETTINGS.LOCATION)
                .values(1, locationJson)
                .onDuplicateKeyUpdate()
                .set(SETTINGS.LOCATION, locationJson)
                .execute();
    }

    @Override
    protected RecordMapper<Record, Settings> getRecordMapper() {
        return this::fromRecord;
    }

    protected Settings fromRecord(Record record) {
        Settings settings = new Settings();

        Location location = Optional.ofNullable(record.get(SETTINGS.LOCATION))
                .map(json -> gson.fromJson(json, Location.class))
                .orElse(null);
        settings.setLocation(location);

        TvTimes tvTimes = Optional.ofNullable(record.get(SETTINGS.TV_TIMES))
                .map(json -> gson.fromJson(json, TvTimes.class))
                .orElse(null);
        settings.setTvTimes(tvTimes);

        return settings;
    }
}
