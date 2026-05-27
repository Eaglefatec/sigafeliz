package com.sigafeliz.service;

import com.sigafeliz.dao.TemaDAO;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Tema;

import java.sql.SQLException;
import java.util.List;

public class TemaService {
    private static final TemaDAO dao = new TemaDAO();

    public static List<Tema> getTemasPorDisciplina(Disciplina d) throws SQLException {
        return dao.listarPorDisciplina(d);
    }

    public static void salvar(Tema t) throws SQLException {
        dao.salvar(t);
    }

    public static void editar(Tema t, String tituloAntigo) throws SQLException {
        dao.editar(t, tituloAntigo);
    }

    public static void excluir(Tema t) throws SQLException {
        dao.excluir(t.getDisciplina().getNome(), t.getTitulo());
    }
}