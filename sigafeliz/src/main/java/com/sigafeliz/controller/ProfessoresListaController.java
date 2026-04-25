package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.Professor;
import javafx.collections.FXCollections;
import com.sigafeliz.service.ProfessorService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProfessoresListaController {

    @FXML private TableView<Professor> tabelaProfessores;
    @FXML private TableColumn<Professor, String> colNome;
    @FXML private TableColumn<Professor, String> colEmail;
    @FXML private TableColumn<Professor, Void> colSelec;

    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;

    private final ToggleGroup professorGroup = new ToggleGroup();
    private final ObservableList<Professor> listaProfessores = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        configurarColunaSelecao();

        listaProfessores.addAll(ProfessorService.getAllProfessores());

        tabelaProfessores.setItems(listaProfessores);
    }

    private void configurarColunaSelecao() {
        // Uso do <> (Diamond Operator) para limpar o alerta
        colSelec.setCellFactory(param -> new TableCell<>() {
            private final RadioButton rb = new RadioButton();
            {
                rb.setToggleGroup(professorGroup);
                // Lambda simplificada (Expression Lambda)
                rb.setOnAction(event -> getTableView().getSelectionModel().select(getIndex()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(rb);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }

    @FXML
    private void salvarProfessor() {
        String nome = txtNome.getText();
        String email = txtEmail.getText();

        if (nome == null || nome.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Preencha o nome e o e-mail!");
            alert.show();
            return;
        }

        listaProfessores.add(new Professor(nome, email));
        txtNome.clear();
        txtEmail.clear();
        txtNome.requestFocus();
    }

    @FXML
    private void irParaProximaTela() {
        Professor selecionado = tabelaProfessores.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            System.out.println("Professor selecionado: " + selecionado.getNome());
        } else {
            exibirAlerta("Por favor, selecione um professor na lista.");
        }
    }

    @FXML
    private void voltarParaHome() {
        Main.loadView("TelaInicial.fxml");
    }

    private void exibirAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}