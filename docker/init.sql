-- ===========================================
-- Siga Feliz — PostgreSQL Schema Initialization
-- ===========================================
-- This script runs automatically when the PostgreSQL container starts for
-- the first time (docker-entrypoint-initdb.d).

CREATE TABLE IF NOT EXISTS semesters (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    start_date VARCHAR(10) NOT NULL,
    end_date VARCHAR(10) NOT NULL,
    kickoff_date VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS blocked_days (
    id SERIAL PRIMARY KEY,
    semester_id INTEGER NOT NULL REFERENCES semesters(id) ON DELETE CASCADE,
    blocked_date VARCHAR(10) NOT NULL,
    description VARCHAR(255),
    day_type VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS planning_units (
    id SERIAL PRIMARY KEY,
    semester_id INTEGER NOT NULL REFERENCES semesters(id),
    subject_name VARCHAR(150) NOT NULL,
    workload INTEGER NOT NULL,
    weekly_schedule VARCHAR(500) NOT NULL,
    created_at VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS temas (
    id SERIAL PRIMARY KEY,
    planning_unit_id INTEGER NOT NULL REFERENCES planning_units(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    min_aulas INTEGER NOT NULL,
    max_aulas INTEGER NOT NULL,
    priority VARCHAR(10) NOT NULL,
    is_evaluation INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS generated_schedules (
    id SERIAL PRIMARY KEY,
    planning_unit_id INTEGER NOT NULL REFERENCES planning_units(id),
    generated_at VARCHAR(30) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    subject_name VARCHAR(150) NOT NULL,
    semester_name VARCHAR(50) NOT NULL,
    workload INTEGER NOT NULL DEFAULT 40
);
