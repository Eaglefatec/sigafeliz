package com.sigafeliz.dao;

import com.sigafeliz.infra.ConexaoDB;
import com.sigafeliz.model.Semestre;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SemestreDAO {

    public void salvar(Semestre semestre) throws SQLException {
        String sql = """
                INSERT INTO semestre (nome, data_inicio, data_fim, data_kickoff)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, semestre.getNome());
            ps.setDate(2, Date.valueOf(semestre.getDataInicio()));
            ps.setDate(3, Date.valueOf(semestre.getDataFim()));
            ps.setDate(4, Date.valueOf(semestre.getDataKickoff()));

            ps.executeUpdate();
        }
    }

    // --- NOVO MÉTODO DE EXCLUSÃO ---
    public void excluir(String nome) throws SQLException {
        String sql = "DELETE FROM semestre WHERE nome = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.executeUpdate();
        }
    }

    public List<Semestre> listarTodos() throws SQLException {
        List<Semestre> lista = new ArrayList<>();
        String sql = "SELECT nome, data_inicio, data_fim, data_kickoff FROM semestre";

        try (Connection con = ConexaoDB.getConexao();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                LocalDate inicio = rs.getDate("data_inicio") != null ? rs.getDate("data_inicio").toLocalDate() : null;
                LocalDate fim = rs.getDate("data_fim") != null ? rs.getDate("data_fim").toLocalDate() : null;
                LocalDate kickoff = rs.getDate("data_kickoff") != null ? rs.getDate("data_kickoff").toLocalDate() : null;

                lista.add(new Semestre(rs.getString("nome"), inicio, fim, kickoff));
            }
        }
        return lista;
    }

    public Semestre buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT nome, data_inicio, data_fim, data_kickoff FROM semestre WHERE nome = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate inicio = rs.getDate("data_inicio") != null ? rs.getDate("data_inicio").toLocalDate() : null;
                    LocalDate fim = rs.getDate("data_fim") != null ? rs.getDate("data_fim").toLocalDate() : null;
                    LocalDate kickoff = rs.getDate("data_kickoff") != null ? rs.getDate("data_kickoff").toLocalDate() : null;

                    return new Semestre(rs.getString("nome"), inicio, fim, kickoff);
                }
            }
        }
        return null;
    }
}