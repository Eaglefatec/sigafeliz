package com.sigafeliz.dao;

import com.sigafeliz.infra.ConexaoDB;
import com.sigafeliz.model.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {

    public List<Professor> listarTodos() throws SQLException {
        List<Professor> lista = new ArrayList<>();
        String sql = "SELECT * FROM professor";

        try (Connection con = ConexaoDB.getConexao();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Professor p = new Professor();
                p.setNome(rs.getString("nome"));
                // preencha os outros campos do seu model
                lista.add(p);
            }
        }
        return lista;
    }

    public void salvar(Professor p) throws SQLException {
        String sql = "INSERT INTO professor (nome) VALUES (?)";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.executeUpdate();
        }
    }
}