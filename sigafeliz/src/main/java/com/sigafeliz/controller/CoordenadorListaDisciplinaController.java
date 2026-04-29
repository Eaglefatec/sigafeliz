package br.com.exemplo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CoordenadorListaController {

    // CAMPOS DA LINHA DE INSERÇÃO
    @FXML
    private TextField txtDisciplina;

    @FXML
    private ComboBox<String> cbProfessor;

    @FXML
    private ComboBox<Integer> cbCargaHoraria;

    @FXML
    private Button btnSalvar;

    // MÉTODO INICIAL (carrega dados)
    @FXML
    public void initialize() {
        carregarProfessores();
        carregarCargaHoraria();
    }

    private void carregarProfessores() {
        cbProfessor.getItems().clear();
        cbProfessor.getItems().addAll(
                "Prof. Carlos Silva",
                "Prof. João Pereira",
                "Prof. Ana Souza"
        );
    }

    private void carregarCargaHoraria() {
        cbCargaHoraria.getItems().clear();
        cbCargaHoraria.getItems().addAll(40, 80);
    }

    // AÇÃO DO BOTÃO SALVAR
    @FXML
    private void salvarDisciplina() {

        String disciplina = txtDisciplina.getText();
        String professor = cbProfessor.getValue();
        Integer carga = cbCargaHoraria.getValue();

        // 🔎 VALIDAÇÃO MELHORADA
        if (disciplina == null || disciplina.trim().isEmpty()
                || professor == null
                || carga == null) {

            mostrarAlerta("Preencha todos os campos!");
            return;
        }

        // 🎯 SIMULAÇÃO DE SALVAMENTO
        System.out.println("Disciplina: " + disciplina.trim());
        System.out.println("Professor: " + professor);
        System.out.println("Carga: " + carga);

        mostrarAlerta("Disciplina salva com sucesso!");

        limparCampos();
    }

    // LIMPA FORMULÁRIO
    private void limparCampos() {
        txtDisciplina.clear();
        cbProfessor.getSelectionModel().clearSelection();
        cbCargaHoraria.getSelectionModel().clearSelection();

        // Opcional: foco volta para o primeiro campo
        txtDisciplina.requestFocus();
    }

    // ALERTA PADRÃO
    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
