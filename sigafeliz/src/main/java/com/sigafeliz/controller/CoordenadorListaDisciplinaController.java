package com.sigafeliz.controller;

import com.sigafeliz.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CoordenadorListaDisciplinaController {

    @FXML
    private Button btnAvancar;

    @FXML
    private Button btnEditarGrade;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnVoltar;

    @FXML
    private ComboBox<?> cbProfessor;

    @FXML
    private TextField txtDisciplina;

    @FXML
    void abrirEditarGrade(ActionEvent event) {
        Main.loadView("GradeSemanal.fxml");
    }

    @FXML
    void avancarSemestreEdicao(ActionEvent event) {
        Main.loadView("SemestreLista.fxml");
    }

    @FXML
    void salvarDisciplina(ActionEvent event) {

    }

    @FXML
    void voltarProfessorLista(ActionEvent event) {
        Main.loadView("ProfessoresLista.fxml");
    }

}
