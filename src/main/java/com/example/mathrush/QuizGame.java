package com.example.mathrush;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizGame {
    private final VBox root;
    private final Label questionLabel;
    private final Label feedbackLabel;
    private final List<Button> optionButtons;

    private MathQuestion currentQuestion;

    public QuizGame() {
        root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        questionLabel = new Label();
        feedbackLabel = new Label();

        optionButtons = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Button btn = new Button();
            btn.setPrefWidth(200);
            btn.setOnAction(e -> handleAnswer(btn));
            optionButtons.add(btn);
        }

        root.getChildren().addAll(questionLabel);
        root.getChildren().addAll(optionButtons);
        root.getChildren().add(feedbackLabel);

        loadNewQuestion();
    }

    public VBox getRoot() {
        return root;
    }

    private void loadNewQuestion() {
        currentQuestion = MathQuestion.generateRandom();
        questionLabel.setText(currentQuestion.getQuestionText());
        feedbackLabel.setText("");

        List<Integer> options = new ArrayList<>();
        options.add(currentQuestion.getCorrectAnswer());

        while (options.size() < 4) {
            int fakeAnswer = currentQuestion.getCorrectAnswer() + (int)(Math.random() * 10 - 5);
            if (!options.contains(fakeAnswer)) {
                options.add(fakeAnswer);
            }
        }

        Collections.shuffle(options);

        for (int i = 0; i < 4; i++) {
            optionButtons.get(i).setText(String.valueOf(options.get(i)));
        }
    }

    private void handleAnswer(Button btn) {
        int selected = Integer.parseInt(btn.getText());
        if (selected == currentQuestion.getCorrectAnswer()) {
            feedbackLabel.setText("✅ ¡Correcto!");
        } else {
            feedbackLabel.setText("❌ Incorrecto. Intenta de nuevo.");
        }

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                javafx.application.Platform.runLater(this::loadNewQuestion);
            } catch (InterruptedException ignored) {}
        }).start();
    }
}
