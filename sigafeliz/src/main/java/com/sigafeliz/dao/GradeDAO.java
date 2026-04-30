package com.sigafeliz.dao;

import com.sigafeliz.model.AulasPorDia;
import com.sigafeliz.infra.ConexaoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GradeDAO{

    public void salvarGrade(AulasPorDia a) throws SQLException {
        String sql = """
                INSERT INTO aula_por_dia (disciplina_nome, dia_semana, quantidade_aulas)
                VALUES (?, ?, ?)
                """;

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getDisciplina().getNome());
            ps.setString(2, a.getDiaSemana().name());
            ps.setInt(3, a.getQuantidadeAulas());

            ps.executeUpdate();
        }

    }


}