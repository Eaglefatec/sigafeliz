package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.Professor;
import com.sigafeliz.service.MockDataService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class SelecaoProfessorController {

    @FXML
    private ComboBox<Professor> comboProfessores;

    // Chamado quando o controller do fxml atrelado a ele é carregado pelo JavaFX.
    @FXML
    public void initialize() {
        // Carrega a lista do MockDataService para o ComboBox
        comboProfessores.setItems(FXCollections.observableArrayList(MockDataService.getAllProfessores()));
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

    @FXML
    private void handleAcessar() {
        Professor selecionado = comboProfessores.getValue();

        if (selecionado != null) {
            MockDataService.setProfessorLogado(selecionado);
            Main.loadView("MinhasDisciplinas.fxml");
        } else {
            exibirAlerta("Selecione um professor para continuar.");
        }
    }

    @FXML
    private void handleVoltar() {
        Main.loadView("TelaInicial.fxml");
    }

    private void exibirAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}