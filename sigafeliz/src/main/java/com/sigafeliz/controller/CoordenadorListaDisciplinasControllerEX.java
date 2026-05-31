package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Professor;
import com.sigafeliz.service.DisciplinaService;
import com.sigafeliz.service.ProfessorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class CoordenadorListaDisciplinasControllerEX {

    @FXML private Label lblTituloGeral;
    @FXML private TableView<Disciplina> tabelaDisciplinas;
    @FXML private TableColumn<Disciplina, String> colCurso;
    @FXML private TableColumn<Disciplina, String> colNome;
    @FXML private TableColumn<Disciplina, Integer> colCarga;
    @FXML private TableColumn<Disciplina, Void> colAcoesGrade;
    @FXML private TableColumn<Disciplina, Void> colAcoesSelec;

    @FXML private TextField txtCurso;
    @FXML private TextField txtDisciplina;
    @FXML private ComboBox<Integer> comboCarga;

    private ObservableList<Disciplina> listaDisciplinas;
    private final ToggleGroup disciplinaGroup = new ToggleGroup();
    private Disciplina disciplinaSelecionadaTable;
    private Professor professorAtual;

    @FXML
    public void initialize() {
        // Resgata o professor que foi clicado na tela anterior
        professorAtual = ProfessorService.getProfessorLogado();

        if (professorAtual == null) {
            mostrarAlerta("Nenhum professor selecionado! Retornando...");
            Main.loadView("ProfessoresLista.fxml");
            return;
        }

        // Define o título da tela com o nome do professor selecionado
        if (lblTituloGeral != null) {
            lblTituloGeral.setText("DISCIPLINAS DO PROFESSOR: " + professorAtual.getNome().toUpperCase());
        }

        comboCarga.getItems().addAll(40, 80);

        // Configuração das colunas de texto da Tabela
        colCurso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCarga.setCellValueFactory(new PropertyValueFactory<>("cargaHorariaTotal"));

        configurarColunasAcoes();

        // Carrega as disciplinas APENAS do professor selecionado
        listaDisciplinas = FXCollections.observableArrayList(DisciplinaService.getDisciplinasPorProfessor(professorAtual));
        tabelaDisciplinas.setItems(listaDisciplinas);
    }

    private void configurarColunasAcoes() {
        // Coluna de Botão para Acessar Grade
        colAcoesGrade.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Aulas");
            {
                btn.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2; -fx-font-weight: bold; -fx-cursor: hand;");
                btn.setOnAction(e -> {
                    Disciplina d = getTableView().getItems().get(getIndex());
                    DisciplinaService.setDisciplinaSelecionada(d);
                    Main.loadView("GradeSemanal.fxml");
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    setGraphic(btn);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        // Coluna de Seleção para Excluir
        colAcoesSelec.setCellFactory(param -> new TableCell<>() {
            private final RadioButton rb = new RadioButton();
            {
                rb.setToggleGroup(disciplinaGroup);
                rb.setOnAction(e -> {
                    disciplinaSelecionadaTable = getTableView().getItems().get(getIndex());
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    setGraphic(rb);
                    setAlignment(Pos.CENTER);
                    Disciplina d = getTableView().getItems().get(getIndex());
                    rb.setSelected(d.equals(disciplinaSelecionadaTable));
                }
            }
        });
    }

    @FXML
    private void salvarDisciplina(ActionEvent event) {
        String curso = txtCurso.getText();
        String nome = txtDisciplina.getText();
        Integer carga = comboCarga.getValue();

        if (curso == null || curso.trim().isEmpty() || nome == null || nome.trim().isEmpty() || carga == null) {
            mostrarAlerta("Preencha todos os campos para salvar a disciplina.");
            return;
        }

        // Atrela a disciplina automaticamente ao professor logado
        Disciplina novaDisciplina = new Disciplina(curso, nome, professorAtual, carga);

        try {
            DisciplinaService.salvar(novaDisciplina);
            listaDisciplinas.add(novaDisciplina);
            limparCampos();
        } catch (SQLException e) {
            mostrarAlerta("Erro ao salvar disciplina: " + e.getMessage());
        }
    }

    @FXML
    private void excluirDisciplina(ActionEvent event) {
        if (disciplinaSelecionadaTable == null) {
            mostrarAlerta("Selecione uma disciplina na tabela para excluir.");
            return;
        }

        try {
            DisciplinaService.excluir(disciplinaSelecionadaTable);
            listaDisciplinas.remove(disciplinaSelecionadaTable);
            disciplinaSelecionadaTable = null;
            disciplinaGroup.selectToggle(null);
        } catch (SQLException e) {
            mostrarAlerta("Erro ao excluir disciplina: " + e.getMessage());
        }
    }

    private void limparCampos() {
        txtCurso.clear();
        txtDisciplina.clear();
        comboCarga.setValue(null);
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML private void voltar(ActionEvent event) { Main.loadView("ProfessoresLista.fxml"); }

    @FXML private void avancar(ActionEvent event) { Main.loadView("SemestreLista.fxml"); }
}