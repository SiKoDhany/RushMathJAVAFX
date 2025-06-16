package com.example.mathrush;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        QuizGame quizGame = new QuizGame();
        Scene scene = new Scene(quizGame.getRoot(), 600, 650);
        
        // No es necesario cargar el CSS externo ya que los estilos ahora están incorporados directamente
        
        stage.setScene(scene);
        stage.setTitle("Math Rush - Desafío Matemático");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}