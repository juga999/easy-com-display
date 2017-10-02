package org.chorem.ecd;

import com.google.common.io.Files;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
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

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.List;

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
            File postgresRuntimeTempDir = Files.createTempDir();
            IRuntimeConfig postgresRuntimeConfig = EmbeddedPostgres.cachedRuntimeConfig(postgresRuntimeTempDir.toPath());
            int port = findFreePort();
            List<String> additionalParams = Arrays.asList("-E", "UTF-8", "--locale=C", "--lc-collate=C", "--lc-ctype=C");
            String url = postgres.start(postgresRuntimeConfig, "localhost", port, DB_NAME, USER, PWD, additionalParams);

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

    /**
     * Returns a free port number on localhost.
     * <p/>
     * Heavily inspired from org.eclipse.jdt.launching.SocketUtil (to avoid a dependency to JDT just because of this).
     * Slightly improved with close() missing in JDT. And throws exception instead of returning -1.
     *
     * @return a free port number on localhost
     * @throws IllegalStateException if unable to find a free port
     */
    private static int findFreePort() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            int port = socket.getLocalPort();
            try {
                socket.close();
            } catch (IOException ignored) {
                // Ignore IOException on close()
            }
            return port;
        } catch (IOException ignored) {
            // Ignore IOException on open
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                    // Ignore IOException on close()
                }
            }
        }
        throw new IllegalStateException("Could not find a free TCP/IP port");
    }
}
