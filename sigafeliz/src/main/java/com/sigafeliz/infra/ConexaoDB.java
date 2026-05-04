package com.sigafeliz.infra;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoDB {

    private static final Properties props = new Properties();

    static {
        try (InputStream is = ConexaoDB.class
                .getResourceAsStream("/secret/postgres.properties")) {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Arquivo db.properties não encontrado", e);
        }
    }

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.pass")
        );
    }
}