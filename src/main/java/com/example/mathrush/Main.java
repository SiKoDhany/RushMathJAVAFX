package com.example.mathrush;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        QuizGame quizGame = new QuizGame();
        Scene scene = new Scene(quizGame.getRoot(), 400, 300);
        stage.setScene(scene);
        stage.setTitle("Math Rush");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}