package org.test.project.infra.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@RequiredArgsConstructor
public class DataSourceConfig {

//    private final static String JDBC_URL = "jdbc:mysql://localhost:3306/telecom?createDatabaseIfNotExist=true";
//    private final static String USER_NAME = "root";
//    private final static String PASSWORD = "380500850614Lucky";
    private final ConfigLoader configLoader;

    public DataSource configureDataSource() {
        configLoader.loadConfig("db/db.properties");
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(configLoader.getJdbcUrl());
        hikariConfig.setUsername(configLoader.getUserName());
        hikariConfig.setPassword(configLoader.getUserPassword());
        return new HikariDataSource(hikariConfig);
    }

}

