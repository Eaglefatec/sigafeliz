package com.eaglefatec.sigafeliz.dao;

import com.eaglefatec.sigafeliz.model.Semester;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SemesterDAO {

    public List<Semester> findAll() {
        List<Semester> list = new ArrayList<>();
        String sql = "SELECT id, name, start_date, end_date, kickoff_date FROM semesters ORDER BY name DESC";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Semester findById(int id) {
        String sql = "SELECT id, name, start_date, end_date, kickoff_date FROM semesters WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void insert(Semester s) {
        String sql = "INSERT INTO semesters (name, start_date, end_date, kickoff_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getStartDate());
            ps.setString(3, s.getEndDate());
            ps.setString(4, s.getKickoffDate());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next())
                    s.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM semesters WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Semester mapRow(ResultSet rs) throws SQLException {
        Semester s = new Semester();
        s.setId(rs.getInt("id"));
        s.setName(rs.getString("name"));
        s.setStartDate(rs.getString("start_date"));
        s.setEndDate(rs.getString("end_date"));
        s.setKickoffDate(rs.getString("kickoff_date"));
        return s;
    }
}
