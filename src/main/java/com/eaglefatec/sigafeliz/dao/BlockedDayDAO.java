package com.eaglefatec.sigafeliz.dao;

import com.eaglefatec.sigafeliz.model.BlockedDay;
import com.eaglefatec.sigafeliz.model.BlockedDay.DayType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlockedDayDAO {

    public List<BlockedDay> findBySemesterId(int semesterId) {
        List<BlockedDay> list = new ArrayList<>();
        String sql = "SELECT id, semester_id, blocked_date, description, day_type FROM blocked_days WHERE semester_id = ? ORDER BY blocked_date";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, semesterId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BlockedDay b = new BlockedDay();
                    b.setId(rs.getInt("id"));
                    b.setSemesterId(rs.getInt("semester_id"));
                    b.setBlockedDate(rs.getString("blocked_date"));
                    b.setDescription(rs.getString("description"));
                    b.setDayType(DayType.valueOf(rs.getString("day_type")));
                    list.add(b);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void insert(BlockedDay b) {
        String sql = "INSERT INTO blocked_days (semester_id, blocked_date, description, day_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, b.getSemesterId());
            ps.setString(2, b.getBlockedDate());
            ps.setString(3, b.getDescription());
            ps.setString(4, b.getDayType().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next())
                    b.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM blocked_days WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
