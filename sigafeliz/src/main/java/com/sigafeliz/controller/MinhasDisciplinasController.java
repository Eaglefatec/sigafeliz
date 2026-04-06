package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.AulasPorDia;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Professor;
import com.sigafeliz.model.Semestre;
import com.sigafeliz.service.MockDataService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

public class MinhasDisciplinasController {

    @FXML private Label lblNomeProfessor;
    @FXML private ComboBox<Semestre> comboSemestre;
    @FXML private TableView<Disciplina> tabelaDisciplinas;
    @FXML private TableColumn<Disciplina, String> colNome;
    @FXML private TableColumn<Disciplina, Integer> colCarga;
    @FXML private TableColumn<Disciplina, String> colAulasSemana;

    @FXML
    public void initialize() {
        Professor logado = MockDataService.getProfessorLogado();
        if (logado == null) return;

        lblNomeProfessor.setText(logado.getNome());

        comboSemestre.setItems(FXCollections.observableArrayList(MockDataService.getAllSemestres()));
        comboSemestre.setConverter(new StringConverter<Semestre>() {
            @Override public String toString(Semestre s) { return s == null ? "" : s.getNome(); }
            @Override public Semestre fromString(String string) { return null; }
        });

        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCarga.setCellValueFactory(new PropertyValueFactory<>("cargaHorariaTotal"));

        // Soma e Tradução
        colAulasSemana.setCellValueFactory(data -> {
            List<AulasPorDia> aulas = data.getValue().getAulasPorDia();

            // Soma o total de aulas
            int totalAulas = aulas.stream().mapToInt(AulasPorDia::getQuantidadeAulas).sum();

            // Traduz os dias
            String diasStr = aulas.stream()
                    .map(a -> traduzirDiaDaSemana(a.getDiaSemana()))
                    .collect(Collectors.joining(", "));

            return new SimpleStringProperty(totalAulas + " (" + diasStr + ")");
        });

        tabelaDisciplinas.setItems(FXCollections.observableArrayList(MockDataService.getDisciplinasPorProfessor(logado)));
    }

    private String traduzirDiaDaSemana(DayOfWeek day) {
        switch (day) {
            case MONDAY: return "Segunda";
            case TUESDAY: return "Terça";
            case WEDNESDAY: return "Quarta";
            case THURSDAY: return "Quinta";
            case FRIDAY: return "Sexta";
            case SATURDAY: return "Sábado";
            case SUNDAY: return "Domingo";
            default: return "";
        }
    }

    @FXML
    private void handlePlanejar() {
        Disciplina disc = tabelaDisciplinas.getSelectionModel().getSelectedItem();
        Semestre sem = comboSemestre.getValue();

        if (disc == null || sem == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("Selecione uma DISCIPLINA na tabela e um SEMESTRE letivo para prosseguir.");
            alert.showAndWait();
            return;
        }

        MockDataService.setDisciplinaSelecionada(disc);
        MockDataService.setSemestreSelecionado(sem);
        Main.loadView("PlanejamentoEmenta.fxml");
    }

    @FXML
    private void handleVoltar() {
        Main.loadView("SelecaoProfessor.fxml");
    }
}