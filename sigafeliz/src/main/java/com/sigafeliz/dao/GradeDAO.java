package com.sigafeliz.dao;

import com.sigafeliz.model.AulasPorDia;
import com.sigafeliz.infra.ConexaoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

public class GradeDAO {

    public void deletarPorDisciplina(String nomeDisciplina) throws SQLException {
        String sql = "DELETE FROM aula_por_dia WHERE disciplina_nome = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nomeDisciplina);
            ps.executeUpdate();
        }
    }

    public void salvarGrade(AulasPorDia a) throws SQLException {
        String sql = """
                INSERT INTO aula_por_dia (disciplina_nome, dia_semana, quantidade_aulas)
                VALUES (?, ?, ?)
                """;
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getDisciplina().getNome());
            ps.setInt(2, a.getDiaSemana().getValue()); // DayOfWeek.getValue() = 1(Seg)..7(Dom)
            ps.setInt(3, a.getQuantidadeAulas());
            ps.executeUpdate();
        }
    }

    public Map<DayOfWeek, Integer> buscarGradePorDisciplina(String nomeDisciplina) throws SQLException {
        String sql = "SELECT dia_semana, quantidade_aulas FROM aula_por_dia WHERE disciplina_nome = ?";
        Map<DayOfWeek, Integer> grade = new HashMap<>();

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nomeDisciplina);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int diaInt = rs.getInt("dia_semana");
                // Banco guarda 1=Seg, 2=Ter... mas DayOfWeek.of() espera 1=Seg também — OK!
                DayOfWeek dia = DayOfWeek.of(diaInt);
                int quantidade = rs.getInt("quantidade_aulas");
                grade.put(dia, quantidade);
            }
        }
        return grade;
    }
}