package com.eaglefatec.sigafeliz.dao;

import com.eaglefatec.sigafeliz.model.PlanningUnit;
import com.eaglefatec.sigafeliz.model.Tema;
import com.eaglefatec.sigafeliz.model.Tema.Priority;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanningUnitDAO {

    public int insert(PlanningUnit pu) {
        String sql = "INSERT INTO planning_units (semester_id, subject_name, workload, weekly_schedule, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, pu.getSemesterId());
                ps.setString(2, pu.getSubjectName());
                ps.setInt(3, pu.getWorkload());
                ps.setString(4, pu.getWeeklyScheduleJson());
                ps.setString(5, pu.getCreatedAt());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next())
                        pu.setId(keys.getInt(1));
                }
            }
            // Insert temas
            String temaSql = "INSERT INTO temas (planning_unit_id, title, min_aulas, max_aulas, priority, is_evaluation) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(temaSql)) {
                for (Tema t : pu.getTemas()) {
                    t.setPlanningUnitId(pu.getId());
                    ps.setInt(1, pu.getId());
                    ps.setString(2, t.getTitle());
                    ps.setInt(3, t.getMinAulas());
                    ps.setInt(4, t.getMaxAulas());
                    ps.setString(5, t.getPriority().name());
                    ps.setInt(6, t.isEvaluation() ? 1 : 0);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            conn.commit();
            return pu.getId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PlanningUnit> findBySemesterId(int semesterId) {
        List<PlanningUnit> list = new ArrayList<>();
        String sql = "SELECT * FROM planning_units WHERE semester_id = ? ORDER BY created_at DESC";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, semesterId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PlanningUnit pu = mapRow(rs);
                    pu.setTemas(findTemasByPlanningUnitId(conn, pu.getId()));
                    list.add(pu);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private PlanningUnit mapRow(ResultSet rs) throws SQLException {
        PlanningUnit pu = new PlanningUnit();
        pu.setId(rs.getInt("id"));
        pu.setSemesterId(rs.getInt("semester_id"));
        pu.setSubjectName(rs.getString("subject_name"));
        pu.setWorkload(rs.getInt("workload"));
        pu.setWeeklyScheduleJson(rs.getString("weekly_schedule"));
        pu.setCreatedAt(rs.getString("created_at"));
        return pu;
    }

    private List<Tema> findTemasByPlanningUnitId(Connection conn, int puId) throws SQLException {
        List<Tema> temas = new ArrayList<>();
        String sql = "SELECT * FROM temas WHERE planning_unit_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, puId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tema t = new Tema();
                    t.setId(rs.getInt("id"));
                    t.setPlanningUnitId(rs.getInt("planning_unit_id"));
                    t.setTitle(rs.getString("title"));
                    t.setMinAulas(rs.getInt("min_aulas"));
                    t.setMaxAulas(rs.getInt("max_aulas"));
                    t.setPriority(Priority.valueOf(rs.getString("priority")));
                    t.setEvaluation(rs.getInt("is_evaluation") == 1);
                    temas.add(t);
                }
            }
        }
        return temas;
    }
}
