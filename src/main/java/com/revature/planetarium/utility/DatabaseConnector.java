package com.revature.planetarium.utility;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sqlite.SQLiteConfig;

public class DatabaseConnector {

    public static Connection getConnection() throws SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src/test/resources/config.json"));
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            String host = jsonNode.get("host").asText();
            String port = jsonNode.get("port").asText();
            String database = jsonNode.get("database").asText();
            String user = jsonNode.get("user").asText();
            String password = jsonNode.get("password").asText();
            String url = host + ":" + port + "/" + database;
            Properties props = config.toProperties();
            props.setProperty("user", user);
            props.setProperty("password", password);
            return DriverManager.getConnection(url, props);
        } catch (IOException e) {
            throw new SQLException("Failed to read config file");
        }
    }

}
