package com.sigafeliz.dao;

import com.sigafeliz.infra.ConexaoDB;
import com.sigafeliz.model.AulasPorDia;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Professor;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {

    public List<Disciplina> listarTodas() throws SQLException {
        List<Disciplina> lista = new ArrayList<>();
        String sql = """
                SELECT d.nome, d.carga_horaria_total,
                       d.aula_segunda, d.aula_terca, d.aula_quarta, d.aula_quinta, d.aula_sexta,
                       p.nome as prof_nome, p.email as prof_email
                FROM disciplina d
                LEFT JOIN professor p ON d.professor_email = p.email
                """;

        try (Connection con = ConexaoDB.getConexao();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Professor prof = null;
                if (rs.getString("prof_email") != null) {
                    prof = new Professor(rs.getString("prof_nome"), rs.getString("prof_email"));
                }

                Disciplina d = new Disciplina(rs.getString("nome"), prof, rs.getInt("carga_horaria_total"));

                if (rs.getInt("aula_segunda") > 0) d.addAulaPorDia(new AulasPorDia(d, DayOfWeek.MONDAY, rs.getInt("aula_segunda")));
                if (rs.getInt("aula_terca") > 0) d.addAulaPorDia(new AulasPorDia(d, DayOfWeek.TUESDAY, rs.getInt("aula_terca")));
                if (rs.getInt("aula_quarta") > 0) d.addAulaPorDia(new AulasPorDia(d, DayOfWeek.WEDNESDAY, rs.getInt("aula_quarta")));
                if (rs.getInt("aula_quinta") > 0) d.addAulaPorDia(new AulasPorDia(d, DayOfWeek.THURSDAY, rs.getInt("aula_quinta")));
                if (rs.getInt("aula_sexta") > 0) d.addAulaPorDia(new AulasPorDia(d, DayOfWeek.FRIDAY, rs.getInt("aula_sexta")));

                lista.add(d);
            }
        }
        return lista;
    }

    public List<Disciplina> listarPorProfessor(Professor professor) throws SQLException {
        List<Disciplina> lista = new ArrayList<>();
        String sql = """
                SELECT d.nome, d.carga_horaria_total,
                       d.aula_segunda, d.aula_terca, d.aula_quarta, d.aula_quinta, d.aula_sexta
                FROM disciplina d
                WHERE d.professor_email = ?
                """;

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, professor.getEmail());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Disciplina d = new Disciplina(rs.getString("nome"), professor, rs.getInt("carga_horaria_total"));
                    if (rs.getInt("aula_segunda") > 0) d.addAulaPorDia(new AulasPorDia(d, DayOfWeek.MONDAY, rs.getInt("aula_segunda")));
                    if (rs.getInt("aula_terca") > 0) d.addAulaPorDia(new AulasPorDia(d, DayOfWeek.TUESDAY, rs.getInt("aula_terca")));
                    if (rs.getInt("aula_quarta") > 0) d.addAulaPorDia(new AulasPorDia(d, DayOfWeek.WEDNESDAY, rs.getInt("aula_quarta")));
                    if (rs.getInt("aula_quinta") > 0) d.addAulaPorDia(new AulasPorDia(d, DayOfWeek.THURSDAY, rs.getInt("aula_quinta")));
                    if (rs.getInt("aula_sexta") > 0) d.addAulaPorDia(new AulasPorDia(d, DayOfWeek.FRIDAY, rs.getInt("aula_sexta")));

                    lista.add(d);
                }
            }
        }
        return lista;
    }

    public boolean existeDisciplina(String nome) throws SQLException {
        String sql = "SELECT 1 FROM disciplina WHERE nome = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void salvar(Disciplina d) throws SQLException {
        String sql = """
                INSERT INTO disciplina (nome, professor_email, carga_horaria_total, 
                                        aula_segunda, aula_terca, aula_quarta, aula_quinta, aula_sexta)
                VALUES (?, ?, ?, 0, 0, 0, 0, 0)
                """;

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getNome());
            ps.setString(2, d.getProfessor() != null ? d.getProfessor().getEmail() : null);
            ps.setInt(3, d.getCargaHorariaTotal());
            ps.executeUpdate();
        }
    }

    // ADICIONADO: Método essencial para salvar a grade de aulas diretamente na tabela disciplina
    public void atualizarGrade(String nomeDisciplina, int seg, int ter, int qua, int qui, int sex) throws SQLException {
        String sql = """
                UPDATE disciplina 
                SET aula_segunda = ?, 
                    aula_terca = ?, 
                    aula_quarta = ?, 
                    aula_quinta = ?, 
                    aula_sexta = ? 
                WHERE nome = ?
                """;

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, seg);
            ps.setInt(2, ter);
            ps.setInt(3, qua);
            ps.setInt(4, qui);
            ps.setInt(5, sex);
            ps.setString(6, nomeDisciplina);
            ps.executeUpdate();
        }
    }

    public void excluir(String nome) throws SQLException {
        String sql = "DELETE FROM disciplina WHERE nome = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.executeUpdate();
        }
    }
}