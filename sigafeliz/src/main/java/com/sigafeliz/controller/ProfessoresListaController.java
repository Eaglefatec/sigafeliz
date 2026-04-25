package com.sigafeliz.controller;

import com.sigafeliz.Main;
import com.sigafeliz.model.Professor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProfessoresListaController {

    @FXML private TableView<Professor> tabelaProfessores;
    @FXML private TableColumn<Professor, String> colNome;
    @FXML private TableColumn<Professor, String> colEmail;

    // Declarando os botões para que o JavaFX os reconheça
    @FXML private Button btnVoltar;
    @FXML private Button btnAvancar;
    @FXML private Button btnAdd;

    private ObservableList<Professor> listaProfessores = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Dados de teste
        listaProfessores.add(new Professor("Carlos Silva", "carlos.silva@email.com"));
        listaProfessores.add(new Professor("Maria Oliveira", "maria.oliveira@email.com"));

        tabelaProfessores.setItems(listaProfessores);
    }

    @FXML
    private void voltarParaHome() {
        // Certifique-se que o arquivo existe em resources/view/
        Main.loadView("TelaInicial.fxml");
    }

    @FXML
    private void irParaProximaTela() {
        // Substitua pelo nome real da sua próxima tela
       // Main.loadView("ProximaTela.fxml");
        System.out.println("O botão Avançar está funcionando perfeitamente!");
    }
}