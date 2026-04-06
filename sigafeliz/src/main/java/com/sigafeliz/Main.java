package com.sigafeliz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    // O objeto stage (palco) equivale à "moldura" da janela do windows.
    // Dentro do stage, serão carregadas scenes (cenas).
    // Dentro das scenes, serão produzidas as views (telas) a partir de uma hierarquia de elementos nós.
    private static Stage primaryStage;

    // O JavaFX chama esse método 'start' automaticamente assim que o programa abre, e injeta o stage instanciado por ele.
    @Override
    public void start(Stage stage) {
        // Fazemos a variável que criamos apontar para o objeto stage que foi criado pelo JavaFX, para mexermos nele depois.
        primaryStage = stage;
        // Define o texto que aparece em cima, na barra da janela.
        primaryStage.setTitle("Siga Feliz");
        // Abre a primeira tela do sistema (a tela inicial).
        loadView("TelaInicial.fxml");
    }

    /**
     * Esse método serve para trocar de tela.
     * Você só precisa passar o nome do arquivo .fxml que quer mostrar.
     */
    public static void loadView(String fxmlFileName) {
        try {
            // 1. Instancia Classe FXMLLoader que é capaz de carregar FXML
            // Ele sempre olhará pro caminho pasado como argumento quando for instanciado.
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/" + fxmlFileName));

            // 2. Chama o método que efetivamente carega o arquvo FXML em um nó do tipo Parent
            //  Atribui o resultado do método à variável root (raíz).
            Parent root = loader.load();

            // 3. Cria a Scene (o conteúdo) partindo do nó root com o tamanho fixo de 900x600.
            Scene scene = new Scene(root, 900, 600);

            // 4. Aplica o arquivo de estilo (CSS) para a tela não ficar com a aparência padrão.
            scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());

            // 5. Coloca o conteúdo dentro do stage, centraliza e mostra para o usuário.
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (IOException e) {
            // Se o arquivo não for encontrado ou estiver com erro, avisa no console.
            System.err.println("Erro ao carregar a tela: " + fxmlFileName);
            e.printStackTrace();
        }
    }

    // O ponto de partida de qualquer programa Java. Só serve para dar o "play" no JavaFX.
    public static void main(String[] args) {
        launch(args);
    }
}