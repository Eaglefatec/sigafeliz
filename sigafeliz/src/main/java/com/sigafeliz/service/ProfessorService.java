package com.sigafeliz.service;

import com.sigafeliz.dao.ProfessorDAO;
import com.sigafeliz.model.Professor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfessorService {

    private static final ProfessorDAO dao = new ProfessorDAO();

    // Estado de sessão (tira do Mock quando estiver pronto)
    private static Professor professorLogado;

    public static List<Professor> getAllProfessores() {
        try {
            return dao.listarTodos();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar professores: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static Professor getProfessorPorNome(String nome) {
        try {
            return dao.buscarPorNome(nome);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar professor: " + e.getMessage());
            return null;
        }
    }

    public static void setProfessorLogado(Professor p) { professorLogado = p; }
    public static Professor getProfessorLogado()       { return professorLogado; }
}