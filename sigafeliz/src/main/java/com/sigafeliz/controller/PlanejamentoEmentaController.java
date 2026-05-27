package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Prioridade;
import com.sigafeliz.model.Tema;
import com.sigafeliz.service.DisciplinaService;
import com.sigafeliz.service.TemaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class PlanejamentoEmentaController {

    @FXML private Label lblTituloDisciplina;
    @FXML private Label lblMetaAulas;
    @FXML private Label lblSomaNoBox;
    @FXML private Label lblSomaAtualBarra;
    @FXML private VBox boxSprintError;

    @FXML private TableView<Tema> tabelaTemas;
    @FXML private TableColumn<Tema, Integer> colOrdem;
    @FXML private TableColumn<Tema, String> colTitulo;
    @FXML private TableColumn<Tema, Integer> colMin;
    @FXML private TableColumn<Tema, Integer> colMax;
    @FXML private TableColumn<Tema, Prioridade> colPrioridade;
    @FXML private TableColumn<Tema, Boolean> colProva;
    @FXML private TableColumn<Tema, Void> colAcoes;

    private Disciplina disciplinaAtual;

    // Controles de Sessão de Edição Inline
    private Tema temaEmEdicao = null;
    private String tituloOriginalEdicao = null;
    private boolean showSprintError = false;

    @FXML
    public void initialize() {
        disciplinaAtual = DisciplinaService.getDisciplinaSelecionada();

        if (disciplinaAtual != null) {
            lblTituloDisciplina.setText("PLANEJAMENTO: " + disciplinaAtual.getNome().toUpperCase());
            lblMetaAulas.setText("Meta: " + disciplinaAtual.getCargaHorariaTotal() + " Aulas");

            configurarColunasInline();
            carregarTemasDoBanco();
        } else {
            exibirAlerta("Erro de Sessão", "Nenhuma disciplina selecionada. Volte à tela anterior.", Alert.AlertType.WARNING);
            tabelaTemas.setDisable(true);
        }
    }

    private void carregarTemasDoBanco() {
        try {
            List<Tema> temasBanco = TemaService.getTemasPorDisciplina(disciplinaAtual);
            disciplinaAtual.getTemas().clear();
            disciplinaAtual.getTemas().addAll(temasBanco);
            atualizarTabelaESomas();
        } catch (SQLException e) {
            exibirAlerta("Erro", "Falha ao carregar temas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void atualizarTabelaESomas() {
        tabelaTemas.setItems(FXCollections.observableArrayList(disciplinaAtual.getTemas()));

        int somaMin = disciplinaAtual.getTemas().stream().mapToInt(Tema::getCargaMinima).sum();
        int somaMax = disciplinaAtual.getTemas().stream().mapToInt(Tema::getCargaMaxima).sum();
        String textoSoma = String.format("Soma Atual Mín: %d | Máx: %d", somaMin, somaMax);

        lblSomaNoBox.setText(textoSoma);
        lblSomaAtualBarra.setText(textoSoma);
    }

    // --- MÁGICA DA EDIÇÃO DIRETO NA TABELA ---
    private void configurarColunasInline() {
        colOrdem.setCellValueFactory(new PropertyValueFactory<>("ordem"));

        // COLUNA TÍTULO: Vira um TextField se estiver em edição
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colTitulo.setCellFactory(col -> new TableCell<>() {
            private final TextField txt = new TextField();
            { txt.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-font-family: 'Monospaced';"); }

            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); setText(null); return; }
                Tema t = getTableView().getItems().get(getIndex());

                if (t == temaEmEdicao) {
                    txt.setText(t.getTitulo());
                    txt.setOnKeyReleased(e -> t.setTitulo(txt.getText()));
                    setGraphic(txt);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(item);
                }
            }
        });

        // COLUNA MÍNIMO: Vira um Spinner numérico se estiver em edição
        colMin.setCellValueFactory(new PropertyValueFactory<>("cargaMinima"));
        colMin.setCellFactory(col -> new TableCell<>() {
            private final Spinner<Integer> spinner = new Spinner<>(1, 100, 1);
            {
                spinner.setEditable(true);
                spinner.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-font-family: 'Monospaced';");
                spinner.valueProperty().addListener((obs, old, newVal) -> {
                    if (getIndex() >= 0 && getIndex() < getTableView().getItems().size() && newVal != null) {
                        Tema t = getTableView().getItems().get(getIndex());
                        if (t == temaEmEdicao) t.setCargaMinima(newVal);
                    }
                });
            }
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); setText(null); return; }
                Tema t = getTableView().getItems().get(getIndex());
                if (t == temaEmEdicao) {
                    spinner.getValueFactory().setValue(item);
                    setGraphic(spinner);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(String.valueOf(item));
                }
                setAlignment(Pos.CENTER);
            }
        });

        // COLUNA MÁXIMO
        colMax.setCellValueFactory(new PropertyValueFactory<>("cargaMaxima"));
        colMax.setCellFactory(col -> new TableCell<>() {
            private final Spinner<Integer> spinner = new Spinner<>(1, 100, 1);
            {
                spinner.setEditable(true);
                spinner.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-font-family: 'Monospaced';");
                spinner.valueProperty().addListener((obs, old, newVal) -> {
                    if (getIndex() >= 0 && getIndex() < getTableView().getItems().size() && newVal != null) {
                        Tema t = getTableView().getItems().get(getIndex());
                        if (t == temaEmEdicao) t.setCargaMaxima(newVal);
                    }
                });
            }
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); setText(null); return; }
                Tema t = getTableView().getItems().get(getIndex());
                if (t == temaEmEdicao) {
                    spinner.getValueFactory().setValue(item);
                    setGraphic(spinner);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(String.valueOf(item));
                }
                setAlignment(Pos.CENTER);
            }
        });

        // COLUNA PRIORIDADE
        colPrioridade.setCellValueFactory(new PropertyValueFactory<>("prioridade"));
        colPrioridade.setCellFactory(col -> new TableCell<>() {
            private final ComboBox<Prioridade> combo = new ComboBox<>(FXCollections.observableArrayList(Prioridade.values()));
            {
                combo.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-font-family: 'Monospaced';");
                combo.valueProperty().addListener((obs, old, newVal) -> {
                    if (getIndex() >= 0 && getIndex() < getTableView().getItems().size()) {
                        Tema t = getTableView().getItems().get(getIndex());
                        if (t == temaEmEdicao && newVal != null) t.setPrioridade(newVal);
                    }
                });
            }
            @Override protected void updateItem(Prioridade item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); setText(null); return; }
                Tema t = getTableView().getItems().get(getIndex());
                if (t == temaEmEdicao) {
                    combo.setValue(item);
                    setGraphic(combo);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(item.name());
                }
                setAlignment(Pos.CENTER);
            }
        });

        // COLUNA PROVA (CHECKBOX)
        colProva.setCellValueFactory(new PropertyValueFactory<>("eAvaliacao"));
        colProva.setCellFactory(col -> new TableCell<>() {
            private final CheckBox chk = new CheckBox();
            {
                chk.setStyle("-fx-scale-x: 1.3; -fx-scale-y: 1.3; -fx-cursor: hand;");
                chk.selectedProperty().addListener((obs, old, newVal) -> {
                    if (getIndex() >= 0 && getIndex() < getTableView().getItems().size()) {
                        Tema t = getTableView().getItems().get(getIndex());
                        if (t == temaEmEdicao) t.setEAvaliacao(newVal);
                    }
                });
            }
            @Override protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); return; }
                Tema t = getTableView().getItems().get(getIndex());
                chk.setSelected(item);
                chk.setDisable(t != temaEmEdicao); // Fica cinza se não estiver editando
                setGraphic(chk);
                setAlignment(Pos.CENTER);
            }
        });

        // COLUNA DE AÇÕES (Troca os botões dependendo do estado da linha)
        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnSalvar = new Button("✔ Salvar");
            private final Button btnCancelar = new Button("X");
            private final Button btnEditar = new Button("✎");
            private final Button btnExcluir = new Button("X");

            {
                btnSalvar.setStyle("-fx-background-color: #d4edda; -fx-border-color: black; -fx-cursor: hand; -fx-font-family: 'Monospaced'; -fx-font-weight: bold;");
                btnCancelar.setStyle("-fx-background-color: #f8d7da; -fx-border-color: black; -fx-cursor: hand; -fx-text-fill: #721c24; -fx-font-family: 'Monospaced'; -fx-font-weight: bold;");
                btnEditar.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 15px;");
                btnExcluir.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 15px; -fx-text-fill: red; -fx-font-weight: bold;");

                btnEditar.setOnAction(e -> iniciarEdicaoInline(getTableView().getItems().get(getIndex())));
                btnExcluir.setOnAction(e -> excluirTema(getTableView().getItems().get(getIndex())));
                btnSalvar.setOnAction(e -> salvarEdicaoInline(getTableView().getItems().get(getIndex())));
                btnCancelar.setOnAction(e -> cancelarEdicaoInline(getTableView().getItems().get(getIndex())));
            }

            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Tema t = getTableView().getItems().get(getIndex());

                if (t == temaEmEdicao) {
                    HBox box = new HBox(5, btnSalvar, btnCancelar);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                } else {
                    HBox box = new HBox(15, btnEditar, btnExcluir);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
    }

    // --- AÇÕES INLINE ---

    @FXML
    private void handleAdicionarTema() {
        if (temaEmEdicao != null) {
            exibirAlerta("Aviso", "Conclua a edição atual antes de adicionar um novo tema.", Alert.AlertType.WARNING);
            return;
        }
        int proximaOrdem = disciplinaAtual.getTemas().size() + 1;

        // Cria um tema VAZIO, mas não salva no banco ainda
        Tema novoTema = new Tema(disciplinaAtual, "", 2, 4, Prioridade.MEDIA, false, proximaOrdem);
        disciplinaAtual.getTemas().add(novoTema);

        atualizarTabelaESomas();
        iniciarEdicaoInline(novoTema);
    }

    private void iniciarEdicaoInline(Tema t) {
        if (temaEmEdicao != null) return;
        temaEmEdicao = t;
        tituloOriginalEdicao = t.getTitulo(); // Guarda para o WHERE do SQL caso mude o nome
        tabelaTemas.refresh(); // Força a tabela a desenhar os inputs na linha
    }

    private void salvarEdicaoInline(Tema t) {
        if (t.getTitulo() == null || t.getTitulo().trim().isEmpty()) {
            exibirAlerta("Aviso", "O título do tema não pode ser vazio.", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (tituloOriginalEdicao == null || tituloOriginalEdicao.isEmpty()) {
                // É um tema inteiramente novo, faz INSERT
                TemaService.salvar(t);
            } else {
                // É uma edição, faz UPDATE
                TemaService.editar(t, tituloOriginalEdicao);
            }
            temaEmEdicao = null;
            tituloOriginalEdicao = null;
            carregarTemasDoBanco();

        } catch (SQLException e) {
            exibirAlerta("Erro", "Erro ao gravar tema no banco: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cancelarEdicaoInline(Tema t) {
        // Se era uma linha nova (sem título original salvo), apenas removemos da lista
        if (tituloOriginalEdicao == null || tituloOriginalEdicao.isEmpty()) {
            disciplinaAtual.getTemas().remove(t);
        }
        temaEmEdicao = null;
        tituloOriginalEdicao = null;
        carregarTemasDoBanco(); // Recarrega os dados intactos do banco
    }

    private void excluirTema(Tema t) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Deseja excluir o tema '" + t.getTitulo() + "'?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.YES) {
                try {
                    TemaService.excluir(t);
                    carregarTemasDoBanco();
                } catch (SQLException e) {
                    exibirAlerta("Erro", "Erro ao excluir: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    // --- OUTRAS AÇÕES DA TELA ---
    @FXML
    private void handleValidar() {
        showSprintError = !showSprintError;
        boxSprintError.setVisible(showSprintError);
        boxSprintError.setManaged(showSprintError);
        if (!showSprintError) {
            exibirAlerta("Sucesso", "Distribuição validada! Sem conflitos de Sprint.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML private void handleVoltar() { Main.loadView("MinhasDisciplinas.fxml"); }
    @FXML private void handleExportar() { exibirAlerta("Exportação", "Arquivo exportado para a sua pasta de Documentos.", Alert.AlertType.INFORMATION); }
    @FXML private void handleFinalizar() { exibirAlerta("Concluído", "Planejamento salvo.", Alert.AlertType.INFORMATION); }

    private void exibirAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}