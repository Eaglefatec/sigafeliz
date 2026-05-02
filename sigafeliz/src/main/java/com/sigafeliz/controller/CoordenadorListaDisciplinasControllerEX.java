package com.sigafeliz.controller;


import com.sigafeliz.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import static com.sigafeliz.Main.loadView;

/**
     * Controller para a gestão de Disciplinas no projeto sigafeliz.
     * Focado no alinhamento entre a visualização e a área de CRUD.
     */
public class CoordenadorListaDisciplinasControllerEX implements Initializable {

        @FXML
        private TextField txtDisciplina;

        @FXML
        private ComboBox<String> cbProfessor;

        @FXML
        private ComboBox<Integer> comboCarga;

    @FXML
    void abrirEditarGrade(ActionEvent event) {
        Main.loadView("GradeSemanal.fxml");
    }

        @FXML
        private Button btnSalvar;

        @FXML
        private Button btnExcluir;

        @Override
        public void initialize(URL url, ResourceBundle rb) {
            // Inicializa as opções de carga horária comuns no curso da FATEC
            comboCarga.getItems().addAll(40, 80);

            // Mock de professores para teste de interface
            cbProfessor.getItems().addAll("Prof. Mineda", "Prof. Carlos Silva", "Prof. João Pereira");
        }

        @FXML
        private void salvarDisciplina(ActionEvent event) {
            String nome = txtDisciplina.getText();
            String professor = cbProfessor.getValue();
            Integer carga = comboCarga.getValue();

            if (nome != null && !nome.isEmpty() && professor != null) {
                System.out.println("Salvando Disciplina: " + nome + " | Prof: " + professor + " | CH: " + carga);
                // Aqui entra a sua lógica de persistência com MySQL
                limparCampos();
            } else {
                System.out.println("Por favor, preencha todos os campos.");
            }
        }

        @FXML
        private void excluirDisciplina(ActionEvent event) {
            // Lógica para excluir a disciplina selecionada ou limpar a edição atual
            System.out.println("Excluindo/Limpando seleção atual.");
            limparCampos();
        }

        private void limparCampos() {
            txtDisciplina.clear();
            cbProfessor.setValue(null);
            comboCarga.setValue(null);
        }

        @FXML
        private void voltar(ActionEvent event)  {loadView("ProfessoresLista.fxml");
        }
         //   System.out.println("Retornando para a tela anterior.");


        @FXML
        private void avancar(ActionEvent event) {
            Main.loadView("SemestreLista.fxml");
        }
}

