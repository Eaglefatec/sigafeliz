package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.service.DisciplinaService;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.dao.GradeDAO;
import com.sigafeliz.model.AulasPorDia;
import java.time.DayOfWeek;
import java.sql.SQLException;
import java.util.Map;

public class GradeSemanalController {

    @FXML private Spinner<Integer> spnSeg;
    @FXML private Spinner<Integer> spnTer;
    @FXML private Spinner<Integer> spnQua;
    @FXML private Spinner<Integer> spnQui;
    @FXML private Spinner<Integer> spnSex;

    private Disciplina disciplinaAtual = DisciplinaService.getDisciplinaSelecionada();

    @FXML
    public void initialize() {
        configurarSpinner(spnSeg);
        configurarSpinner(spnTer);
        configurarSpinner(spnQua);
        configurarSpinner(spnQui);
        configurarSpinner(spnSex);
        carregarGrade();
    }

    private void configurarSpinner(Spinner<Integer> spinner) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 0));
        spinner.setEditable(true);
    }

    private void carregarGrade() {
        GradeDAO gradeDAO = new GradeDAO();
        try {
            Map<DayOfWeek, Integer> grade = gradeDAO.buscarGradePorDisciplina(disciplinaAtual.getNome());

            spnSeg.getValueFactory().setValue(grade.getOrDefault(DayOfWeek.MONDAY,    0));
            spnTer.getValueFactory().setValue(grade.getOrDefault(DayOfWeek.TUESDAY,   0));
            spnQua.getValueFactory().setValue(grade.getOrDefault(DayOfWeek.WEDNESDAY, 0));
            spnQui.getValueFactory().setValue(grade.getOrDefault(DayOfWeek.THURSDAY,  0));
            spnSex.getValueFactory().setValue(grade.getOrDefault(DayOfWeek.FRIDAY,    0));

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERRO ao carregar grade do banco.");
        }
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplinaAtual = disciplina;
        carregarGrade(); // Recarrega se a disciplina for definida depois do initialize
    }

    @FXML
    private void voltarTela() {
        Main.loadView("CoordenadorListaDisciplinasEX.fxml");
    }

    @FXML
    private void salvarGrade() {
        GradeDAO gradeDAO = new GradeDAO();

        int seg = spnSeg.getValue();
        int ter = spnTer.getValue();
        int qua = spnQua.getValue();
        int qui = spnQui.getValue();
        int sex = spnSex.getValue();

        try {
            gradeDAO.deletarPorDisciplina(disciplinaAtual.getNome());

            if (seg > 0) gradeDAO.salvarGrade(new AulasPorDia(disciplinaAtual, DayOfWeek.MONDAY,    seg));
            if (ter > 0) gradeDAO.salvarGrade(new AulasPorDia(disciplinaAtual, DayOfWeek.TUESDAY,   ter));
            if (qua > 0) gradeDAO.salvarGrade(new AulasPorDia(disciplinaAtual, DayOfWeek.WEDNESDAY, qua));
            if (qui > 0) gradeDAO.salvarGrade(new AulasPorDia(disciplinaAtual, DayOfWeek.THURSDAY,  qui));
            if (sex > 0) gradeDAO.salvarGrade(new AulasPorDia(disciplinaAtual, DayOfWeek.FRIDAY,    sex));

            voltarTela();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERRO: não voltou por causa desse exception");
        }
    }

    public int[] getValoresDias() {
        return new int[]{
                spnSeg.getValue(),
                spnTer.getValue(),
                spnQua.getValue(),
                spnQui.getValue(),
                spnSex.getValue()
        };
    }
}