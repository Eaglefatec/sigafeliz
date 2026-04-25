package com.sigafeliz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

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
}