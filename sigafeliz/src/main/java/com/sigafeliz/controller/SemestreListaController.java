package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.Semestre;
import com.sigafeliz.service.SemestreService; // ← Import alterado
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SemestreListaController {

    @FXML private TextField txtNome;
    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFim;
    @FXML private DatePicker dpKickoff;

    @FXML private TableView<Semestre> tabelaSemestres;
    @FXML private TableColumn<Semestre, String> colNome;
    @FXML private TableColumn<Semestre, String> colInicio;
    @FXML private TableColumn<Semestre, String> colFim;
    @FXML private TableColumn<Semestre, String> colKickoff;
    @FXML private TableColumn<Semestre, Void> colAcoes;

    private ObservableList<Semestre> listaSemestres;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        colInicio.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDataInicio() != null ? cellData.getValue().getDataInicio().format(formatter) : ""
        ));
        colFim.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDataFim() != null ? cellData.getValue().getDataFim().format(formatter) : ""
        ));
        colKickoff.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDataKickoff() != null ? cellData.getValue().getDataKickoff().format(formatter) : ""
        ));

        configurarColunaAcoes();

        // ↓ Busca dados reais do banco usando o novo Service
        listaSemestres = SemestreService.getAllSemestres();
        tabelaSemestres.setItems(listaSemestres);
    }

    private void configurarColunaAcoes() {
        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("EDITAR");
            {
                btnEditar.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2; -fx-cursor: hand; -fx-font-family: 'Monospaced'; -fx-font-weight: bold;");
                btnEditar.setOnAction(event -> {
                    Semestre s = getTableView().getItems().get(getIndex());
                    // ↓ Salva a sessão no Service correto
                    SemestreService.setSemestreSelecionado(s);
                    Main.loadView("SemestreEdicao.fxml");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    setGraphic(btnEditar);
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }

    @FXML
    private void handleCriarSemestre() {
        String nome = txtNome.getText();
        LocalDate inicio = dpInicio.getValue();
        LocalDate fim = dpFim.getValue();
        LocalDate kickoff = dpKickoff.getValue();

        if (nome == null || nome.trim().isEmpty() || inicio == null || fim == null || kickoff == null) {
            mostrarAlerta("Aviso", "Preencha todos os campos e selecione todas as datas para criar o semestre.", Alert.AlertType.WARNING);
            return;
        }

        Semestre novoSemestre = new Semestre(nome, inicio, fim, kickoff);

        try {
            // ↓ Insere no PostgreSQL através do Service
            SemestreService.salvar(novoSemestre);

            // Se o banco aceitou, atualiza a tabela da tela
            listaSemestres.add(novoSemestre);

            // Limpa os campos
            txtNome.clear();
            dpInicio.setValue(null);
            dpFim.setValue(null);
            dpKickoff.setValue(null);

        } catch (SQLException e) {
            // Exibe o erro vindo do banco/validação (Ex: UNIQUE violation)
            mostrarAlerta("Erro ao salvar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleVoltar() {
        Main.loadView("TelaInicial.fxml");
    }

    // Método utilitário limpo para gerar alertas
    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}