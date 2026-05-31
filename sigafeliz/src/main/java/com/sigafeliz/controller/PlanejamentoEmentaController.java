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
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sigafeliz.service.DistribuicaoAulaService;
import com.sigafeliz.service.SemestreService;
import com.sigafeliz.model.Semestre;
import java.util.ArrayList;
import com.sigafeliz.dao.DiaRestritoDAO;
import com.sigafeliz.dao.GradeDAO;
import com.sigafeliz.model.DiaRestrito;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        // COLUNA TÍTULO
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

        // COLUNA MÍNIMO
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

        // COLUNA PROVA
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

        // COLUNA DE AÇÕES
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

        Tema novoTema = new Tema(disciplinaAtual, "", 2, 4, Prioridade.MEDIA, false, proximaOrdem);
        disciplinaAtual.getTemas().add(novoTema);

        atualizarTabelaESomas();
        iniciarEdicaoInline(novoTema);
    }

    private void iniciarEdicaoInline(Tema t) {
        if (temaEmEdicao != null) return;
        temaEmEdicao = t;
        tituloOriginalEdicao = t.getTitulo();
        tabelaTemas.refresh();
    }

    private void salvarEdicaoInline(Tema t) {
        if (t.getTitulo() == null || t.getTitulo().trim().isEmpty()) {
            exibirAlerta("Aviso", "O título do tema não pode ser vazio.", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (tituloOriginalEdicao == null || tituloOriginalEdicao.isEmpty()) {
                TemaService.salvar(t);
            } else {
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
        if (tituloOriginalEdicao == null || tituloOriginalEdicao.isEmpty()) {
            disciplinaAtual.getTemas().remove(t);
        }
        temaEmEdicao = null;
        tituloOriginalEdicao = null;
        carregarTemasDoBanco();
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


    @FXML
    private void handleExportar() {
        if (disciplinaAtual == null || disciplinaAtual.getTemas().isEmpty()) {
            exibirAlerta("Aviso", "Não há temas cadastrados nesta disciplina para exportar.", Alert.AlertType.WARNING);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Planejamento como Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Planilha do Excel (*.xlsx)", "*.xlsx"));

        String defaultName = "Planejamento_" + disciplinaAtual.getNome().replaceAll("\\s+", "_") + ".xlsx";
        fileChooser.setInitialFileName(defaultName);

        File file = fileChooser.showSaveDialog(tabelaTemas.getScene().getWindow());

        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Grade Diária");
                Row headerRow = sheet.createRow(0);

                String[] columns = {
                        "Número da Aula",
                        "Data",
                        "Tema",
                        "Marcador de Prova",
                        "Dia da Semana",
                        "Identificação da Disciplina"
                };

                for (int i = 0; i < columns.length; i++) {
                    headerRow.createCell(i).setCellValue(columns[i]);
                }

                Semestre semestre = SemestreService.getSemestreSelecionado();
                DistribuicaoAulaService distribuicaoService = new DistribuicaoAulaService();
                List<Tema> temasDistribuidos;

                if (semestre != null) {
                    temasDistribuidos = distribuicaoService.distribuir(disciplinaAtual.getNome(), semestre.getNome());
                } else {
                    temasDistribuidos = new ArrayList<>(disciplinaAtual.getTemas());
                }

                temasDistribuidos.sort(Comparator.comparingInt(Tema::getOrdem));

                Queue<Tema> filaAulas = new LinkedList<>();
                for (Tema t : temasDistribuidos) {
                    if (t.isEAvaliacao()) {
                        filaAulas.add(t); // prova entra UMA vez só
                    } else {
                        for (int i = 0; i < t.getAulasAlocadas(); i++) {
                            filaAulas.add(t); // conteúdo normal entra uma vez por aula
                        }
                    }
                }

                GradeDAO gradeDAO = new GradeDAO();
                DiaRestritoDAO diaRestritoDAO = new DiaRestritoDAO();
                Map<DayOfWeek, Integer> gradeAulas = gradeDAO.buscarGradePorDisciplina(disciplinaAtual.getNome());
                Set<LocalDate> datasRestritas = diaRestritoDAO.listarPorSemestre(semestre)
                        .stream().map(DiaRestrito::getData)
                        .collect(java.util.stream.Collectors.toSet());

                String[] nomesDias = {"", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"};
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                int rowNum = 1;
                int numeroDaAula = 1;
                LocalDate data = semestre.getDataInicio();

                while (!data.isAfter(semestre.getDataFim()) && !filaAulas.isEmpty()) {
                    DayOfWeek dia = data.getDayOfWeek();

                    if (gradeAulas.containsKey(dia) && !datasRestritas.contains(data)) {
                        int qtdAulasNoDia = gradeAulas.get(dia);
                        Tema proximoTema = filaAulas.peek();

                        if (proximoTema != null && proximoTema.isEAvaliacao()) {
                            // Procura o próximo dia com 2+ aulas para a prova
                            LocalDate melhorDia = null;
                            int melhorQtd = 0;
                            LocalDate buscaData = data;

                            while (!buscaData.isAfter(semestre.getDataFim())) {
                                DayOfWeek buscaDia = buscaData.getDayOfWeek();
                                if (gradeAulas.containsKey(buscaDia) && !datasRestritas.contains(buscaData)) {
                                    int qtd = gradeAulas.get(buscaDia);
                                    if (qtd >= 2) {
                                        melhorDia = buscaData;
                                        melhorQtd = qtd;
                                        break;
                                    } else if (melhorDia == null) {
                                        // Guarda o primeiro dia disponível como fallback
                                        melhorDia = buscaData;
                                        melhorQtd = qtd;
                                    }
                                }
                                buscaData = buscaData.plusDays(1);
                            }

                            // Avança 'data' até o melhor dia encontrado
                            if (melhorDia != null) {
                                data = melhorDia;
                                dia = data.getDayOfWeek();
                                qtdAulasNoDia = melhorQtd;
                            }

                            // Consome a prova e gera uma linha
                            Tema temaProva = filaAulas.poll();
                            for (int i = 1; i < qtdAulasNoDia; i++) {
                                if (!filaAulas.isEmpty() && filaAulas.peek() == temaProva) {
                                    filaAulas.poll();
                                }
                            }

                            Row row = sheet.createRow(rowNum++);
                            row.createCell(0).setCellValue(numeroDaAula);
                            numeroDaAula += qtdAulasNoDia;
                            row.createCell(1).setCellValue(data.format(formatter));
                            row.createCell(2).setCellValue(temaProva.getTitulo());
                            row.createCell(3).setCellValue("Sim");
                            row.createCell(4).setCellValue(nomesDias[dia.getValue()]);
                            row.createCell(5).setCellValue(disciplinaAtual.getNome());

                        } else {
                            // Comportamento normal: 1 linha por aula
                            for (int i = 0; i < qtdAulasNoDia && !filaAulas.isEmpty(); i++) {
                                Tema temaAula = filaAulas.poll();

                                // Se o próximo tema normal for prova, para o dia aqui
                                if (temaAula.isEAvaliacao()) {
                                    filaAulas.add(temaAula); // devolve pra fila
                                    break;
                                }

                                Row row = sheet.createRow(rowNum++);
                                row.createCell(0).setCellValue(numeroDaAula++);
                                row.createCell(1).setCellValue(data.format(formatter));
                                row.createCell(2).setCellValue(temaAula.getTitulo());
                                row.createCell(3).setCellValue("Sim".equals("Não") ? "Sim" : "Não");
                                row.createCell(4).setCellValue(nomesDias[dia.getValue()]);
                                row.createCell(5).setCellValue(disciplinaAtual.getNome());
                            }
                        }
                    }

                    data = data.plusDays(1);
                }

                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }

                exibirAlerta("Sucesso", "Grade exportada com sucesso!\nArquivo salvo em:\n" + file.getAbsolutePath(), Alert.AlertType.INFORMATION);

            } catch (Exception e) {
                exibirAlerta("Erro na Exportação", "Ocorreu um erro ao gerar a planilha XLSX:\n" + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML private void handleFinalizar() { exibirAlerta("Concluído", "Planejamento salvo.", Alert.AlertType.INFORMATION); }

    private void exibirAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}