package org.test.project.infra.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;

@RequiredArgsConstructor
public class DataSourceConfig {

    public DataSource configureDataSource() {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/telecom?createDatabaseIfNotExist=true");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("380500850614Lucky");
        return new HikariDataSource(hikariConfig);
    }

}

