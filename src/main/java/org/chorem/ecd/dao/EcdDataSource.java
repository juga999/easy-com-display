package org.chorem.ecd.dao;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class EcdDataSource {

    private static final Logger logger = LoggerFactory.getLogger(EcdDataSource.class);

    private DataSource dataSource;

    private static final ThreadLocal<Configuration> threadTransactionConfiguration = new ThreadLocal<>();

    public EcdDataSource() {
        PoolProperties p = new PoolProperties();
        p.setDriverClassName("org.postgresql.Driver");
        p.setUrl("jdbc:postgresql:ecd");
        p.setUsername("ecd");
        p.setPassword("ecd");
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                        "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");

        dataSource = new DataSource();
        dataSource.setPoolProperties(p);
    }

    public Optional<Configuration> getTransactionConfiguration() {
        return Optional.ofNullable(threadTransactionConfiguration.get());
    }

    public void setTransactionConfiguration(Configuration configuration) {
        threadTransactionConfiguration.set(configuration);
    }

    public DSLContext getSqlContext() {
        return getTransactionConfiguration()
                .map(DSL::using)
                .orElse(DSL.using(dataSource, SQLDialect.POSTGRES_9_4));
    }
}
