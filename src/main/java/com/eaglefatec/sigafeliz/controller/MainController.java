package com.eaglefatec.sigafeliz.controller;

import com.eaglefatec.sigafeliz.dao.*;
import com.eaglefatec.sigafeliz.engine.*;
import com.eaglefatec.sigafeliz.model.*;
import com.eaglefatec.sigafeliz.model.BlockedDay.DayType;
import com.eaglefatec.sigafeliz.model.Tema.Priority;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Main controller that manages all screens of the application.
 * Builds UI programmatically (no FXML) to keep the project monolithic and
 * simple.
 */
public class MainController {

    private final Stage stage;
    private final BorderPane root;
    private final SemesterDAO semesterDAO = new SemesterDAO();
    private final BlockedDayDAO blockedDayDAO = new BlockedDayDAO();
    private final PlanningUnitDAO planningUnitDAO = new PlanningUnitDAO();
    private final GeneratedScheduleDAO generatedScheduleDAO = new GeneratedScheduleDAO();

    // Wizard state
    private Semester selectedSemester;
    private final Map<DayOfWeek, Integer> weeklySchedule = new LinkedHashMap<>();
    private String subjectName = "";
    private int workload = 40;
    private final ObservableList<Tema> temas = FXCollections.observableArrayList();

    public MainController(Stage stage) {
        this.stage = stage;
        this.root = new BorderPane();
    }

    public void show() {
        Scene scene = new Scene(root, 900, 650);
        scene.getStylesheets().add(getClass().getResource("/com/eaglefatec/sigafeliz/styles.css").toExternalForm());

        // Secret key combination: Ctrl+Shift+D
        KeyCodeCombination devCombo = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN,
                KeyCombination.SHIFT_DOWN);
        scene.setOnKeyPressed(event -> {
            if (devCombo.match(event)) {
                showDevLoginDialog();
            }
        });

        stage.setTitle("Siga Feliz — Planejamento Acadêmico");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        // Set app icon
        try {
            Image icon = new Image(getClass().getResourceAsStream("/com/eaglefatec/sigafeliz/eagle_logo.png"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            // Logo not found, ignore
        }

        stage.show();
        showMainMenu();
    }

    // ==================== MAIN MENU ====================

    private void showMainMenu() {
        VBox menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        menu.getStyleClass().add("main-container");

        // Logo
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/com/eaglefatec/sigafeliz/eagle_logo.png"),
                    120, 120, true, true);
            ImageView logo = new ImageView(logoImage);
            logo.getStyleClass().add("logo-image");
            menu.getChildren().add(logo);
        } catch (Exception e) {
            // Logo not available
        }

        Label title = new Label("Siga Feliz");
        title.getStyleClass().add("app-title");

        Label subtitle = new Label("Planejamento Semestral Automatizado");
        subtitle.getStyleClass().add("app-subtitle");

        Button btnNewPlan = new Button("Novo Planejamento");
        btnNewPlan.getStyleClass().addAll("btn", "btn-primary");
        btnNewPlan.setPrefWidth(300);
        btnNewPlan.setOnAction(e -> showWizardStep1());

        Button btnHistory = new Button("Histórico de Cronogramas");
        btnHistory.getStyleClass().addAll("btn", "btn-secondary");
        btnHistory.setPrefWidth(300);
        btnHistory.setOnAction(e -> showHistory());

        Label hint = new Label("v1.0 — Eagle FATEC");
        hint.getStyleClass().add("footer-text");

