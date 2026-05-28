package com.sigafeliz.service;

import com.sigafeliz.dao.DiaRestritoDAO;
import com.sigafeliz.dao.FeriadoNacionalDAO; // Import adicionado
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
import java.util.List; // Import adicionado

public class SemestreService {

    private static final SemestreDAO semestreDAO = new SemestreDAO();
    private static final DiaRestritoDAO diaRestritoDAO = new DiaRestritoDAO();
    private static final SabadoLetivoDAO sabadoLetivoDAO = new SabadoLetivoDAO();
    private static final FeriadoNacionalDAO feriadoNacionalDAO = new FeriadoNacionalDAO(); // Instanciado o DAO de Feriados

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

        // Validação de intervalo de dias do semestre
        long dias = java.time.temporal.ChronoUnit.DAYS.between(s.getDataInicio(), s.getDataFim());
        if (dias < 110 || dias > 200) {
            throw new SQLException("O semestre deve ter entre 110 e 200 dias de duração.");
        }

        // Validação da Segunda-Feira do Kickoff
        if (s.getDataKickoff().getDayOfWeek() != java.time.DayOfWeek.MONDAY) {
            throw new SQLException("A data de Kickoff deve ser obrigatoriamente uma segunda-feira.");
        }

        // Validação do ciclo mínimo de Sprints
        long diasKickoffFim = java.time.temporal.ChronoUnit.DAYS.between(s.getDataKickoff(), s.getDataFim());
        if (diasKickoffFim < 98) {
            throw new SQLException("A data de Kickoff deve permitir no mínimo 98 dias até o fim do semestre.");
        }

        // 1. Salva a entidade pai (Semestre)
        semestreDAO.salvar(s);

        // --- 2. NOVO: BUSCA OS FERIADOS NACIONAIS DO INTERVALO E VINCULA COMO DIA RESTRITO ---
        List<FeriadoNacionalDAO.FeriadoNacionalDTO> feriados = feriadoNacionalDAO.listarPorIntervalo(s.getDataInicio(), s.getDataFim());
        for (FeriadoNacionalDAO.FeriadoNacionalDTO f : feriados) {
            // Instancia e salva a restrição correspondente para este semestre
            DiaRestrito dr = new DiaRestrito(s, f.data(), f.descricao());
            diaRestritoDAO.salvar(dr);
            s.addDiaRestrito(dr); // Sincroniza o objeto em memória
        }
    }

    // --- ATUALIZAR SEMESTRE ---
    public static void atualizar(Semestre s) throws SQLException {
        long dias = java.time.temporal.ChronoUnit.DAYS.between(s.getDataInicio(), s.getDataFim());
        if (dias < 110 || dias > 200) {
            throw new SQLException("O semestre deve ter entre 110 e 200 dias de duração.");
        }

        if (s.getDataKickoff().getDayOfWeek() != java.time.DayOfWeek.MONDAY) {
            throw new SQLException("A data de Kickoff deve ser obrigatoriamente uma segunda-feira.");
        }

        long diasKickoffFim = java.time.temporal.ChronoUnit.DAYS.between(s.getDataKickoff(), s.getDataFim());
        if (diasKickoffFim < 98) {
            throw new SQLException("A data de Kickoff deve permitir no mínimo 98 dias até o fim do semestre.");
        }

        semestreDAO.atualizar(s);
    }

    // --- MÉTODOS DOS DIAS DO SEMESTRE ---

    public static void carregarDetalhes(Semestre semestre) throws SQLException {
        semestre.getDiasRestritos().clear();
        semestre.getSabadosLetivos().clear();
        semestre.getDiasRestritos().addAll(diaRestritoDAO.listarPorSemestre(semestre));
        semestre.getSabadosLetivos().addAll(sabadoLetivoDAO.listarPorSemestre(semestre));
    }

    public static void alternarSabadoLetivo(Semestre semestre, LocalDate data) throws SQLException {
        boolean existe = semestre.getSabadosLetivos().stream().anyMatch(s -> s.getData().equals(data));
        if (existe) {
            removerSabadoLetivo(semestre, data);
        } else {
            SabadoLetivo sl = new SabadoLetivo(semestre, data);
            sabadoLetivoDAO.salvar(sl);
            semestre.addSabadoLetivo(sl);
        }
    }

    public static void removerSabadoLetivo(Semestre semestre, LocalDate data) throws SQLException {
        sabadoLetivoDAO.excluir(semestre.getNome(), data);
        semestre.getSabadosLetivos().removeIf(s -> s.getData().equals(data));
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

    public static void excluir(Semestre s) throws SQLException {
        if (s != null && s.getNome() != null) {
            semestreDAO.excluir(s.getNome());
        }
    }
}