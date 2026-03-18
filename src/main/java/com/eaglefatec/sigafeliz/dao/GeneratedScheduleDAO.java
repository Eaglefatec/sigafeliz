package com.eaglefatec.sigafeliz.dao;

import com.eaglefatec.sigafeliz.model.GeneratedSchedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GeneratedScheduleDAO {

    public void insert(GeneratedSchedule gs) {
        String sql = "INSERT INTO generated_schedules (planning_unit_id, generated_at, file_path, subject_name, semester_name, workload) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, gs.getPlanningUnitId());
            ps.setString(2, gs.getGeneratedAt());
            ps.setString(3, gs.getFilePath());
            ps.setString(4, gs.getSubjectName());
            ps.setString(5, gs.getSemesterName());
            ps.setInt(6, gs.getWorkload());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next())
                    gs.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<GeneratedSchedule> findAll() {
        List<GeneratedSchedule> list = new ArrayList<>();
        String sql = "SELECT * FROM generated_schedules ORDER BY generated_at DESC";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                GeneratedSchedule gs = new GeneratedSchedule();
                gs.setId(rs.getInt("id"));
                gs.setPlanningUnitId(rs.getInt("planning_unit_id"));
                gs.setGeneratedAt(rs.getString("generated_at"));
                gs.setFilePath(rs.getString("file_path"));
                gs.setSubjectName(rs.getString("subject_name"));
                gs.setSemesterName(rs.getString("semester_name"));
                gs.setWorkload(rs.getInt("workload"));
                list.add(gs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