        menu.getChildren().addAll(title, subtitle, btnNewPlan, btnHistory, hint);
        root.setCenter(menu);
        root.setTop(null);
    }

    // ==================== DEVELOPER LOGIN & PANEL ====================

    private void showDevLoginDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Acesso de Desenvolvedor");
        dialog.setHeaderText("Insira a chave de desenvolvedor:");
        dialog.initModality(Modality.APPLICATION_MODAL);

        PasswordField keyField = new PasswordField();
        keyField.setPromptText("Chave");
        keyField.setPrefWidth(250);

        VBox content = new VBox(10, keyField);
        content.setPadding(new Insets(20));
        dialog.getDialogPane().setContent(content);

        ButtonType loginType = new ButtonType("Entrar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginType, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt == loginType)
                return keyField.getText();
            return null;
        });

        dialog.setOnShown(e -> keyField.requestFocus());

        dialog.showAndWait().ifPresent(key -> {
            if ("eagle".equals(key)) {
                showDevPanel();
            } else {
                showAlert(Alert.AlertType.ERROR, "Chave inválida", "A chave de desenvolvedor está incorreta.");
            }
        });
    }

    private void showDevPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.getStyleClass().add("panel");

        // Top bar
        HBox topBar = createTopBar("Painel do Coordenador", this::showMainMenu);

        // --- Semesters section ---
        Label lblSemesters = new Label("Semestres");
        lblSemesters.getStyleClass().add("section-title");

        TableView<Semester> semesterTable = new TableView<>();
        semesterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Semester, String> colName = new TableColumn<>("Nome");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Semester, String> colStart = new TableColumn<>("Início");
        colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        TableColumn<Semester, String> colEnd = new TableColumn<>("Fim");
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        TableColumn<Semester, String> colKickoff = new TableColumn<>("Kickoff");
        colKickoff.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getKickoffDate() != null ? cd.getValue().getKickoffDate() : "—"));

        semesterTable.getColumns().addAll(colName, colStart, colEnd, colKickoff);
        semesterTable.setItems(FXCollections.observableArrayList(semesterDAO.findAll()));
        semesterTable.setPrefHeight(180);

        // Add semester form
        HBox addSemForm1 = new HBox(10);
        addSemForm1.setAlignment(Pos.CENTER_LEFT);
        TextField tfName = new TextField();
        tfName.setPromptText("Ex: 2025/1");
        DatePicker dpStart = new DatePicker();
        dpStart.setPromptText("Início");
        DatePicker dpEnd = new DatePicker();
        dpEnd.setPromptText("Fim");
        addSemForm1.getChildren().addAll(new Label("Nome:"), tfName, new Label("Início:"), dpStart,
                new Label("Fim:"), dpEnd);

        HBox addSemForm2 = new HBox(10);
        addSemForm2.setAlignment(Pos.CENTER_LEFT);
        DatePicker dpKickoff = new DatePicker();
        dpKickoff.setPromptText("Kickoff PI");

        Button btnAdd = new Button("Adicionar Semestre");
        btnAdd.getStyleClass().addAll("btn", "btn-primary");
        btnAdd.setOnAction(e -> {
            if (tfName.getText().isBlank() || dpStart.getValue() == null || dpEnd.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Campos obrigatórios",
                        "Preencha nome, início e fim do semestre.");
                return;
            }
            String kickoffStr = dpKickoff.getValue() != null ? dpKickoff.getValue().toString() : null;
            Semester s = new Semester(tfName.getText().trim(), dpStart.getValue().toString(),
                    dpEnd.getValue().toString(), kickoffStr);
            semesterDAO.insert(s);
            semesterTable.setItems(FXCollections.observableArrayList(semesterDAO.findAll()));
            tfName.clear();
            dpStart.setValue(null);
            dpEnd.setValue(null);
            dpKickoff.setValue(null);
        });

        Button btnDelete = new Button("Excluir Selecionado");
        btnDelete.getStyleClass().addAll("btn", "btn-danger");
        btnDelete.setOnAction(e -> {
            Semester sel = semesterTable.getSelectionModel().getSelectedItem();
            if (sel != null) {
                semesterDAO.delete(sel.getId());
                semesterTable.setItems(FXCollections.observableArrayList(semesterDAO.findAll()));
            }
        });

        addSemForm2.getChildren().addAll(new Label("Kickoff PI:"), dpKickoff, btnAdd, btnDelete);

        // --- Blocked days section ---
        Label lblBlocked = new Label("Dias Bloqueados / Sábados Letivos");
        lblBlocked.getStyleClass().add("section-title");

        Label lblSelectSem = new Label("Selecione um semestre na tabela acima para gerenciar seus dias.");
        lblSelectSem.getStyleClass().add("hint-text");

        TableView<BlockedDay> blockedTable = new TableView<>();
        blockedTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<BlockedDay, String> colDate = new TableColumn<>("Data");
        colDate.setCellValueFactory(new PropertyValueFactory<>("blockedDate"));
        TableColumn<BlockedDay, String> colDesc = new TableColumn<>("Descrição");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<BlockedDay, String> colType = new TableColumn<>("Tipo");
        colType.setCellValueFactory(cd -> new SimpleStringProperty(formatDayType(cd.getValue().getDayType())));

        blockedTable.getColumns().addAll(colDate, colDesc, colType);
        blockedTable.setPrefHeight(180);

        // Add blocked day form
        HBox addBlockForm = new HBox(10);
        addBlockForm.setAlignment(Pos.CENTER_LEFT);
        DatePicker dpBlockDate = new DatePicker();
        dpBlockDate.setPromptText("Data");
        TextField tfDesc = new TextField();
        tfDesc.setPromptText("Descrição");
        ComboBox<DayType> cbType = new ComboBox<>(FXCollections.observableArrayList(DayType.values()));
        cbType.setPromptText("Tipo");
        Button btnAddBlock = new Button("Adicionar");
        btnAddBlock.getStyleClass().addAll("btn", "btn-primary");

        Button btnDeleteBlock = new Button("Excluir");
        btnDeleteBlock.getStyleClass().addAll("btn", "btn-danger");

        addBlockForm.getChildren().addAll(new Label("Data:"), dpBlockDate, new Label("Desc:"), tfDesc,
                new Label("Tipo:"), cbType, btnAddBlock, btnDeleteBlock);

        // Link semester selection to blocked days
        semesterTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblSelectSem.setText("Dias de: " + newVal.getName());
                blockedTable
                        .setItems(FXCollections.observableArrayList(blockedDayDAO.findBySemesterId(newVal.getId())));
            }
        });

        btnAddBlock.setOnAction(e -> {
            Semester sel = semesterTable.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione um semestre primeiro.");
                return;
            }
            if (dpBlockDate.getValue() == null || cbType.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Campos obrigatórios", "Preencha data e tipo.");
                return;
            }
            BlockedDay bd = new BlockedDay(sel.getId(), dpBlockDate.getValue().toString(), tfDesc.getText().trim(),
                    cbType.getValue());
            blockedDayDAO.insert(bd);
            blockedTable.setItems(FXCollections.observableArrayList(blockedDayDAO.findBySemesterId(sel.getId())));
            dpBlockDate.setValue(null);
            tfDesc.clear();
            cbType.setValue(null);
        });

        btnDeleteBlock.setOnAction(e -> {
            BlockedDay sel = blockedTable.getSelectionModel().getSelectedItem();
            Semester semSel = semesterTable.getSelectionModel().getSelectedItem();
            if (sel != null && semSel != null) {
                blockedDayDAO.delete(sel.getId());
                blockedTable
                        .setItems(FXCollections.observableArrayList(blockedDayDAO.findBySemesterId(semSel.getId())));
            }
        });

        ScrollPane scrollPane = new ScrollPane();
        VBox contentPane = new VBox(15, lblSemesters, semesterTable, addSemForm1, addSemForm2,
                new Separator(), lblBlocked, lblSelectSem, blockedTable, addBlockForm);
        contentPane.setPadding(new Insets(10));
        scrollPane.setContent(contentPane);
        scrollPane.setFitToWidth(true);

        panel.getChildren().addAll(topBar, scrollPane);
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);

        root.setTop(null);
        root.setCenter(panel);
    }

    // ==================== TEACHER WIZARD ====================

    private void showWizardStep1() {
        // Reset wizard state
        selectedSemester = null;
        weeklySchedule.clear();
        subjectName = "";
        workload = 40;
        temas.clear();

        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));
        panel.setAlignment(Pos.TOP_CENTER);
        panel.getStyleClass().add("panel");

        HBox topBar = createTopBar("Passo 1 de 4 — Selecionar Semestre", this::showMainMenu);

        Label instruction = new Label("Selecione o semestre letivo para o planejamento:");
        instruction.getStyleClass().add("section-title");

        List<Semester> semesters = semesterDAO.findAll();
        if (semesters.isEmpty()) {
            Label noData = new Label(
                    "Nenhum semestre cadastrado.\nPeça ao coordenador para configurar (Ctrl+Shift+D).");
            noData.getStyleClass().add("warning-text");
            noData.setWrapText(true);
            panel.getChildren().addAll(topBar, instruction, noData);
            root.setCenter(panel);
            return;
        }

        ComboBox<Semester> cbSemester = new ComboBox<>(FXCollections.observableArrayList(semesters));
        cbSemester.setPromptText("Escolha um semestre...");
        cbSemester.setPrefWidth(300);

        Label info = new Label("");
        info.getStyleClass().add("hint-text");
        info.setWrapText(true);

        cbSemester.setOnAction(e -> {
            Semester s = cbSemester.getValue();
            if (s != null) {
                String kickoffInfo = s.getKickoffDate() != null ? " | Kickoff PI: " + s.getKickoffDate() : "";
                info.setText(String.format("Período: %s a %s%s", s.getStartDate(), s.getEndDate(), kickoffInfo));
            }
        });

        Button btnNext = new Button("Próximo →");
        btnNext.getStyleClass().addAll("btn", "btn-primary");
        btnNext.setOnAction(e -> {
            if (cbSemester.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione um semestre para continuar.");
                return;
            }
            selectedSemester = cbSemester.getValue();
            showWizardStep2();
        });

        panel.getChildren().addAll(topBar, instruction, cbSemester, info, btnNext);
        root.setCenter(panel);
    }

    private void showWizardStep2() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));
        panel.getStyleClass().add("panel");

        HBox topBar = createTopBar("Passo 2 de 4 — Grade Horária", this::showWizardStep1);

        Label instruction = new Label("Configure os dias de aula e a carga horária:");
        instruction.getStyleClass().add("section-title");

        // Subject name
        HBox nameBox = new HBox(10);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        TextField tfSubject = new TextField(subjectName);
        tfSubject.setPromptText("Nome da matéria");
        tfSubject.setPrefWidth(300);
        nameBox.getChildren().addAll(new Label("Matéria:"), tfSubject);

        // Workload selection
        HBox loadBox = new HBox(15);
        loadBox.setAlignment(Pos.CENTER_LEFT);
        ToggleGroup tg = new ToggleGroup();
        RadioButton rb40 = new RadioButton("40 aulas");
        rb40.setToggleGroup(tg);
        rb40.setUserData(40);
        RadioButton rb80 = new RadioButton("80 aulas");
        rb80.setToggleGroup(tg);
        rb80.setUserData(80);
        if (workload == 80)
            rb80.setSelected(true);
        else
            rb40.setSelected(true);
        loadBox.getChildren().addAll(new Label("Carga Horária:"), rb40, rb80);

        // Weekly schedule grid
        Label lblGrid = new Label("Dias da semana e quantidade de aulas por dia:");
        lblGrid.getStyleClass().add("hint-text");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        String[] dayLabels = { "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado" };
        DayOfWeek[] days = { DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY, DayOfWeek.SATURDAY };
        CheckBox[] checkBoxes = new CheckBox[6];
        Spinner<Integer>[] spinners = new Spinner[6];

        for (int i = 0; i < 6; i++) {
            checkBoxes[i] = new CheckBox(dayLabels[i]);
            spinners[i] = new Spinner<>(1, 6, 2);
            spinners[i].setPrefWidth(80);
            spinners[i].setDisable(true);

            final int idx = i;
            checkBoxes[i].selectedProperty().addListener((obs, old, val) -> spinners[idx].setDisable(!val));

            // Restore previous state
            if (weeklySchedule.containsKey(days[i])) {
                checkBoxes[i].setSelected(true);
                spinners[i].getValueFactory().setValue(weeklySchedule.get(days[i]));
                spinners[i].setDisable(false);
            }

            grid.add(checkBoxes[i], 0, i);
            grid.add(spinners[i], 1, i);
        }

        Button btnBack = new Button("← Voltar");
        btnBack.getStyleClass().addAll("btn", "btn-secondary");
        btnBack.setOnAction(e -> showWizardStep1());

        Button btnNext = new Button("Próximo →");
        btnNext.getStyleClass().addAll("btn", "btn-primary");
        btnNext.setOnAction(e -> {
            if (tfSubject.getText().isBlank()) {
                showAlert(Alert.AlertType.WARNING, "Aviso", "Informe o nome da matéria.");
                return;
            }
            weeklySchedule.clear();
            boolean anySelected = false;
            for (int i = 0; i < 6; i++) {
                if (checkBoxes[i].isSelected()) {
                    weeklySchedule.put(days[i], spinners[i].getValue());
                    anySelected = true;
                }
            }
            if (!anySelected) {
                showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione pelo menos um dia da semana.");
                return;
            }
            subjectName = tfSubject.getText().trim();
            workload = (int) tg.getSelectedToggle().getUserData();
            showWizardStep3();
        });

        HBox buttons = new HBox(15, btnBack, btnNext);
        buttons.setAlignment(Pos.CENTER);

        panel.getChildren().addAll(topBar, instruction, nameBox, loadBox, lblGrid, grid, buttons);
        root.setCenter(panel);
    }

    private void showWizardStep3() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.getStyleClass().add("panel");

        HBox topBar = createTopBar("Passo 3 de 4 — Definir Temas", this::showWizardStep2);

        Label instruction = new Label("Adicione os temas da matéria \"" + subjectName + "\" (" + workload + " aulas):");
        instruction.getStyleClass().add("section-title");
        instruction.setWrapText(true);

        // Temas table
        TableView<Tema> temaTable = new TableView<>(temas);
        temaTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        temaTable.setPrefHeight(250);

        TableColumn<Tema, String> colTitle = new TableColumn<>("Título");
        colTitle.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getTitle()));
        colTitle.setPrefWidth(200);

        TableColumn<Tema, String> colMin = new TableColumn<>("Mín");
        colMin.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getMinAulas())));

        TableColumn<Tema, String> colMax = new TableColumn<>("Máx");
        colMax.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getMaxAulas())));

        TableColumn<Tema, String> colPrio = new TableColumn<>("Prioridade");
        colPrio.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getPriority().name()));

        TableColumn<Tema, String> colEval = new TableColumn<>("Avaliação?");
        colEval.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().isEvaluation() ? "Sim" : "Não"));

        temaTable.getColumns().addAll(colTitle, colMin, colMax, colPrio, colEval);

        // Add tema form
        HBox form1 = new HBox(10);
        form1.setAlignment(Pos.CENTER_LEFT);
        TextField tfTitle = new TextField();
        tfTitle.setPromptText("Título do tema");
        tfTitle.setPrefWidth(200);
        Spinner<Integer> spMin = new Spinner<>(1, 80, 2);
        spMin.setPrefWidth(70);
        Spinner<Integer> spMax = new Spinner<>(1, 80, 4);
        spMax.setPrefWidth(70);
        ComboBox<Priority> cbPrio = new ComboBox<>(FXCollections.observableArrayList(Priority.values()));
        cbPrio.setValue(Priority.MEDIO);
        CheckBox chkEval = new CheckBox("Avaliação");

        form1.getChildren().addAll(
                new Label("Título:"), tfTitle,
                new Label("Mín:"), spMin,
                new Label("Máx:"), spMax,
                new Label("Prior.:"), cbPrio,
                chkEval);

        HBox form2 = new HBox(10);
        form2.setAlignment(Pos.CENTER_LEFT);

        Button btnAddTema = new Button("Adicionar Tema");
        btnAddTema.getStyleClass().addAll("btn", "btn-primary");
        btnAddTema.setOnAction(e -> {
            if (tfTitle.getText().isBlank()) {
                showAlert(Alert.AlertType.WARNING, "Aviso", "Informe o título do tema.");
                return;
            }
            if (spMin.getValue() > spMax.getValue()) {
                showAlert(Alert.AlertType.WARNING, "Aviso", "O mínimo não pode ser maior que o máximo.");
                return;
            }
            Tema t = new Tema(tfTitle.getText().trim(), spMin.getValue(), spMax.getValue(), cbPrio.getValue(),
                    chkEval.isSelected());
            temas.add(t);
            tfTitle.clear();
            chkEval.setSelected(false);
        });

        Button btnRemoveTema = new Button("Remover Selecionado");
        btnRemoveTema.getStyleClass().addAll("btn", "btn-danger");
        btnRemoveTema.setOnAction(e -> {
            Tema sel = temaTable.getSelectionModel().getSelectedItem();
            if (sel != null)
                temas.remove(sel);
        });

        form2.getChildren().addAll(btnAddTema, btnRemoveTema);

        // Live math feedback
        Label mathLabel = new Label();
        mathLabel.getStyleClass().add("math-feedback");
        mathLabel.setWrapText(true);

        Runnable updateMath = () -> {
            int sumMin = temas.stream().mapToInt(Tema::getMinAulas).sum();
            int sumMax = temas.stream().mapToInt(Tema::getMaxAulas).sum();
            String status;
            if (temas.isEmpty()) {
                status = "Nenhum tema adicionado.";
                mathLabel.setTextFill(Color.GRAY);
            } else if (sumMin > workload) {
                status = String.format("⚠ Σ Mín (%d) > Carga (%d). Reduza os mínimos!", sumMin, workload);
                mathLabel.setTextFill(Color.web("#dc2626"));
            } else if (sumMax < workload) {
                status = String.format(
                        "Σ Mín=%d | Σ Máx=%d | Carga=%d — Aulas extras serão preenchidas com 'Fechamento'.", sumMin,
                        sumMax, workload);
                mathLabel.setTextFill(Color.web("#ea580c"));
            } else {
                status = String.format("✓ Σ Mín=%d | Σ Máx=%d | Carga=%d — OK", sumMin, sumMax, workload);
                mathLabel.setTextFill(Color.web("#16a34a"));
            }
            mathLabel.setText(status);
        };

        temas.addListener((javafx.collections.ListChangeListener<Tema>) c -> updateMath.run());
        updateMath.run();

        Button btnBack = new Button("← Voltar");
        btnBack.getStyleClass().addAll("btn", "btn-secondary");
        btnBack.setOnAction(e -> showWizardStep2());

        Button btnNext = new Button("Próximo →");
        btnNext.getStyleClass().addAll("btn", "btn-primary");
        btnNext.setOnAction(e -> {
            if (temas.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Aviso", "Adicione pelo menos um tema.");
                return;
            }
            int sumMin = temas.stream().mapToInt(Tema::getMinAulas).sum();
            if (sumMin > workload) {
                showAlert(Alert.AlertType.ERROR, "Erro",
                        "A soma mínima dos temas excede a carga horária. Ajuste os valores.");
                return;
            }
            showWizardStep4();
        });

        HBox buttons2 = new HBox(15, btnBack, btnNext);
        buttons2.setAlignment(Pos.CENTER);

        panel.getChildren().addAll(topBar, instruction, temaTable, form1, form2, mathLabel, buttons2);
        root.setCenter(panel);
    }

    private void showWizardStep4() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.getStyleClass().add("panel");

        HBox topBar = createTopBar("Passo 4 de 4 — Revisão e Geração", this::showWizardStep3);

        Label instruction = new Label("Revise os dados e gere o cronograma:");
        instruction.getStyleClass().add("section-title");

        // Summary
        VBox summary = new VBox(8);
        summary.getStyleClass().add("summary-card");
        summary.setPadding(new Insets(15));

        String kickoffInfo = selectedSemester.getKickoffDate() != null
                ? selectedSemester.getKickoffDate()
                : "Não definido";

        summary.getChildren().addAll(
                createSummaryRow("Semestre:",
                        selectedSemester.getName() + " (" + selectedSemester.getStartDate() + " a "
                                + selectedSemester.getEndDate() + ")"),
                createSummaryRow("Kickoff PI:", kickoffInfo),
                createSummaryRow("Matéria:", subjectName),
                createSummaryRow("Carga Horária:", workload + " aulas"),
                createSummaryRow("Grade Semanal:", formatWeeklySchedule()),
                createSummaryRow("Temas:", temas.size() + " tema(s) definido(s)"));

        // Pre-validation
        List<BlockedDay> blockedDays = blockedDayDAO.findBySemesterId(selectedSemester.getId());
        SchedulingEngine engine = new SchedulingEngine();

        // Quick validation
        Label validationLabel = new Label();
        validationLabel.setWrapText(true);

        ScheduleResult testResult = engine.generate(selectedSemester, blockedDays, new LinkedHashMap<>(weeklySchedule),
                new ArrayList<>(temas), workload);
        if (testResult.hasErrors()) {
            validationLabel.setText("❌ " + String.join("\n❌ ", testResult.getErrors()));
            validationLabel.setTextFill(Color.web("#dc2626"));
        } else if (testResult.hasWarnings()) {
            validationLabel.setText("⚠ " + String.join("\n⚠ ", testResult.getWarnings()));
            validationLabel.setTextFill(Color.web("#ea580c"));
        } else {
            validationLabel.setText("✓ Validação OK — Pronto para gerar!");
            validationLabel.setTextFill(Color.web("#16a34a"));
        }

        Button btnBack = new Button("← Voltar");
        btnBack.getStyleClass().addAll("btn", "btn-secondary");
        btnBack.setOnAction(e -> showWizardStep3());

        Button btnGenerate = new Button("⚡ Gerar Cronograma");
        btnGenerate.getStyleClass().addAll("btn", "btn-success");
        btnGenerate.setDisable(testResult.hasErrors());
        btnGenerate.setOnAction(e -> generateSchedule(blockedDays));

        HBox buttons = new HBox(15, btnBack, btnGenerate);
        buttons.setAlignment(Pos.CENTER);

        panel.getChildren().addAll(topBar, instruction, summary, validationLabel, buttons);
        root.setCenter(panel);
    }

    private void generateSchedule(List<BlockedDay> blockedDays) {
        SchedulingEngine engine = new SchedulingEngine();
        ScheduleResult result = engine.generate(selectedSemester, blockedDays, new LinkedHashMap<>(weeklySchedule),
                new ArrayList<>(temas), workload);

        if (result.hasErrors()) {
            showAlert(Alert.AlertType.ERROR, "Erro na Geração", String.join("\n", result.getErrors()));
            return;
        }

        // Ask for save directory
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Escolha o diretório para salvar o cronograma");
        dc.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));
        File dir = dc.showDialog(stage);
        if (dir == null)
            return;

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = String.format("Cronograma_%s_%s_%s.xlsx",
                subjectName.replaceAll("[^a-zA-Z0-9]", "_"),
                selectedSemester.getName().replace("/", "-"),
                timestamp);
        File file = new File(dir, fileName);

        try {
            ExcelExporter exporter = new ExcelExporter();
            exporter.export(result.getAulas(), file);

            // Save planning unit + temas to DB
            PlanningUnit pu = new PlanningUnit();
            pu.setSemesterId(selectedSemester.getId());
            pu.setSubjectName(subjectName);
            pu.setWorkload(workload);
            pu.setWeeklyScheduleJson(SchedulingEngine.serializeWeeklySchedule(weeklySchedule));
            pu.setCreatedAt(LocalDateTime.now().toString());
            pu.setTemas(new ArrayList<>(temas));
            int puId = planningUnitDAO.insert(pu);

            // Save generated schedule record
            GeneratedSchedule gs = new GeneratedSchedule();
            gs.setPlanningUnitId(puId);
            gs.setGeneratedAt(LocalDateTime.now().toString());
            gs.setFilePath(file.getAbsolutePath());
            gs.setSubjectName(subjectName);
            gs.setSemesterName(selectedSemester.getName());
            gs.setWorkload(workload);
            generatedScheduleDAO.insert(gs);

            // Show warnings if any
            String warningText = "";
            if (result.hasWarnings()) {
                warningText = "\n\nAvisos:\n" + String.join("\n", result.getWarnings());
            }

            showAlert(Alert.AlertType.INFORMATION, "Cronograma Gerado!",
                    "Arquivo salvo em:\n" + file.getAbsolutePath() + warningText);

            // Open folder in Explorer
            try {
                Desktop.getDesktop().open(dir);
            } catch (Exception ex) {
                // Fallback: just inform user
            }

            showMainMenu();

        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Erro ao gerar", "Falha ao criar o arquivo Excel:\n" + ex.getMessage());
        }
    }

    // ==================== HISTORY ====================

    private void showHistory() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.getStyleClass().add("panel");

        HBox topBar = createTopBar("Histórico de Cronogramas", this::showMainMenu);

        List<GeneratedSchedule> schedules = generatedScheduleDAO.findAll();

        if (schedules.isEmpty()) {
            Label empty = new Label("Nenhum cronograma gerado ainda.");
            empty.getStyleClass().add("hint-text");
            panel.getChildren().addAll(topBar, empty);
            root.setCenter(panel);
            return;
        }

        TableView<GeneratedSchedule> table = new TableView<>(FXCollections.observableArrayList(schedules));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<GeneratedSchedule, String> colDate = new TableColumn<>("Data Geração");
        colDate.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getGeneratedAt().substring(0, Math.min(16, cd.getValue().getGeneratedAt().length()))));

        TableColumn<GeneratedSchedule, String> colSem = new TableColumn<>("Semestre");
        colSem.setCellValueFactory(new PropertyValueFactory<>("semesterName"));

        TableColumn<GeneratedSchedule, String> colSubject = new TableColumn<>("Matéria");
        colSubject.setCellValueFactory(new PropertyValueFactory<>("subjectName"));

        TableColumn<GeneratedSchedule, String> colWorkload = new TableColumn<>("Carga");
        colWorkload.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getWorkload() + " aulas"));

        TableColumn<GeneratedSchedule, String> colPath = new TableColumn<>("Arquivo");
        colPath.setCellValueFactory(cd -> {
            String path = cd.getValue().getFilePath();
            return new SimpleStringProperty(new File(path).getName());
        });

        table.getColumns().addAll(colDate, colSem, colSubject, colWorkload, colPath);

        Button btnOpen = new Button("Abrir no Explorer");
        btnOpen.getStyleClass().addAll("btn", "btn-primary");
        btnOpen.setOnAction(e -> {
            GeneratedSchedule sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                try {
                    File f = new File(sel.getFilePath());
                    if (f.exists()) {
                        Desktop.getDesktop().open(f.getParentFile());
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Arquivo não encontrado",
                                "O arquivo não foi encontrado em:\n" + sel.getFilePath());
                    }
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir o Explorer.");
                }
            }
        });

        panel.getChildren().addAll(topBar, table, btnOpen);
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);
        root.setCenter(panel);
    }

    // ==================== HELPERS ====================

    private HBox createTopBar(String title, Runnable backAction) {
        HBox bar = new HBox(15);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.getStyleClass().add("top-bar");
        bar.setPadding(new Insets(10, 15, 10, 15));

        Button btnBack = new Button("← Voltar");
        btnBack.getStyleClass().addAll("btn", "btn-secondary");
        btnBack.setOnAction(e -> backAction.run());

        Label lbl = new Label(title);
        lbl.getStyleClass().add("top-bar-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        bar.getChildren().addAll(btnBack, lbl, spacer);
        return bar;
    }

    private HBox createSummaryRow(String label, String value) {
        HBox row = new HBox(10);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-weight: bold;");
        lbl.setMinWidth(130);
        Label val = new Label(value);
        val.setWrapText(true);
        row.getChildren().addAll(lbl, val);
        return row;
    }

    private String formatWeeklySchedule() {
        StringBuilder sb = new StringBuilder();
        Map<DayOfWeek, String> names = Map.of(
                DayOfWeek.MONDAY, "Seg",
                DayOfWeek.TUESDAY, "Ter",
                DayOfWeek.WEDNESDAY, "Qua",
                DayOfWeek.THURSDAY, "Qui",
                DayOfWeek.FRIDAY, "Sex",
                DayOfWeek.SATURDAY, "Sáb");
        for (Map.Entry<DayOfWeek, Integer> entry : weeklySchedule.entrySet()) {
            if (!sb.isEmpty())
                sb.append(", ");
            sb.append(names.getOrDefault(entry.getKey(), entry.getKey().name()))
                    .append(": ").append(entry.getValue()).append(" aula(s)");
        }
        return sb.toString();
    }

    private String formatDayType(DayType type) {
        return switch (type) {
            case HOLIDAY -> "Feriado";
            case EVENT -> "Evento";
            case SPRINT_REVIEW -> "Sprint Review";
            case SABADO_LETIVO -> "Sábado Letivo";
        };
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
