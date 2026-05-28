package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.dao.DisciplinaDAO;
import com.sigafeliz.dao.GradeDAO;
import com.sigafeliz.model.AulasPorDia;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Professor;
import com.sigafeliz.service.DisciplinaService;
import com.sigafeliz.service.ProfessorService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public class MinhasDisciplinasController {

    @FXML private Label lblNomeProfessor;
    @FXML private ComboBox<String> comboSemestre;
    @FXML private TableView<Disciplina> tabelaDisciplinas;
    @FXML private TableColumn<Disciplina, String> colNome;
    @FXML private TableColumn<Disciplina, String> colAulasSemana;
    @FXML private TableColumn<Disciplina, Integer> colCarga;
    @FXML private TableColumn<Disciplina, RadioButton> colSelec;

    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private ObservableList<Disciplina> listaDisciplinas;
    private final ToggleGroup grupoSelecao = new ToggleGroup();

    @FXML
    public void initialize() {
        // Inicializa o ComboBox de Semestres
        comboSemestre.setItems(FXCollections.observableArrayList("2026.1", "2025.2", "2025.1"));
        comboSemestre.getSelectionModel().selectFirst();

        // Configura as colunas básicas
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCarga.setCellValueFactory(new PropertyValueFactory<>("cargaHorariaTotal"));

        // Processa e monta dinamicamente o resumo legível dos dias de aula na tabela usando o GradeDAO
        colAulasSemana.setCellValueFactory(cellData -> {
            Disciplina disciplina = cellData.getValue();
            List<AulasPorDia> aulas = disciplina.getAulasPorDia();

            if (aulas == null || aulas.isEmpty()) {
                return new SimpleStringProperty("Sem aulas registradas");
            }

            try {
                StringBuilder diasTexto = new StringBuilder();
                for (int i = 0; i < aulas.size(); i++) {
                    AulasPorDia aula = aulas.get(i);
                    String diaAbreviado = traduzirDiaSemana(aula.getDiaSemana());

                    diasTexto.append(diaAbreviado).append(" (").append(aula.getQuantidadeAulas()).append(")");

                    if (i < aulas.size() - 1) {
                        diasTexto.append(", ");
                    }
                }
                return new SimpleStringProperty(diasTexto.toString());
            } catch (Exception e) {
                return new SimpleStringProperty(aulas.size() + " dia(s) configurado(s)");
            }
        });

        // Configuração do RadioButton na coluna de seleção
        colSelec.setCellFactory(new Callback<TableColumn<Disciplina, RadioButton>, TableCell<Disciplina, RadioButton>>() {
            @Override
            public TableCell<Disciplina, RadioButton> call(TableColumn<Disciplina, RadioButton> param) {
                return new TableCell<>() {
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

                            // Garante que o clique na célula selecione o RadioButton sincronizadamente
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
                // 1. Busca a lista básica de disciplinas vinculadas ao professor
                List<Disciplina> disciplines = disciplinaDAO.listarPorProfessor(professorLogado);

                // 2. Instancia o GradeDAO para acessar estritamente a tabela 'aula_por_dia'
                GradeDAO gradeDAO = new GradeDAO();

                // 3. Alimenta as listas internas de AulasPorDia de cada disciplina através do banco
                for (Disciplina d : disciplines) {
                    d.getAulasPorDia().clear(); // Evita acúmulo de duplicatas em memória

                    // Puxa o mapa da tabela intermediária pelo GradeDAO
                    Map<DayOfWeek, Integer> gradeMap = gradeDAO.buscarGradePorDisciplina(d.getNome());

                    // Reconstrói e acopla os objetos AulasPorDia na disciplina correspondente
                    for (Map.Entry<DayOfWeek, Integer> entrada : gradeMap.entrySet()) {
                        if (entrada.getValue() > 0) {
                            d.addAulaPorDia(new AulasPorDia(d, entrada.getKey(), entrada.getValue()));
                        }
                    }
                }

                // 4. Converte a lista atualizada para Observable e renderiza na interface
                listaDisciplinas = FXCollections.observableArrayList(disciplines);
                tabelaDisciplinas.setItems(listaDisciplinas);

            } catch (java.sql.SQLException e) {
                System.err.println("Erro ao buscar dados no banco com GradeDAO: " + e.getMessage());
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

    // Tradutor de dias da semana para exibição em formato amigável PT-BR
    private String traduzirDiaSemana(DayOfWeek dia) {
        return switch (dia) {
            case MONDAY -> "Seg";
            case TUESDAY -> "Ter";
            case WEDNESDAY -> "Qua";
            case THURSDAY -> "Qui";
            case FRIDAY -> "Sex";
            case SATURDAY -> "Sáb";
            case SUNDAY -> "Dom";
        };
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

            // Grava o objeto selecionado no Service global antes de carregar a tela de Ementas
            DisciplinaService.setDisciplinaSelecionada(disciplinaSelecionada);

            Main.loadView("PlanejamentoEmenta.fxml");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecione uma disciplina na tabela antes de avançar.");
            alert.showAndWait();
        }
    }
}