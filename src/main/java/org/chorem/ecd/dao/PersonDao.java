package org.chorem.ecd.dao;

import org.chorem.ecd.model.Person;
import org.jooq.DSLContext;
import org.chorem.ecd.mapping.tables.records.PersonRecord;

import javax.enterprise.context.ApplicationScoped;

import static org.chorem.ecd.mapping.tables.Person.PERSON;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class PersonDao extends AbstractDao<org.chorem.ecd.model.Person, Integer> {

    public PersonDao() {
        super(Person.class, PERSON, PERSON.ID::eq);
    }

    public void addPerson(org.chorem.ecd.model.Person person) {
        DSLContext create = dataSource.getSqlContext();
        PersonRecord personRecord = create.newRecord(PERSON);
        personRecord.setName(person.getName());
        personRecord.store();
    }
}
