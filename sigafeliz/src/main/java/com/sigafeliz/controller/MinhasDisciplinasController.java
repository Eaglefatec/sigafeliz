package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.dao.DisciplinaDAO;
import com.sigafeliz.model.AulasPorDia;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Professor;
import com.sigafeliz.service.ProfessorService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;

public class MinhasDisciplinasController {

    @FXML private Label lblNomeProfessor;
    @FXML private ComboBox<String> comboSemestre;
    @FXML private TableView<Disciplina> tabelaDisciplinas;
    @FXML private TableColumn<Disciplina, String> colNome;
    @FXML private TableColumn<Disciplina, String> colAulasSemana;
    @FXML private TableColumn<Disciplina, Integer> colCarga;
    @FXML private TableColumn<Disciplina, RadioButton> colSelec;

    private DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private ObservableList<Disciplina> listaDisciplinas;
    private ToggleGroup grupoSelecao = new ToggleGroup();

    @FXML
    public void initialize() {
        // Inicializa o ComboBox de Semestres
        comboSemestre.setItems(FXCollections.observableArrayList("2026.1", "2025.2", "2025.1"));
        comboSemestre.getSelectionModel().selectFirst();

        // Configura as colunas básicas
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCarga.setCellValueFactory(new PropertyValueFactory<>("cargaHorariaTotal"));

        // Processa a lista de aulas dinamicamente sem exigir o método no Modelo
        colAulasSemana.setCellValueFactory(cellData -> {
            Disciplina disciplina = cellData.getValue();
            List<AulasPorDia> aulas = disciplina.getAulasPorDia();

            if (aulas == null || aulas.isEmpty()) {
                return new SimpleStringProperty("Sem aulas registradas");
            }

            try {
                StringBuilder diasTexto = new StringBuilder();
                for (int i = 0; i < aulas.size(); i++) {
                    if (i > 0) diasTexto.append(", ");
                }
                return new SimpleStringProperty(aulas.size() + " dia(s) na semana");
            } catch (Exception e) {
                return new SimpleStringProperty(aulas.size() + " aulas");
            }
        });

        // Configuração do RadioButton na coluna de seleção
        colSelec.setCellFactory(new Callback<TableColumn<Disciplina, RadioButton>, TableCell<Disciplina, RadioButton>>() {
            @Override
            public TableCell<Disciplina, RadioButton> call(TableColumn<Disciplina, RadioButton> param) {
                return new TableCell<Disciplina, RadioButton>() {
                    private final RadioButton rb = new RadioButton();

                    @Override
                    protected void updateItem(RadioButton item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            rb.setToggleGroup(grupoSelecao);

                            Disciplina disciplinaDaLinha = getTableView().getItems().get(getIndex());
                            rb.setUserData(disciplinaDaLinha);
                            rb.setFocusTraversable(false);

                            // Garante que o clique na célula também selecione o RadioButton
                            setOnMouseClicked(event -> rb.setSelected(true));

                            setGraphic(rb);
                            setStyle("-fx-alignment: CENTER;");
                        }
                    }
                };
            }
        });

        carregarDadosProfessor();
    }

    private void carregarDadosProfessor() {
        Professor professorLogado = ProfessorService.getProfessorLogado();
        if (professorLogado != null) {
            lblNomeProfessor.setText(professorLogado.getNome());

            try {
                List<Disciplina> disciplines = disciplinaDAO.listarPorProfessor(professorLogado);
                listaDisciplinas = FXCollections.observableArrayList(disciplines);
                tabelaDisciplinas.setItems(listaDisciplinas);
            } catch (java.sql.SQLException e) {
                System.err.println("Erro ao buscar disciplinas no banco de dados: " + e.getMessage());
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro de Conexão");
                alert.setHeaderText("Não foi possível carregar as disciplinas");
                alert.setContentText("Ocorreu uma falha ao conectar com o banco de dados. Verifique sua conexão.");
                alert.showAndWait();
            }
        } else {
            lblNomeProfessor.setText("Professor não identificado");
        }
    }

    @FXML
    private void handleVoltar() {
        Main.loadView("SelecaoProfessor.fxml");
    }

    @FXML
    private void handlePlanejar() {
        Toggle selecionado = grupoSelecao.getSelectedToggle();

        if (selecionado != null) {
            Disciplina disciplinaSelecionada = (Disciplina) selecionado.getUserData();

            // Se você precisar salvar a disciplina selecionada em um Service (igual fez com o Professor):
            // DisciplinaService.setDisciplinaSelecionada(disciplinaSelecionada);

            Main.loadView("PlanejamentoEmenta.fxml");
        } else {
            // Se você tiver um método utilitário de alerta no controller, use-o aqui. Ex:
            // exibirAlerta("Por favor, selecione uma disciplina antes de avançar.");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecione uma disciplina na tabela antes de avançar.");
            alert.showAndWait();
        }
    }
}