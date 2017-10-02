package org.chorem.ecd;

import org.flywaydb.core.Flyway;
import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.Configuration;
import org.jooq.util.jaxb.Database;
import org.jooq.util.jaxb.Generator;
import org.jooq.util.jaxb.Jdbc;
import org.jooq.util.jaxb.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import static ru.yandex.qatools.embed.postgresql.distribution.Version.V9_6_3;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public class Generate {

    private static final Logger logger = LoggerFactory.getLogger(Generate.class);

    private static final String DB_NAME = "ecd";
    private static final String USER = "ecd";
    private static final String PWD = "ecd";
    private static final String SCHEMA = "ecd_app";

    public static void main(String[] args) {
        logger.info("Generating classes");

        final EmbeddedPostgres postgres = new EmbeddedPostgres(V9_6_3);

        try {
            String url = postgres.start("localhost", 55432, DB_NAME, USER, PWD);

            Flyway flyway = new Flyway();
            flyway.setDataSource(url, USER, PWD);
            flyway.setSchemas(SCHEMA);
            flyway.migrate();

            Configuration configuration = new Configuration()
                    .withJdbc(new Jdbc()
                        .withDriver("org.postgresql.Driver")
                        .withUrl(url))
                    .withGenerator(new Generator()
                        .withDatabase(new Database()
                            .withName("org.jooq.util.postgres.PostgresDatabase")
                            .withIncludes(".*")
                            .withExcludes("")
                            .withInputSchema(SCHEMA))
                    .withTarget(new Target()
                        .withPackageName("org.chorem.ecd.mapping")
                        .withDirectory("target/generated-sources/jooq")));
            GenerationTool.generate(configuration);

            logger.info("Done");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            postgres.stop();
        }
    }

}
