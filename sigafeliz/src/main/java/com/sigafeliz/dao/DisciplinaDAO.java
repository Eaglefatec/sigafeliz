package com.sigafeliz.dao;

import com.sigafeliz.infra.ConexaoDB;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {

    // Busca disciplinas de um professor, já com o objeto Professor montado
    public List<Disciplina> listarPorProfessor(Professor professor) throws SQLException {
        List<Disciplina> lista = new ArrayList<>();
        String sql = """
                SELECT d.nome, d.carga_horaria_total
                FROM disciplina d
                WHERE d.professor_email = ?
                """;

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, professor.getEmail());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Disciplina(
                            rs.getString("nome"),
                            professor,
                            rs.getInt("carga_horaria_total")
                    ));
                }
            }
        }
        return lista;
    }

    public void salvar(Disciplina d) throws SQLException {
        String sql = """
                INSERT INTO disciplina (nome, professor_email, carga_horaria_total)
                VALUES (?, ?, ?)
                """;

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, d.getNome());
            ps.setString(2, d.getProfessor().getEmail());
            ps.setInt(3, d.getCargaHorariaTotal());
            ps.executeUpdate();
        }
    }
}