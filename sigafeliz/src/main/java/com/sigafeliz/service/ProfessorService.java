package com.sigafeliz.service;

import com.sigafeliz.dao.ProfessorDAO;
import com.sigafeliz.model.Professor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProfessorService {

    private static final ProfessorDAO dao = new ProfessorDAO();
    private static Professor professorLogado;

    public static ObservableList<Professor> getAllProfessores() {
        try {
            return FXCollections.observableArrayList(dao.listarTodos());
        } catch (SQLException e) {
            System.err.println("Erro ao buscar professores: " + e.getMessage());
            return FXCollections.observableArrayList(new ArrayList<>());
        }
    }

    public static void setProfessorLogado(Professor p) {
        professorLogado = p;
    }

    public static Professor getProfessorLogado() {
        return professorLogado;
    }

    public static boolean isSelecaoValida() {
        return professorLogado != null &&
                professorLogado.getEmail() != null &&
                professorLogado.getEmail().contains("@");
    }

    public static void salvar(Professor p) throws SQLException {
        dao.salvar(p);
    }

    public static Professor getProfessorPorNome(String nome) {
        try {
            return dao.buscarPorNome(nome);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar professor: " + e.getMessage());
            return null;
        }
    }
}