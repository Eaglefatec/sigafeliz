package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.Semestre;
import com.sigafeliz.service.SemestreService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.time.DayOfWeek; // Import necessário
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit; // Import para cálculo de dias

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

        // REGRA: Bloquear Kickoff até que início e fim sejam preenchidos
        dpKickoff.setDisable(true);
        dpInicio.valueProperty().addListener((obs, oldV, newVal) -> validarHabilitacaoKickoff());
        dpFim.valueProperty().addListener((obs, oldV, newVal) -> validarHabilitacaoKickoff());

        listaSemestres = SemestreService.getAllSemestres();
        tabelaSemestres.setItems(listaSemestres);
    }

    private void validarHabilitacaoKickoff() {
        dpKickoff.setDisable(dpInicio.getValue() == null || dpFim.getValue() == null);
    }

    private void configurarColunaAcoes() {
        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("EDITAR");
            private final Button btnExcluir = new Button("EXCLUIR");
            private final HBox painelBotoes = new HBox(10, btnEditar, btnExcluir);

            {
                painelBotoes.setAlignment(Pos.CENTER);
                btnEditar.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2; -fx-cursor: hand; -fx-font-family: 'Monospaced'; -fx-font-weight: bold;");
                btnEditar.setOnAction(event -> {
                    Semestre s = getTableView().getItems().get(getIndex());
                    SemestreService.setSemestreSelecionado(s);
                    Main.loadView("SemestreEdicao.fxml");
                });

                btnExcluir.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2; -fx-text-fill: #b30000; -fx-cursor: hand; -fx-font-family: 'Monospaced'; -fx-font-weight: bold;");
                btnExcluir.setOnAction(event -> {
                    Semestre s = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Deseja excluir o semestre " + s.getNome() + "?", ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(res -> {
                        if (res == ButtonType.YES) {
                            try {
                                SemestreService.excluir(s);
                                listaSemestres.remove(s);
                            } catch (SQLException e) {
                                mostrarAlerta("Erro", "Erro ao excluir: " + e.getMessage(), Alert.AlertType.ERROR);
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : painelBotoes);
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
            mostrarAlerta("Aviso", "Preencha todos os campos antes de criar o semestre.", Alert.AlertType.WARNING);
            return;
        }

        // --- VALIDAÇÕES DE REGRAS DE NEGÓCIO ---

        // 1. Duração do semestre: Min 110, Max 200 dias
        long duracaoSemestre = ChronoUnit.DAYS.between(inicio, fim);
        if (duracaoSemestre < 110 || duracaoSemestre > 200) {
            mostrarAlerta("Regra de Datas", "O semestre deve ter entre 110 e 200 dias (atual: " + duracaoSemestre + ").", Alert.AlertType.ERROR);
            return;
        }

        // 2. Kickoff deve ser posterior ao início
        if (!kickoff.isAfter(inicio)) {
            mostrarAlerta("Regra de Kickoff", "A data de Kickoff deve ser posterior ao início do semestre.", Alert.AlertType.ERROR);
            return;
        }

        // 3. Kickoff obrigatoriamente na Segunda-Feira
        if (kickoff.getDayOfWeek() != DayOfWeek.MONDAY) {
            mostrarAlerta("Regra de Kickoff", "O Kickoff deve ser necessariamente em uma segunda-feira.", Alert.AlertType.ERROR);
            return;
        }

        // 4. Kickoff deve permitir o ciclo de Sprints (Mínimo 98 dias antes do fim)
        long diasParaOFim = ChronoUnit.DAYS.between(kickoff, fim);
        if (diasParaOFim < 98) {
            mostrarAlerta("Regra de Kickoff", "Deve haver no mínimo 98 dias entre o Kickoff e o fim do semestre (atual: " + diasParaOFim + ").", Alert.AlertType.ERROR);
            return;
        }

        Semestre novoSemestre = new Semestre(nome, inicio, fim, kickoff);

        try {
            SemestreService.salvar(novoSemestre);
            listaSemestres.add(novoSemestre);
            limparCampos();
        } catch (SQLException e) {
            mostrarAlerta("Erro ao salvar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void limparCampos() {
        txtNome.clear();
        dpInicio.setValue(null);
        dpFim.setValue(null);
        dpKickoff.setValue(null);
        dpKickoff.setDisable(true);
    }

    @FXML
    private void handleVoltar() {
        Main.loadView("TelaInicial.fxml");
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}