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

public class GradeSemanalController {

    @FXML private Spinner<Integer> spnSeg;
    @FXML private Spinner<Integer> spnTer;
    @FXML private Spinner<Integer> spnQua;
    @FXML private Spinner<Integer> spnQui;
    @FXML private Spinner<Integer> spnSex;

    private Disciplina disciplinaAtual;

    @FXML
    public void initialize() {
        configurarSpinner(spnSeg);
        configurarSpinner(spnTer);
        configurarSpinner(spnQua);
        configurarSpinner(spnQui);
        configurarSpinner(spnSex);

        // Resgata a disciplina selecionada pelo coordenador
        this.disciplinaAtual = DisciplinaService.getDisciplinaSelecionada();

        if (disciplinaAtual != null) {
            carregarGrade();
        } else {
            System.out.println("Aviso: Nenhuma disciplina foi pré-selecionada no DisciplinaService.");
        }
    }

    private void configurarSpinner(Spinner<Integer> spinner) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 0));
        spinner.setEditable(true);
    }

    private void carregarGrade() {
        if (disciplinaAtual == null) return;

        // Reinicia os spinners zerados por segurança antes de popular
        spnSeg.getValueFactory().setValue(0);
        spnTer.getValueFactory().setValue(0);
        spnQua.getValueFactory().setValue(0);
        spnQui.getValueFactory().setValue(0);
        spnSex.getValueFactory().setValue(0);

        // Alimenta a tela usando a lista interna carregada do banco pelo DisciplinaDAO
        if (disciplinaAtual.getAulasPorDia() != null) {
            for (AulasPorDia aula : disciplinaAtual.getAulasPorDia()) {
                switch (aula.getDiaSemana()) {
                    case MONDAY:
                        spnSeg.getValueFactory().setValue(aula.getQuantidadeAulas());
                        break;
                    case TUESDAY:
                        spnTer.getValueFactory().setValue(aula.getQuantidadeAulas());
                        break;
                    case WEDNESDAY:
                        spnQua.getValueFactory().setValue(aula.getQuantidadeAulas());
                        break;
                    case THURSDAY:
                        spnQui.getValueFactory().setValue(aula.getQuantidadeAulas());
                        break;
                    case FRIDAY:
                        spnSex.getValueFactory().setValue(aula.getQuantidadeAulas());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void setDisciplina(Disciplina d) {
        this.disciplinaAtual = d;
        carregarGrade();
    }

    @FXML
    private void volverTela() {
        Main.loadView("CoordenadorListaDisciplinasEX.fxml");
    }

    @FXML
    private void voltarTela() {
        Main.loadView("CoordenadorListaDisciplinasEX.fxml");
    }

    @FXML
    private void salvarGrade() {
        if (disciplinaAtual == null) {
            System.err.println("ERRO: Não é possível salvar a grade porque nenhuma disciplina está ativa.");
            return;
        }

        GradeDAO gradeDAO = new GradeDAO();

        int seg = spnSeg.getValue();
        int ter = spnTer.getValue();
        int qua = spnQua.getValue();
        int qui = spnQui.getValue();
        int sex = spnSex.getValue();

        try {
            // Limpa o histórico antigo na tabela temporária aula_por_dia
            gradeDAO.deletarPorDisciplina(disciplinaAtual.getNome());

            // Grava as novas quantidades configuradas nos Spinners
            if (seg > 0) gradeDAO.salvarGrade(new AulasPorDia(disciplinaAtual, DayOfWeek.MONDAY,    seg));
            if (ter > 0) gradeDAO.salvarGrade(new AulasPorDia(disciplinaAtual, DayOfWeek.TUESDAY,   ter));
            if (qua > 0) gradeDAO.salvarGrade(new AulasPorDia(disciplinaAtual, DayOfWeek.WEDNESDAY, qua));
            if (qui > 0) gradeDAO.salvarGrade(new AulasPorDia(disciplinaAtual, DayOfWeek.THURSDAY,  qui));
            if (sex > 0) gradeDAO.salvarGrade(new AulasPorDia(disciplinaAtual, DayOfWeek.FRIDAY,    sex));

            voltarTela();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERRO: Ocorreu uma exceção ao salvar no banco de dados.");
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