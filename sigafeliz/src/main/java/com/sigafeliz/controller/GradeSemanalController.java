package com.sigafeliz.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
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

    @FXML
    public void initialize() {
        configurarSpinner(spnSeg);
        configurarSpinner(spnTer);
        configurarSpinner(spnQua);
        configurarSpinner(spnQui);
        configurarSpinner(spnSex);
    }

    private void configurarSpinner(Spinner<Integer> spinner) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        spinner.setEditable(true);
    }

    @FXML
    private void voltarParaHome() {
        Stage stage = (Stage) spnSeg.getScene().getWindow();
        stage.close();
    }

    public int getAulasSeg() { return spnSeg.getValue(); }
    public int getAulasTer() { return spnTer.getValue(); }
    public int getAulasQua() { return spnQua.getValue(); }
    public int getAulasQui() { return spnQui.getValue(); }
    public int getAulasSex() { return spnSex.getValue(); }

    public int[] getValoresDias() {
        return new int[]{
                spnSeg.getValue(),
                spnTer.getValue(),
                spnQua.getValue(),
                spnQui.getValue(),
                spnSex.getValue()
        };
    }


    private Disciplina disciplinaAtual;

    public void setDisciplina(Disciplina disciplina) {
        this.disciplinaAtual = disciplina;
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
            if (seg > 0) {
                gradeDAO.salvarGrade(
                        new AulasPorDia(disciplinaAtual, DayOfWeek.MONDAY, seg)
                );
            }
            if (ter > 0) {
                gradeDAO.salvarGrade(
                        new AulasPorDia(disciplinaAtual, DayOfWeek.TUESDAY, ter)
                );
            }
            if (qua > 0) {
                gradeDAO.salvarGrade(
                        new AulasPorDia(disciplinaAtual, DayOfWeek.WEDNESDAY, qua)
                );
            }
            if (qui > 0) {
                gradeDAO.salvarGrade(
                        new AulasPorDia(disciplinaAtual, DayOfWeek.THURSDAY, qui)
                );
            }
            if (sex > 0) {
                gradeDAO.salvarGrade(
                        new AulasPorDia(disciplinaAtual, DayOfWeek.FRIDAY, sex)
                );
            }

            // ✅ Depois de salvar tudo, fecha a tela
            voltarParaHome();

        } catch (SQLException e) {
            e.printStackTrace();
            // depois você pode mostrar um Alert JavaFX aqui
        }
    }



}