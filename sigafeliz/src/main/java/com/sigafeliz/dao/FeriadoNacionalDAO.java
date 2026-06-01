package com.sigafeliz.dao;

import com.sigafeliz.infra.ConexaoDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FeriadoNacionalDAO {

    /**
     * Verifica se existe qualquer feriado cadastrado para o ano informado.
     */
    public boolean existeFeriadoNoAno(int ano) throws SQLException {
        String sql = "SELECT 1 FROM feriado_nacional WHERE EXTRACT(YEAR FROM data) = ? LIMIT 1";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ano);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Salva o feriado nacional caso ele já não exista no banco (Prevenção via ON CONFLICT).
     */
    public void salvar(LocalDate data, String descricao) throws SQLException {
        String sql = "INSERT INTO feriado_nacional (data, descricao) VALUES (?, ?) ON CONFLICT (data) DO NOTHING";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(data));
            ps.setString(2, descricao);
            ps.executeUpdate();
        }
    }

    /**
     * NOVO: Busca todos os feriados nacionais dentro de um intervalo de datas para vinculação automática.
     */
    public List<FeriadoNacionalDTO> listarPorIntervalo(LocalDate inicio, LocalDate fim) throws SQLException {
        List<FeriadoNacionalDTO> lista = new ArrayList<>();
        String sql = "SELECT data, descricao FROM feriado_nacional WHERE data >= ? AND data <= ? ORDER BY data";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fim));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate data = rs.getDate("data").toLocalDate();
                    String descricao = rs.getString("descricao");
                    lista.add(new FeriadoNacionalDTO(data, descricao));
                }
            }
        }
        return lista;
    }

    // DTO utilizando Record (Suportado nativamente a partir do Java 16+)
    public static record FeriadoNacionalDTO(LocalDate data, String descricao) {}
}