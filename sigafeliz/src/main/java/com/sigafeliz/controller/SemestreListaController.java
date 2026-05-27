package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.DiaRestrito;
import com.sigafeliz.model.SabadoLetivo;
import com.sigafeliz.model.Semestre;
import com.sigafeliz.service.SemestreService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class SemestreListaController {

    @FXML private TextField txtNome;
    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFim;
    @FXML private DatePicker dpKickoff;

    @FXML private TableView<Semestre> tabelaSemestres;
    @FXML private TableColumn<Semestre, String> colNome;

    @FXML private TableColumn<Semestre, LocalDate> colInicio;
    @FXML private TableColumn<Semestre, LocalDate> colFim;
    @FXML private TableColumn<Semestre, LocalDate> colKickoff;

    @FXML private TableColumn<Semestre, Void> colAcoes;

    private ObservableList<Semestre> listaSemestres;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        tabelaSemestres.setEditable(true);

        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        colInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        colFim.setCellValueFactory(new PropertyValueFactory<>("dataFim"));
        colKickoff.setCellValueFactory(new PropertyValueFactory<>("dataKickoff"));

        configurarColunaEdicaoData(colInicio, "inicio");
        configurarColunaEdicaoData(colFim, "fim");
        configurarColunaEdicaoData(colKickoff, "kickoff");

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

    // --- LOGICA PARA CÉLULAS DE DATA EDITÁVEIS NA TABLEVIEW ---
    private void configurarColunaEdicaoData(TableColumn<Semestre, LocalDate> coluna, String campo) {
        coluna.setCellFactory(col -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();
            {
                datePicker.setConverter(new javafx.util.StringConverter<LocalDate>() {
                    @Override
                    public String toString(LocalDate date) {
                        return date != null ? formatter.format(date) : "";
                    }
                    @Override
                    public LocalDate fromString(String string) {
                        return string != null && !string.isEmpty() ? LocalDate.parse(string, formatter) : null;
                    }
                });
                datePicker.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-font-family: 'Monospaced';");

                datePicker.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused && isEditing()) {
                        cancelEdit();
                    }
                });

                datePicker.setOnAction(event -> {
                    if (isEditing()) {
                        commitEdit(datePicker.getValue());
                    }
                });
            }

            @Override
            public void startEdit() {
                super.startEdit();
                if (isEmpty()) return;
                datePicker.setValue(getItem());
                setGraphic(datePicker);
                setText(null);
                setTooltip(null);
                datePicker.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setGraphic(null);
                setText(getItem() != null ? formatter.format(getItem()) : "");
                setTooltip(new Tooltip("Dê um duplo clique para editar esta data"));
            }

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                    setTooltip(null);
                } else {
                    if (isEditing()) {
                        datePicker.setValue(item);
                        setGraphic(datePicker);
                        setText(null);
                        setTooltip(null);
                    } else {
                        setGraphic(null);
                        setText(formatter.format(item));
                        // UX: Adiciona tooltip orientando edição ao passar o mouse
                        setTooltip(new Tooltip("Dê um duplo clique para editar esta data"));
                    }
                }
            }
        });

        coluna.setOnEditCommit(event -> {
            Semestre semestre = event.getRowValue();
            LocalDate newValue = event.getNewValue();
            LocalDate oldValue = event.getOldValue();
            if (newValue == null || newValue.equals(oldValue)) return;

            processarEdicaoData(semestre, campo, newValue, oldValue);
        });
    }

    private void processarEdicaoData(Semestre semestre, String campo, LocalDate newValue, LocalDate oldValue) {
        // Guarda backups de memória caso precisemos reverter a ação
        LocalDate backupInicio = semestre.getDataInicio();
        LocalDate backupFim = semestre.getDataFim();
        LocalDate backupKickoff = semestre.getDataKickoff();

        // Aplica o novo valor temporariamente no Objeto em memória
        if (campo.equals("inicio")) semestre.setDataInicio(newValue);
        else if (campo.equals("fim")) semestre.setDataFim(newValue);
        else if (campo.equals("kickoff")) semestre.setDataKickoff(newValue);

        try {
            // 1. Validação Regras Negociais com as Novas Datas
            long duracao = ChronoUnit.DAYS.between(semestre.getDataInicio(), semestre.getDataFim());
            if (duracao < 110 || duracao > 200) throw new IllegalArgumentException("O semestre deve ter entre 110 e 200 dias de duração.");
            if (!semestre.getDataKickoff().isAfter(semestre.getDataInicio())) throw new IllegalArgumentException("O Kickoff deve ser posterior ao início do semestre.");
            if (semestre.getDataKickoff().getDayOfWeek() != DayOfWeek.MONDAY) throw new IllegalArgumentException("O Kickoff deve ser em uma segunda-feira.");
            if (ChronoUnit.DAYS.between(semestre.getDataKickoff(), semestre.getDataFim()) < 98) throw new IllegalArgumentException("Mínimo de 98 dias entre o Kickoff e o fim do semestre.");

            // 2. Validação Exclusão Feriados/Sábados Fora do Range
            if (campo.equals("inicio") || campo.equals("fim")) {
                SemestreService.carregarDetalhes(semestre);

                List<DiaRestrito> feriadosFora = semestre.getDiasRestritos().stream()
                        .filter(d -> d.getData().isBefore(semestre.getDataInicio()) || d.getData().isAfter(semestre.getDataFim()))
                        .toList();

                List<SabadoLetivo> sabadosFora = semestre.getSabadosLetivos().stream()
                        .filter(s -> s.getData().isBefore(semestre.getDataInicio()) || s.getData().isAfter(semestre.getDataFim()))
                        .toList();

                if (!feriadosFora.isEmpty() || !sabadosFora.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Existem datas restritas (feriados) ou sábados letivos que caem fora do novo período estabelecido. O sistema precisará excluí-los.\n\nDeseja confirmar e prosseguir?",
                            ButtonType.YES, ButtonType.NO);
                    alert.setHeaderText("Atenção: Ocorrências fora do limite");

                    alert.showAndWait().ifPresent(res -> {
                        if (res == ButtonType.YES) {
                            try {
                                // Exclui ocorrências órfãs e prossegue
                                for (DiaRestrito d : feriadosFora) SemestreService.removerDiaRestrito(semestre, d.getData());
                                for (SabadoLetivo s : sabadosFora) SemestreService.removerSabadoLetivo(semestre, s.getData());
                                SemestreService.atualizar(semestre);
                            } catch (SQLException e) {
                                mostrarAlerta("Erro", "Falha ao remover ocorrências e atualizar o DB: " + e.getMessage(), Alert.AlertType.ERROR);
                                reverterEdicao(semestre, backupInicio, backupFim, backupKickoff);
                            }
                        } else {
                            // Reverte
                            reverterEdicao(semestre, backupInicio, backupFim, backupKickoff);
                        }
                    });
                    tabelaSemestres.refresh();
                    return;
                }
            }

            // Se chegou aqui e não caiu no IF dos conflitos, apenas atualiza
            SemestreService.atualizar(semestre);

        } catch (Exception e) {
            mostrarAlerta("Erro de Validação", e.getMessage(), Alert.AlertType.ERROR);
            reverterEdicao(semestre, backupInicio, backupFim, backupKickoff);
        }

        tabelaSemestres.refresh(); // Força atualização visual
    }

    // --- MÉTODO REVERTER EDICAO RESTAURADO ---
    private void reverterEdicao(Semestre semestre, LocalDate inicio, LocalDate fim, LocalDate kickoff) {
        semestre.setDataInicio(inicio);
        semestre.setDataFim(fim);
        semestre.setDataKickoff(kickoff);
    }

    private void configurarColunaAcoes() {
        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnConfigurar = new Button("⚙ CONFIGURAR");
            private final Button btnExcluir = new Button("✖ EXCLUIR");
            private final HBox painelBotoes = new HBox(10, btnConfigurar, btnExcluir);

            {
                painelBotoes.setAlignment(Pos.CENTER);

                btnConfigurar.setStyle("-fx-background-color: #cfe2f3; -fx-border-color: #0b5394; -fx-text-fill: #0b5394; -fx-border-width: 2; -fx-cursor: hand; -fx-font-family: 'Monospaced'; -fx-font-weight: bold;");
                btnConfigurar.setTooltip(new Tooltip("Configurar Feriados e Sábados Letivos"));
                btnConfigurar.setOnAction(event -> {
                    Semestre s = getTableView().getItems().get(getIndex());
                    SemestreService.setSemestreSelecionado(s);
                    Main.loadView("SemestreEdicao.fxml");
                });

                btnExcluir.setStyle("-fx-background-color: #f8d7da; -fx-border-color: #b30000; -fx-border-width: 2; -fx-text-fill: #b30000; -fx-cursor: hand; -fx-font-family: 'Monospaced'; -fx-font-weight: bold;");
                btnExcluir.setTooltip(new Tooltip("Deletar este semestre"));
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

        long duracaoSemestre = ChronoUnit.DAYS.between(inicio, fim);
        if (duracaoSemestre < 110 || duracaoSemestre > 200) {
            mostrarAlerta("Regra de Datas", "O semestre deve ter entre 110 e 200 dias (atual: " + duracaoSemestre + ").", Alert.AlertType.ERROR);
            return;
        }

        if (!kickoff.isAfter(inicio)) {
            mostrarAlerta("Regra de Kickoff", "A data de Kickoff deve ser posterior ao início do semestre.", Alert.AlertType.ERROR);
            return;
        }

        if (kickoff.getDayOfWeek() != DayOfWeek.MONDAY) {
            mostrarAlerta("Regra de Kickoff", "O Kickoff deve ser necessariamente em uma segunda-feira.", Alert.AlertType.ERROR);
            return;
        }

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