package com.eaglefatec.sigafeliz.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages the PostgreSQL database connection and schema initialization.
 * Connection parameters are read from environment variables for Docker compatibility.
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    private DatabaseManager() {
        String host = System.getenv().getOrDefault("DB_HOST", "localhost");
        String port = System.getenv().getOrDefault("DB_PORT", "5432");
        String name = System.getenv().getOrDefault("DB_NAME", "sigafeliz");
        this.dbUser = System.getenv().getOrDefault("DB_USER", "sigafeliz");
        this.dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "sigafeliz");
        this.dbUrl = "jdbc:postgresql://" + host + ":" + port + "/" + name;
        initSchema();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    private void initSchema() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS semesters (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(50) NOT NULL,
                            start_date VARCHAR(10) NOT NULL,
                            end_date VARCHAR(10) NOT NULL,
                            kickoff_date VARCHAR(10)
                        )
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS blocked_days (
                            id SERIAL PRIMARY KEY,
                            semester_id INTEGER NOT NULL REFERENCES semesters(id) ON DELETE CASCADE,
                            blocked_date VARCHAR(10) NOT NULL,
                            description VARCHAR(255),
                            day_type VARCHAR(30) NOT NULL
                        )
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS planning_units (
                            id SERIAL PRIMARY KEY,
                            semester_id INTEGER NOT NULL REFERENCES semesters(id),
                            subject_name VARCHAR(150) NOT NULL,
                            workload INTEGER NOT NULL,
                            weekly_schedule VARCHAR(500) NOT NULL,
                            created_at VARCHAR(30) NOT NULL
                        )
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS temas (
                            id SERIAL PRIMARY KEY,
                            planning_unit_id INTEGER NOT NULL REFERENCES planning_units(id) ON DELETE CASCADE,
                            title VARCHAR(200) NOT NULL,
                            min_aulas INTEGER NOT NULL,
                            max_aulas INTEGER NOT NULL,
                            priority VARCHAR(10) NOT NULL,
                            is_evaluation INTEGER NOT NULL DEFAULT 0
                        )
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS generated_schedules (
                            id SERIAL PRIMARY KEY,
                            planning_unit_id INTEGER NOT NULL REFERENCES planning_units(id),
                            generated_at VARCHAR(30) NOT NULL,
                            file_path VARCHAR(500) NOT NULL,
                            subject_name VARCHAR(150) NOT NULL,
                            semester_name VARCHAR(50) NOT NULL,
                            workload INTEGER NOT NULL DEFAULT 40
                        )
                    """);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database schema. "
                    + "Ensure PostgreSQL is running and accessible at: " + dbUrl, e);
        }
    }
}
