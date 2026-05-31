package com.sigafeliz.service;

import com.sigafeliz.dao.DisciplinaDAO;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Professor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaService {

    private static final DisciplinaDAO dao = new DisciplinaDAO();
    private static Disciplina disciplinaSelecionada;

    // Busca todas as disciplinas cadastradas no banco
    public static ObservableList<Disciplina> getAllDisciplinas() {
        try {
            return FXCollections.observableArrayList(dao.listarTodas());
        } catch (SQLException e) {
            System.err.println("Erro ao listar disciplinas: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }

    public static List<Disciplina> getDisciplinasPorProfessor(Professor p) {
        if (p == null || p.getEmail() == null) return new ArrayList<>();
        try {
            return dao.listarPorProfessor(p);
        } catch (SQLException e) {
            System.err.println("Erro ao listar disciplinas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void salvar(Disciplina d) throws SQLException {
        // VALIDAÇÃO ADICIONADA: Verifica se o nome da disciplina já existe no banco
        if (dao.existeDisciplina(d.getNome())) {
            throw new SQLException("Já existe uma disciplina cadastrada com este nome (talvez esteja sendo ministrada por outro professor). Escolha um nome diferente ou adicione um identificador (ex: Matemática A).");
        }

        dao.salvar(d);
    }

    // Exclui a disciplina
    public static void excluir(Disciplina d) throws SQLException {
        dao.excluir(d.getNome());
    }

    public static void setDisciplinaSelecionada(Disciplina d) {
        disciplinaSelecionada = d;
    }

    public static Disciplina getDisciplinaSelecionada() {
        return disciplinaSelecionada;
    }
}