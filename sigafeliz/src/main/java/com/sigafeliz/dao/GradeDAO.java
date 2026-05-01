package com.sigafeliz.dao;

import com.sigafeliz.model.AulasPorDia;
import com.sigafeliz.infra.ConexaoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GradeDAO{


    //TODO Deleta o banco para poder colocar a nova opção.
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
            ps.setInt(2, a.getDiaSemana().getValue());
            ps.setInt(3, a.getQuantidadeAulas());

            ps.executeUpdate();
        }

    }


}