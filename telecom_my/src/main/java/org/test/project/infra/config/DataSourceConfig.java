package org.test.project.infra.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;

@RequiredArgsConstructor
public class DataSourceConfig {

    private final static String JDBC_URL = "jdbc:mysql://localhost:3306/telecom?createDatabaseIfNotExist=true";
    private final static String USER_NAME = "root";
    private final static String PASSWORD = "380500850614Lucky";

    public DataSource configureDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(JDBC_URL);
        hikariConfig.setUsername(USER_NAME);
        hikariConfig.setPassword(PASSWORD);
        return new HikariDataSource(hikariConfig);
    }

}

