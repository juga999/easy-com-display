package org.chorem.ecd.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Function;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public abstract class AbstractDao<T, K> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);

    protected final Class<T> entityType;

    protected final Table<?> table;

    protected final Function<K, Condition> keyComparator;

    protected Gson gson = new GsonBuilder().create();

    @Inject
    protected EcdDataSource dataSource;

    public AbstractDao(Class<T> entityType, Table<?> table, Function<K, Condition> keyComparator) {
        this.entityType = entityType;
        this.table = table;
        this.keyComparator = keyComparator;
    }

    public T get(K id) {
        T entity;
        DSLContext create = dataSource.getSqlContext();
        RecordMapper<Record, T> recordMapper = getRecordMapper();
        if (recordMapper != null) {
            entity = create.select().from(table).where(keyComparator.apply(id)).fetchOne(recordMapper);
        } else {
            entity = create.select().from(table).where(keyComparator.apply(id)).fetchOneInto(entityType);
        }
        return entity;
    }

    public List<T> findAll() {
        List<T> entities;
        DSLContext create = dataSource.getSqlContext();
        RecordMapper<Record, T> recordMapper = getRecordMapper();
        if (recordMapper != null) {
            entities = create.select().from(table).fetch(recordMapper);
        } else {
            entities = create.select().from(table).fetchInto(entityType);
        }
        return entities;
    }

    public void delete(K id) {
        DSLContext create = dataSource.getSqlContext();
        create.deleteFrom(table).where(keyComparator.apply(id)).execute();
    }

    protected RecordMapper<Record, T> getRecordMapper() {
        return null;
    }

}
