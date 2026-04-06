package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Professor;
import com.sigafeliz.service.MockDataService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MinhasDisciplinasController {

    @FXML private Label lblNomeProfessor;
    @FXML private TableView<Disciplina> tabelaDisciplinas;
    @FXML private TableColumn<Disciplina, String> colNome;
    @FXML private TableColumn<Disciplina, Integer> colCarga;

    @FXML
    public void initialize() {
        // Obtém o professor logado previamente persistido pelo MockDataService.
        Professor logado = MockDataService.getProfessorLogado();

        if (logado != null) {
            lblNomeProfessor.setText(logado.getNome());

            // Configura quais campos da Disciplina aparecem em cada coluna
            colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colCarga.setCellValueFactory(new PropertyValueFactory<>("cargaHorariaTotal"));

            // Alimenta a tabela com o filtro por professor
            tabelaDisciplinas.setItems(FXCollections.observableArrayList(
                    MockDataService.getDisciplinasPorProfessor(logado)
            ));
        }
    }

    @FXML
    private void handlePlanejar() {
        Disciplina selecionada = tabelaDisciplinas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            // TODO: Criar sessão para disciplina selecionada e ir para planejamento
            System.out.println("Planejando: " + selecionada.getNome());
        }
    }

    @FXML
    private void handleVoltar() {
        Main.loadView("SelecaoProfessor.fxml");
    }
}