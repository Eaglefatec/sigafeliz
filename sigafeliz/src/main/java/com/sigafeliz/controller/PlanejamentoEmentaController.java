package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Prioridade;
import com.sigafeliz.model.Tema;
import com.sigafeliz.service.MockDataService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class PlanejamentoEmentaController {

    @FXML private Label lblTituloDisciplina;
    @FXML private Label lblMetaAulas;
    @FXML private Label lblSomaNoBox;
    @FXML private Label lblSomaAtualBarra;
    @FXML private VBox boxSprintError;
    @FXML private HBox boxInlineAdd;

    @FXML private TableView<Tema> tabelaTemas;
    @FXML private TableColumn<Tema, Integer> colOrdem;
    @FXML private TableColumn<Tema, String> colTitulo;
    @FXML private TableColumn<Tema, Integer> colMin;
    @FXML private TableColumn<Tema, Integer> colMax;
    @FXML private TableColumn<Tema, Prioridade> colPrioridade;
    @FXML private TableColumn<Tema, Boolean> colProva;
    @FXML private TableColumn<Tema, Void> colAcoes;

    @FXML private TextField txtOrd;
    @FXML private TextField txtTema;
    @FXML private TextField txtMin;
    @FXML private TextField txtMax;
    @FXML private ComboBox<Prioridade> cbPrioridade;
    @FXML private CheckBox chkProva;

    private Disciplina disciplinaAtual;

    @FXML
    public void initialize() {
        disciplinaAtual = MockDataService.getDisciplinaSelecionada();

        if (disciplinaAtual != null) {
            lblTituloDisciplina.setText("PLANEJAMENTO: " + disciplinaAtual.getNome().toUpperCase());
            lblMetaAulas.setText("Meta: " + disciplinaAtual.getCargaHorariaTotal() + " Aulas");

            cbPrioridade.setItems(FXCollections.observableArrayList(Prioridade.values()));
            cbPrioridade.setValue(Prioridade.MEDIA);

            configurarColunas();
            atualizarTabelaESomas();
        }
    }

    private void configurarColunas() {
        colOrdem.setCellValueFactory(new PropertyValueFactory<>("ordem"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colMin.setCellValueFactory(new PropertyValueFactory<>("cargaMinima"));
        colMax.setCellValueFactory(new PropertyValueFactory<>("cargaMaxima"));
        colPrioridade.setCellValueFactory(new PropertyValueFactory<>("prioridade"));

        colProva.setCellFactory(param -> new TableCell<Tema, Boolean>() {
            private final CheckBox checkBox = new CheckBox();
            { checkBox.setDisable(true); }
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item);
                    setGraphic(checkBox);
                    setAlignment(Pos.CENTER);
                }
            }
        });
        colProva.setCellValueFactory(new PropertyValueFactory<>("eAvaliacao"));

        colAcoes.setCellFactory(param -> new TableCell<Tema, Void>() {
            private final Button btnEdit = new Button("✎");
            {
                btnEdit.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 14px;");
                btnEdit.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("A edição direta será implementada na próxima Sprint.");
                    alert.showAndWait();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    setGraphic(btnEdit);
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }

    private void atualizarTabelaESomas() {
        List<Tema> temas = disciplinaAtual.getTemas();
        tabelaTemas.setItems(FXCollections.observableArrayList(temas));

        int somaMin = temas.stream().mapToInt(Tema::getCargaMinima).sum();
        int somaMax = temas.stream().mapToInt(Tema::getCargaMaxima).sum();
        String textoSoma = String.format("Soma Mín: %d | Máx: %d", somaMin, somaMax);

        lblSomaNoBox.setText(textoSoma);
        lblSomaAtualBarra.setText(textoSoma);
    }

    @FXML
    private void handleExibirInlineAdd() {
        boxInlineAdd.setVisible(true);
        boxInlineAdd.setManaged(true);
        txtOrd.setText(String.valueOf(disciplinaAtual.getTemas().size() + 1));
        txtTema.requestFocus();
    }

    @FXML
    private void handleSalvarInline() {
        try {
            int min = Integer.parseInt(txtMin.getText());
            int max = Integer.parseInt(txtMax.getText());
            int ord = Integer.parseInt(txtOrd.getText());

            Tema novoTema = new Tema(null, txtTema.getText(), min, max, cbPrioridade.getValue(), chkProva.isSelected(), ord);
            disciplinaAtual.addTema(novoTema);

            txtTema.clear();
            txtMin.clear();
            txtMax.clear();
            chkProva.setSelected(false);
            cbPrioridade.setValue(Prioridade.MEDIA);

            boxInlineAdd.setVisible(false);
            boxInlineAdd.setManaged(false);

            atualizarTabelaESomas();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Preencha Min e Max com valores numéricos.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleExportar() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Salvar Arquivo Como...");
        alert.setHeaderText("Simulação do FileChooser do Sistema Operacional");
        alert.setContentText("Arquivo exportado para C:\\Users\\Professor\\Documents\\SigaFeliz.xlsx");
        alert.showAndWait();
    }

    @FXML
    private void handleFinalizar() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Salvo no banco de dados. Processo concluído.");
        alert.showAndWait();
    }

    @FXML
    private void handleVoltar() {
        Main.loadView("MinhasDisciplinas.fxml");
    }
}