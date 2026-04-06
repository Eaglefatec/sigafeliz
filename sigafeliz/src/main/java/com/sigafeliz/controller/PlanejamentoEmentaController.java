package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Tema;
import com.sigafeliz.service.MockDataService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PlanejamentoEmentaController {

    @FXML private Label lblTituloDisciplina;
    @FXML private Label lblMetaAulas;
    @FXML private TableView<Tema> tabelaTemas;
    @FXML private TableColumn<Tema, Integer> colOrdem;
    @FXML private TableColumn<Tema, String> colTitulo;
    @FXML private TableColumn<Tema, Integer> colMin;
    @FXML private TableColumn<Tema, Integer> colMax;
    @FXML private TableColumn<Tema, String> colPrioridade;
    @FXML private TableColumn<Tema, Boolean> colProva;

    @FXML
    public void initialize() {
        Disciplina selecionada = MockDataService.getDisciplinaSelecionada();

        if (selecionada != null) {
            lblTituloDisciplina.setText("PLANEJAMENTO: " + selecionada.getNome().toUpperCase());
            lblMetaAulas.setText("Meta: " + selecionada.getCargaHorariaTotal() + " Aulas");

            // Mapeamento das colunas para os atributos da classe Tema
            colOrdem.setCellValueFactory(new PropertyValueFactory<>("ordem"));
            colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            colMin.setCellValueFactory(new PropertyValueFactory<>("cargaMinima"));
            colMax.setCellValueFactory(new PropertyValueFactory<>("cargaMaxima"));
            colPrioridade.setCellValueFactory(new PropertyValueFactory<>("prioridade"));
            colProva.setCellValueFactory(new PropertyValueFactory<>("eAvaliacao"));

            tabelaTemas.setItems(FXCollections.observableArrayList(selecionada.getTemas()));
        }
    }

    @FXML
    private void handleExportar() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exportação");
        alert.setHeaderText("Simulação de US09");
        alert.setContentText("Arquivo Excel gerado com sucesso em: C:\\Users\\Public\\Documentos\\SigaFeliz.xlsx");
        alert.showAndWait();
    }

    @FXML
    private void handleFinalizar() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Planejamento salvo no banco de dados e concluído.");
        alert.showAndWait();
        Main.loadView("TelaInicial.fxml");
    }

    @FXML
    private void handleVoltar() {
        Main.loadView("MinhasDisciplinas.fxml");
    }
}