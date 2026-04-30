package com.sigafeliz.dao;

import com.sigafeliz.infra.ConexaoDB;
import com.sigafeliz.model.DiaRestrito;
import com.sigafeliz.model.Semestre;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DiaRestritoDAO {

    public void salvar(DiaRestrito dr) throws SQLException {
        String sql = "INSERT INTO dia_restrito (semestre_nome, data, descricao) VALUES (?, ?, ?)";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dr.getSemestre().getNome());
            ps.setDate(2, Date.valueOf(dr.getData()));
            ps.setString(3, dr.getDescricao());
            ps.executeUpdate();
        }
    }

    public void excluir(String semestreNome, LocalDate data) throws SQLException {
        String sql = "DELETE FROM dia_restrito WHERE semestre_nome = ? AND data = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, semestreNome);
            ps.setDate(2, Date.valueOf(data));
            ps.executeUpdate();
        }
    }

    public List<DiaRestrito> listarPorSemestre(Semestre semestre) throws SQLException {
        List<DiaRestrito> lista = new ArrayList<>();
        String sql = "SELECT data, descricao FROM dia_restrito WHERE semestre_nome = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, semestre.getNome());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate data = rs.getDate("data").toLocalDate();
                    String descricao = rs.getString("descricao");
                    lista.add(new DiaRestrito(semestre, data, descricao));
                }
            }
        }
        return lista;
    }
}