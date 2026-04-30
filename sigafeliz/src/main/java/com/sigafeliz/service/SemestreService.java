package com.sigafeliz.service;

import com.sigafeliz.dao.DiaRestritoDAO;
import com.sigafeliz.dao.SabadoLetivoDAO;
import com.sigafeliz.dao.SemestreDAO;
import com.sigafeliz.model.DiaRestrito;
import com.sigafeliz.model.SabadoLetivo;
import com.sigafeliz.model.Semestre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class SemestreService {

    private static final SemestreDAO semestreDAO = new SemestreDAO();
    private static final DiaRestritoDAO diaRestritoDAO = new DiaRestritoDAO();
    private static final SabadoLetivoDAO sabadoLetivoDAO = new SabadoLetivoDAO();

    private static Semestre semestreSelecionado;

    // --- MÉTODOS DO SEMESTRE ---
    public static ObservableList<Semestre> getAllSemestres() {
        try {
            return FXCollections.observableArrayList(semestreDAO.listarTodos());
        } catch (SQLException e) {
            System.err.println("Erro ao buscar semestres: " + e.getMessage());
            return FXCollections.observableArrayList(new ArrayList<>());
        }
    }

    public static void salvar(Semestre s) throws SQLException {
        if (semestreDAO.buscarPorNome(s.getNome()) != null) {
            throw new SQLException("Já existe um semestre cadastrado com este nome.");
        }
        if (s.getDataInicio().isAfter(s.getDataFim())) {
            throw new SQLException("A data de início não pode ser depois da data de fim.");
        }
        semestreDAO.salvar(s);
    }

    // --- MÉTODOS DOS DIAS DO SEMESTRE ---

    // Busca no banco todos os feriados e sábados atrelados a um semestre selecionado
    public static void carregarDetalhes(Semestre semestre) throws SQLException {
        semestre.getDiasRestritos().clear();
        semestre.getSabadosLetivos().clear();
        semestre.getDiasRestritos().addAll(diaRestritoDAO.listarPorSemestre(semestre));
        semestre.getSabadosLetivos().addAll(sabadoLetivoDAO.listarPorSemestre(semestre));
    }

    // Insere ou deleta sábados letivos
    public static void alternarSabadoLetivo(Semestre semestre, LocalDate data) throws SQLException {
        boolean existe = semestre.getSabadosLetivos().stream().anyMatch(s -> s.getData().equals(data));
        if (existe) {
            sabadoLetivoDAO.excluir(semestre.getNome(), data);
            semestre.getSabadosLetivos().removeIf(s -> s.getData().equals(data));
        } else {
            SabadoLetivo sl = new SabadoLetivo(semestre, data);
            sabadoLetivoDAO.salvar(sl);
            semestre.addSabadoLetivo(sl);
        }
    }

    public static void adicionarDiaRestrito(Semestre semestre, LocalDate data, String descricao) throws SQLException {
        DiaRestrito dr = new DiaRestrito(semestre, data, descricao);
        diaRestritoDAO.salvar(dr);
        semestre.addDiaRestrito(dr);
    }

    public static void removerDiaRestrito(Semestre semestre, LocalDate data) throws SQLException {
        diaRestritoDAO.excluir(semestre.getNome(), data);
        semestre.getDiasRestritos().removeIf(d -> d.getData().equals(data));
    }

    // --- SESSÃO ---
    public static void setSemestreSelecionado(Semestre s) {
        semestreSelecionado = s;
    }

    public static Semestre getSemestreSelecionado() {
        return semestreSelecionado;
    }
}