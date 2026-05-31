package com.sigafeliz;

import com.sigafeliz.model.Disciplina;
import com.sigafeliz.model.Professor;
import com.sigafeliz.service.DisciplinaService;
import com.sigafeliz.service.FeriadoNacionalService;
import com.sigafeliz.service.ProfessorService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Siga Feliz - MODO DE TESTE DA EMENTA");

        // --- INÍCIO DA EXECUÇÃO DA NOVA FEATURE ---
        // Verifica e popula a nova tabela com feriados nacionais do range atual + 3 semestres futuros
        FeriadoNacionalService.inicializarFeriadosProximosSemestres();
        // --- FIM DA EXECUÇÃO DA NOVA FEATURE ---

        // --- INÍCIO DO SETUP DE TESTE DA SUA FEATURE ---
        //prepararDadosDeTeste();
        // --- FIM DO SETUP DE TESTE ---

        // Carrega DIRETO a sua tela, pulando o login e a lista de disciplinas
        loadView("TelaInicial.fxml");
    }

    /**
     * Injeta dados no banco de dados para evitar erro de Foreign Key
     * e simula a seleção feita nas telas anteriores.
     */
    private void prepararDadosDeTeste() {
        try {
            // 1. Garante que existe um professor no banco
            Professor profTeste = ProfessorService.getProfessorPorNome("Professor Teste");
            if (profTeste == null) {
                profTeste = new Professor("Professor Teste", "teste27@fatec.com");
                ProfessorService.salvar(profTeste);
            }

            // 2. Garante que existe uma disciplina no banco atrelada a esse professor
            Disciplina discTeste = new Disciplina("Disciplina de Teste Automático", profTeste, 80);
            try {
                DisciplinaService.salvar(discTeste);
            } catch (SQLException e) {
                // Ignora o erro se a disciplina já existir no banco das execuções anteriores
            }

            // 3. O PULO DO GATO: Seta a disciplina na "sessão" do sistema!
            DisciplinaService.setDisciplinaSelecionada(discTeste);
            System.out.println("✅ Ambiente de teste preparado. Disciplina em sessão: " + discTeste.getNome());

        } catch (SQLException e) {
            System.err.println("❌ Erro ao preparar dados de teste: " + e.getMessage());
        }
    }

    public static void loadView(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/" + fxmlFileName));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1100, 700);
            scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela: " + fxmlFileName);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}