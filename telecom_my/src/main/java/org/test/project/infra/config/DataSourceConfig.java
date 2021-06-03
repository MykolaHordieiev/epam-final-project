package org.test.project.infra.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@RequiredArgsConstructor
public class DataSourceConfig {

    private static Logger logger = LogManager.getLogger(DataSourceConfig.class);

    private final ConfigLoader configLoader;

    public DataSource configureDataSource() {
        logger.info("Start config DataSource");

        configLoader.loadConfig("db/db.properties");
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(configLoader.getJdbcUrl());
        logger.info("Set JdbcUrl --> " + configLoader.getJdbcUrl());

        hikariConfig.setUsername(configLoader.getUserName());
        logger.info("Set userName --> " + configLoader.getUserName());

        hikariConfig.setPassword(configLoader.getUserPassword());
        logger.info("Set userPassword --> " + configLoader.getUserPassword());

        return new HikariDataSource(hikariConfig);
    }

}

