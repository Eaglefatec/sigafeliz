package com.sigafeliz.dao;

import com.sigafeliz.infra.ConexaoDB;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Prioridade;
import com.sigafeliz.model.Tema;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TemaDAO {

    public List<Tema> listarPorDisciplina(Disciplina disciplina) throws SQLException {
        List<Tema> lista = new ArrayList<>();
        String sql = "SELECT titulo, carga_minima, carga_maxima, prioridade, e_avaliacao, ordem " +
                "FROM tema WHERE disciplina_nome = ? ORDER BY ordem";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, disciplina.getNome());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Tema(
                            disciplina,
                            rs.getString("titulo"),
                            rs.getInt("carga_minima"),
                            rs.getInt("carga_maxima"),
                            Prioridade.valueOf(rs.getString("prioridade")),
                            rs.getBoolean("e_avaliacao"),
                            rs.getInt("ordem")
                    ));
                }
            }
        }
        return lista;
    }

    public void salvar(Tema t) throws SQLException {
        String sql = """
                INSERT INTO tema (disciplina_nome, titulo, carga_minima, carga_maxima, prioridade, e_avaliacao, ordem)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getDisciplina().getNome());
            ps.setString(2, t.getTitulo());
            ps.setInt(3, t.getCargaMinima());
            ps.setInt(4, t.getCargaMaxima());
            ps.setString(5, t.getPrioridade().name());
            ps.setBoolean(6, t.isEAvaliacao());
            ps.setInt(7, t.getOrdem());
            ps.executeUpdate();
        }
    }

    public void editar(Tema t, String tituloAntigo) throws SQLException {
        String sql = """
                UPDATE tema 
                SET titulo = ?, carga_minima = ?, carga_maxima = ?, prioridade = ?, e_avaliacao = ?, ordem = ?
                WHERE disciplina_nome = ? AND titulo = ?
                """;

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getTitulo());
            ps.setInt(2, t.getCargaMinima());
            ps.setInt(3, t.getCargaMaxima());
            ps.setString(4, t.getPrioridade().name());
            ps.setBoolean(5, t.isEAvaliacao());
            ps.setInt(6, t.getOrdem());
            ps.setString(7, t.getDisciplina().getNome());
            ps.setString(8, tituloAntigo); // Usado na cláusula WHERE para achar o registro correto
            ps.executeUpdate();
        }
    }

    public void excluir(String disciplinaNome, String titulo) throws SQLException {
        String sql = "DELETE FROM tema WHERE disciplina_nome = ? AND titulo = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, disciplinaNome);
            ps.setString(2, titulo);
            ps.executeUpdate();
        }
    }
    /**
     * Busca todos os temas de uma disciplina, ordenados por ordem.
     */
    public List<Tema> buscarPorDisciplina(String disciplinaNome) throws SQLException {
        List<Tema> lista = new ArrayList<>();

        String sql = """
                SELECT t.disciplina_nome, t.titulo, t.carga_minima, t.carga_maxima,
                       t.prioridade, t.e_avaliacao, t.ordem,
                       d.professor_email, d.carga_horaria_total
                FROM tema t
                JOIN disciplina d ON d.nome = t.disciplina_nome
                WHERE t.disciplina_nome = ?
                ORDER BY t.ordem
                """;

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, disciplinaNome);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Disciplina disciplina = new Disciplina(
                        rs.getString("disciplina_nome"),
                        null, // Professor pode ser carregado se necessário
                        rs.getInt("carga_horaria_total")
                );

                Prioridade prioridade = Prioridade.valueOf(
                        rs.getString("prioridade").toUpperCase()
                );

                Tema tema = new Tema(
                        disciplina,
                        rs.getString("titulo"),
                        rs.getInt("carga_minima"),
                        rs.getInt("carga_maxima"),
                        prioridade,
                        rs.getBoolean("e_avaliacao"),
                        rs.getInt("ordem")
                );

                lista.add(tema);
            }
        }

        return lista;
    }

    /**
     * Busca o total de aulas por semana de uma disciplina
     * somando os dias cadastrados em aula_por_dia.
     */
    public int buscarAulasPorSemana(String disciplinaNome) throws SQLException {
        String sql = """
                SELECT COALESCE(SUM(quantidade_aulas), 0) AS total
                FROM aula_por_dia
                WHERE disciplina_nome = ?
                """;

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, disciplinaNome);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("total");
        }

        return 1;
    }

    /**
     * Salva as aulas alocadas pelo algoritmo na tabela tema.
     *
     * Rode antes no banco:
     * ALTER TABLE tema ADD COLUMN IF NOT EXISTS aulas_alocadas int4 DEFAULT 0;
     */
    public void salvarAulasAlocadas(Tema tema) throws SQLException {
        String sql = """
                UPDATE tema
                SET aulas_alocadas = ?
                WHERE disciplina_nome = ? AND titulo = ?
                """;

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tema.getAulasAlocadas());
            ps.setString(2, tema.getDisciplina().getNome());
            ps.setString(3, tema.getTitulo());
            ps.executeUpdate();
        }
    }
}