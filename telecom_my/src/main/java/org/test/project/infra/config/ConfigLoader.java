package org.test.project.infra.config;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;

import java.io.InputStream;
import java.util.Properties;

@Data
public class ConfigLoader {

    private static Logger logger = LogManager.getLogger(ConfigLoader.class);
    private String jdbcUrl;
    private String userName;
    private String userPassword;

    public void loadConfig(String configPath) {
        Properties property = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configPath);
            property.load(inputStream);
            this.jdbcUrl = property.getProperty("jdbcURL");
            this.userName = property.getProperty("userName");
            this.userPassword = property.getProperty("userPassword");
        } catch (Exception ex) {
            logger.error("Cannot load properties. ", ex);
        }
    }

}
