package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.Professor;
import com.sigafeliz.service.ProfessorService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

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
    private ComboBox<Integer> comboCargaHoraria;

    @FXML
    private ComboBox<Professor> comboProfessores;

    @FXML
    private TextField txtDisciplina;

    // MÉTODO INICIAL (carrega dados)
    @FXML
    public void initialize() {
        carregarProfessores();
        carregarCargaHoraria();
    }

    private void carregarProfessores() {
        // Carrega a lista de professores do mongo para o ComboBox
        comboProfessores.setItems(FXCollections.observableArrayList(ProfessorService.getAllProfessores()));
        // Configura como o Professor será exibido no ComboBox (apenas o nome)
        comboProfessores.setConverter(new StringConverter<Professor>() {
            @Override
            public String toString(Professor p) {
                return p == null ? "" : p.getNome();
            }
            @Override
            public Professor fromString(String string) {
                return null;
            }
        });

    }

    private void carregarCargaHoraria() {
        comboCargaHoraria.getItems().clear();
        comboCargaHoraria.getItems().addAll(40, 80);
    }

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
