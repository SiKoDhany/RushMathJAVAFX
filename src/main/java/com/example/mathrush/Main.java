package com.example.mathrush;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Clase principal que inicia la aplicación Math Rush.
 */
public class Main extends Application {
    
    // Constantes de la aplicación
    private static final String APP_TITLE = "Math Rush - Desafío Matemático";
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 650;
    
    // Referencia al juego para poder limpiarlo al cerrar
    private QuizGame quizGame;
    
    @Override
    public void start(Stage stage) {
        try {
            // Inicializar el juego
            quizGame = new QuizGame();
            
            // Configurar la escena
            Scene scene = new Scene(quizGame.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
            
            // Configurar el escenario
            stage.setScene(scene);
            stage.setTitle(APP_TITLE);
            stage.setResizable(false);
            
            // Agregar manejador para limpieza de recursos al cerrar
            stage.setOnCloseRequest(event -> {
                if (quizGame != null) {
                    quizGame.shutdown();
                }
            });
            
            // Mostrar la aplicación
            stage.show();
        } catch (Exception e) {
            // Manejar cualquier excepción durante la inicialización
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }

    /**
     * Método principal que lanza la aplicación JavaFX.
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        launch(args);
    }
}