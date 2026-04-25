package com.sigafeliz.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {

    private static final String URL  = "jdbc:postgresql://localhost:5432/sigafeliz";
    private static final String USER = "postgres";
    private static final String PASS = "sua_senha";

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}