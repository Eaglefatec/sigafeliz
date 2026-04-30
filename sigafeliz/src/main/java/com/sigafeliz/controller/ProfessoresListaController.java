package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.Professor;
import com.sigafeliz.service.ProfessorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;

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
        colSelec.setCellFactory(param -> new TableCell<>() {
            private final RadioButton rb = new RadioButton();
            {
                rb.setToggleGroup(professorGroup);
                rb.setOnAction(event -> {
                    Professor p = getTableView().getItems().get(getIndex());
                    ProfessorService.setProfessorLogado(p);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(rb);
                    setStyle("-fx-alignment: CENTER;");
                    Professor p = getTableView().getItems().get(getIndex());
                    rb.setSelected(p.equals(ProfessorService.getProfessorLogado()));
                }
            }
        });
    }

    @FXML
    private void salvarProfessor() {
        String nome = txtNome.getText();
        String email = txtEmail.getText();

        if (!ProfessorService.validarCampos(nome, email)) {
            exibirAlerta("Dados inválidos! Verifique o nome e o e-mail.");
            return;
        }

        try {
            Professor novo = new Professor(nome, email);
            ProfessorService.salvar(novo);
            listaProfessores.add(novo);
            txtNome.clear();
            txtEmail.clear();
        } catch (SQLException e) {
            exibirAlerta(e.getMessage());
        }

        Professor selecionado = ProfessorService.getProfessorLogado();
        if (selecionado != null) {
            try {
                selecionado.setNome(txtNome.getText());
                ProfessorService.atualizar(selecionado);
                tabelaProfessores.refresh();
            } catch (SQLException e) {
                exibirAlerta("Erro ao atualizar: " + e.getMessage());
            }
        } else {
            exibirAlerta("Selecione um professor e digite o novo nome para editar.");
        }
    }


    @FXML
    private void excluirProfessor() {
        Professor selecionado = ProfessorService.getProfessorLogado();
        if (selecionado != null) {
            try {
                ProfessorService.excluir(selecionado);
                listaProfessores.remove(selecionado);
                ProfessorService.setProfessorLogado(null);
            } catch (SQLException e) {
                exibirAlerta("Erro ao excluir: " + e.getMessage());
            }
        } else {
            exibirAlerta("Selecione um professor na lista para excluir.");
        }
    }

    @FXML
    private void irParaProximaTela() {

        Professor selecionado = ProfessorService.getProfessorLogado();
        if (selecionado == null) {
            exibirAlerta("Por favor, selecione um professor.");
        } else {
            Main.loadView("CoordenadorListaDisciplinas.fxml");
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