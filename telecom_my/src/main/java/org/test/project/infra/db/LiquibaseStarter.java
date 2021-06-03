package org.test.project.infra.db;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;

@RequiredArgsConstructor
public class LiquibaseStarter {

    private static Logger logger = LogManager.getLogger(LiquibaseStarter.class);

    private final DataSource dataSource;
    private final static String CHANGE_LOG_FILE = "/db/liquibase/db-changelog-master.xml";

    @SneakyThrows
    public void updateDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new liquibase.Liquibase(CHANGE_LOG_FILE, new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());

            logger.info("Update database");
        }
    }
}