package com.sigafeliz.dao;

import com.sigafeliz.infra.ConexaoDB;
import com.sigafeliz.model.SabadoLetivo;
import com.sigafeliz.model.Semestre;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SabadoLetivoDAO {

    public void salvar(SabadoLetivo sl) throws SQLException {
        String sql = "INSERT INTO sabado_letivo (data, semestre_nome) VALUES (?, ?)";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(sl.getData()));
            ps.setString(2, sl.getSemestre().getNome());
            ps.executeUpdate();
        }
    }

    public void excluir(String semestreNome, LocalDate data) throws SQLException {
        String sql = "DELETE FROM sabado_letivo WHERE semestre_nome = ? AND data = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, semestreNome);
            ps.setDate(2, Date.valueOf(data));
            ps.executeUpdate();
        }
    }

    public List<SabadoLetivo> listarPorSemestre(Semestre semestre) throws SQLException {
        List<SabadoLetivo> lista = new ArrayList<>();
        String sql = "SELECT data FROM sabado_letivo WHERE semestre_nome = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, semestre.getNome());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate data = rs.getDate("data").toLocalDate();
                    lista.add(new SabadoLetivo(semestre, data));
                }
            }
        }
        return lista;
    }
}