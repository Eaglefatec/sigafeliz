package com.sigafeliz.controller;

import com.sigafeliz.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

/**
 * Controller para a tela inicial (TelaInicial.fxml).
 * Transiciona para as telas de Coordenação e seleção de Professor.
 */
public class TelaInicialController {

    // Injeção dos componentes definidos no FXML via fx:id
    // Não obrigatório.
    @FXML
    private Button btnProfessor;
    @FXML
    private Button btnCoordinator;

    /**
     * Ação disparada pelo botão "Sou professor".
     */
    @FXML
    private void handleProfessorPath() {
        // No futuro, loadView carregará "IdentificacaoDocente.fxml"
        Main.loadView("LoginView.fxml");
    }

    /**
     * Ação disparada pelo botão "Sou coordenador".
     * TODO: Implementação de fluxo do coordenador.
     */
    @FXML
    private void handleCoordinatorPath() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Siga Feliz - Protótipo");
        alert.setHeaderText("Fluxo em Desenvolvimento");
        alert.setContentText("O fluxo do coordenador não está mapeado nesta iteração.");
        alert.showAndWait();
    }
}