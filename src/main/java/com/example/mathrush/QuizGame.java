package com.example.mathrush;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizGame {
    private final BorderPane root;
    private final Label questionLabel;
    private final Label feedbackLabel;
    private final Label timerLabel;
    private final Label livesLabel;
    private final Label scoreLabel;
    private final List<Button> optionButtons;
    
    private MathQuestion currentQuestion;
    private Timeline timer;
    private int timeRemaining;
    private int lives;
    private int score;

    // Colores para el tema basado en la segunda imagen
    private static final Color PRIMARY_COLOR = Color.web("#1E88E5"); // Azul principal (fondo)
    private static final Color SECONDARY_COLOR = Color.web("#1565C0"); // Azul oscuro
    private static final Color DARK_BLUE = Color.web("#1A2942"); // Azul muy oscuro para paneles
    private static final Color TEXT_COLOR = Color.WHITE; // Texto blanco
    private static final Color ACCENT_COLOR = Color.web("#FF9800"); // Naranja para botones y acentos

    public QuizGame() {
        // Usar BorderPane como root para una mejor organización
        root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(PRIMARY_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Inicializar variables de juego
        lives = 3;
        score = 0;
        
        // Panel superior para estadísticas
        HBox statsBox = new HBox(30);
        statsBox.setPadding(new Insets(10, 0, 20, 0));
        statsBox.setAlignment(Pos.CENTER);
        
        // Sección de vidas con icono de corazón (sin el cuadrado)
        HBox livesBox = new HBox(5);
        livesBox.setAlignment(Pos.CENTER);
        livesBox.setStyle("-fx-background-color: " + toRgbString(DARK_BLUE) + "; -fx-background-radius: 15; -fx-padding: 5 15;");
        
        Label heartIcon = new Label("❤");
        heartIcon.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        heartIcon.setTextFill(TEXT_COLOR);
        
        livesLabel = new Label(Integer.toString(lives));
        livesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        livesLabel.setTextFill(TEXT_COLOR);
        
        livesBox.getChildren().addAll(heartIcon, livesLabel);
        
        // Sección de puntuación con etiqueta "SCORE:"
        HBox scoreBox = new HBox(10);
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setStyle("-fx-background-color: " + toRgbString(DARK_BLUE) + "; -fx-background-radius: 15; -fx-padding: 5 15;");
        
        Label scoreTextLabel = new Label("SCORE:");
        scoreTextLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        scoreTextLabel.setTextFill(ACCENT_COLOR);
        
        scoreLabel = new Label(Integer.toString(score));
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        scoreLabel.setTextFill(TEXT_COLOR);
        
        scoreBox.getChildren().addAll(scoreTextLabel, scoreLabel);
        
        // Temporizador con estilo
        HBox timerBox = new HBox();
        timerBox.setAlignment(Pos.CENTER);
        timerBox.setStyle("-fx-background-color: " + toRgbString(DARK_BLUE) + "; -fx-background-radius: 15; -fx-padding: 5 15;");
        
        // Ícono de reloj
        Label clockIcon = new Label("⏱");
        clockIcon.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        clockIcon.setTextFill(ACCENT_COLOR);
        
        timerLabel = new Label("10");
        timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        timerLabel.setTextFill(TEXT_COLOR);
        timerLabel.setMinWidth(30);
        timerLabel.setAlignment(Pos.CENTER);
        
        timerBox.getChildren().addAll(clockIcon, timerLabel);
        
        statsBox.getChildren().addAll(livesBox, scoreBox, timerBox);
        
        // Panel central para la pregunta
        VBox questionBox = new VBox(25);
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setPadding(new Insets(20));
        questionBox.setStyle("-fx-background-color: " + toRgbString(DARK_BLUE) + "; -fx-background-radius: 15;");
        questionBox.setMaxWidth(500);
        
        // Etiqueta de pregunta con estilo
        questionLabel = new Label();
        questionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        questionLabel.setTextFill(TEXT_COLOR);
        questionLabel.setWrapText(true);
        questionLabel.setTextAlignment(TextAlignment.CENTER);
        
        questionBox.getChildren().add(questionLabel);
        
        // Crear botones de opciones con estilo
        VBox optionsBox = new VBox(15);
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setPadding(new Insets(20, 0, 20, 0));
        
        optionButtons = new ArrayList<>();
        
        // Crear botones en una disposición 2x2
        HBox row1 = new HBox(15);
        row1.setAlignment(Pos.CENTER);
        
        HBox row2 = new HBox(15);
        row2.setAlignment(Pos.CENTER);
        
        for (int i = 0; i < 4; i++) {
            Button btn = new Button();
            btn.setPrefWidth(220);
            btn.setPrefHeight(60);
            btn.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            btn.setTextFill(TEXT_COLOR);
            
            // Estilo moderno para botones
            String buttonStyle = "-fx-background-color: " + toRgbString(Color.web("#6658F5")) + "; " +
                               "-fx-background-radius: 30; " +
                               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);";
            
            btn.setStyle(buttonStyle);
            
            // Efectos de hover
            btn.setOnMouseEntered(e -> btn.setStyle(buttonStyle.replace(toRgbString(Color.web("#6658F5")), 
                                                                         toRgbString(Color.web("#5245E3")))));
            btn.setOnMouseExited(e -> btn.setStyle(buttonStyle));
            
            btn.setOnAction(e -> handleAnswer(btn));
            optionButtons.add(btn);
            
            // Añadir a la fila correspondiente
            if (i < 2) {
                row1.getChildren().add(btn);
            } else {
                row2.getChildren().add(btn);
            }
        }
        
        optionsBox.getChildren().addAll(row1, row2);
        
        // Etiqueta de retroalimentación
        feedbackLabel = new Label();
        feedbackLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        feedbackLabel.setTextAlignment(TextAlignment.CENTER);
        feedbackLabel.setWrapText(true);
        feedbackLabel.setPadding(new Insets(10, 0, 0, 0));
        feedbackLabel.setTextFill(TEXT_COLOR);
        
        // Estructurar el layout
        VBox centerContent = new VBox(20);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.getChildren().addAll(questionBox, optionsBox, feedbackLabel);
        
        root.setTop(statsBox);
        root.setCenter(centerContent);
        
        // Iniciar el juego
        loadNewQuestion();
    }

    public BorderPane getRoot() {
        return root;
    }
    
    // Convertir Color a formato RGB para CSS
    private String toRgbString(Color color) {
        return String.format("rgb(%d, %d, %d)", 
                            (int) (color.getRed() * 255), 
                            (int) (color.getGreen() * 255), 
                            (int) (color.getBlue() * 255));
    }
    
    private void startTimer() {
        // Detener el temporizador existente si está corriendo
        if (timer != null) {
            timer.stop();
        }
        
        timeRemaining = 10;
        timerLabel.setText(String.valueOf(timeRemaining));
        
        timer = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                timeRemaining--;
                timerLabel.setText(String.valueOf(timeRemaining));
                
                // Cambiar color según el tiempo restante
                if (timeRemaining <= 3) {
                    timerLabel.setTextFill(Color.RED);
                    // Parpadeo del fondo
                    if (timeRemaining % 2 == 0) {
                        timerLabel.setStyle("-fx-text-fill: red;");
                    } else {
                        timerLabel.setStyle("-fx-text-fill: white;");
                    }
                } else {
                    timerLabel.setTextFill(TEXT_COLOR);
                }
                
                if (timeRemaining <= 0) {
                    timer.stop();
                    handleTimeOut();
                }
            })
        );
        
        timer.setCycleCount(11); // 0-10 segundos
        timer.play();
    }
    
    private void handleTimeOut() {
        feedbackLabel.setText("⏰ ¡Se acabó el tiempo!");
        feedbackLabel.setTextFill(Color.RED);
        decreaseLife();
        
        // Esperar un segundo antes de cargar la siguiente pregunta
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                javafx.application.Platform.runLater(this::loadNewQuestion);
            } catch (InterruptedException ignored) {}
        }).start();
    }
    
    private void decreaseLife() {
        lives--;
        livesLabel.setText(Integer.toString(lives));
        
        if (lives <= 0) {
            endGame();
        }
    }
    
    private void increaseScore() {
        score += 10;
        scoreLabel.setText(Integer.toString(score));
    }
    
    private void endGame() {
        // Detener el temporizador
        if (timer != null) {
            timer.stop();
        }
        
        // Preparar la pantalla de fin de juego
        VBox gameOverBox = new VBox(25);
        gameOverBox.setAlignment(Pos.CENTER);
        gameOverBox.setStyle("-fx-background-color: " + toRgbString(DARK_BLUE) + "; -fx-background-radius: 15; -fx-padding: 20;");
        gameOverBox.setMaxWidth(400);
        gameOverBox.setMaxHeight(300);
        
        // Título de fin de juego
        Label gameOverLabel = new Label("¡JUEGO TERMINADO!");
        gameOverLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        gameOverLabel.setTextFill(TEXT_COLOR);
        
        // Puntuación final
        Label finalScoreLabel = new Label("Puntuación final: " + score);
        finalScoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        finalScoreLabel.setTextFill(TEXT_COLOR);
        
        // Botón para reiniciar
        Button restartButton = new Button("Jugar de nuevo");
        restartButton.setPrefWidth(250);
        restartButton.setPrefHeight(60);
        restartButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        restartButton.setTextFill(TEXT_COLOR);
        
        // Estilo del botón
        String buttonStyle = "-fx-background-color: " + toRgbString(ACCENT_COLOR) + "; " +
                           "-fx-background-radius: 30; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);";
        
        restartButton.setStyle(buttonStyle);
        
        // Efectos de hover
        restartButton.setOnMouseEntered(e -> restartButton.setStyle(buttonStyle.replace(toRgbString(ACCENT_COLOR), 
                                                                     toRgbString(Color.web("#F57C00")))));
        restartButton.setOnMouseExited(e -> restartButton.setStyle(buttonStyle));
        
        restartButton.setOnAction(e -> resetGame());
        
        gameOverBox.getChildren().addAll(gameOverLabel, finalScoreLabel, restartButton);
        
        // Actualizar la interfaz
        VBox centerContent = new VBox();
        centerContent.setAlignment(Pos.CENTER);
        centerContent.getChildren().add(gameOverBox);
        
        root.setTop(null);
        root.setCenter(centerContent);
    }
    
    private void resetGame() {
        // Reiniciar las variables del juego
        lives = 3;
        score = 0;
        
        // Reiniciar el contador de preguntas para comenzar con nivel fácil
        MathQuestion.resetCounter();
        
        // Panel superior para estadísticas
        HBox statsBox = new HBox(30);
        statsBox.setPadding(new Insets(10, 0, 20, 0));
        statsBox.setAlignment(Pos.CENTER);
        
        // Sección de vidas con icono de corazón (sin el cuadrado)
        HBox livesBox = new HBox(5);
        livesBox.setAlignment(Pos.CENTER);
        livesBox.setStyle("-fx-background-color: " + toRgbString(DARK_BLUE) + "; -fx-background-radius: 15; -fx-padding: 5 15;");
        
        Label heartIcon = new Label("❤");
        heartIcon.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        heartIcon.setTextFill(TEXT_COLOR);
        
        livesLabel.setText(Integer.toString(lives));
        livesLabel.setTextFill(TEXT_COLOR);
        
        livesBox.getChildren().addAll(heartIcon, livesLabel);
        
        // Sección de puntuación con etiqueta "SCORE:"
        HBox scoreBox = new HBox(10);
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setStyle("-fx-background-color: " + toRgbString(DARK_BLUE) + "; -fx-background-radius: 15; -fx-padding: 5 15;");
        
        Label scoreTextLabel = new Label("SCORE:");
        scoreTextLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        scoreTextLabel.setTextFill(ACCENT_COLOR);
        
        scoreLabel.setText(Integer.toString(score));
        scoreLabel.setTextFill(TEXT_COLOR);
        
        scoreBox.getChildren().addAll(scoreTextLabel, scoreLabel);
        
        // Temporizador con estilo
        HBox timerBox = new HBox();
        timerBox.setAlignment(Pos.CENTER);
        timerBox.setStyle("-fx-background-color: " + toRgbString(DARK_BLUE) + "; -fx-background-radius: 15; -fx-padding: 5 15;");
        
        // Ícono de reloj
        Label clockIcon = new Label("⏱");
        clockIcon.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        clockIcon.setTextFill(ACCENT_COLOR);
        
        timerLabel.setText("10");
        timerLabel.setTextFill(TEXT_COLOR);
        
        timerBox.getChildren().addAll(clockIcon, timerLabel);
        
        statsBox.getChildren().addAll(livesBox, scoreBox, timerBox);
        
        // Panel central para la pregunta
        VBox questionBox = new VBox(25);
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setPadding(new Insets(20));
        questionBox.setStyle("-fx-background-color: " + toRgbString(DARK_BLUE) + "; -fx-background-radius: 15;");
        questionBox.setMaxWidth(500);
        
        questionBox.getChildren().add(questionLabel);
        
        // Crear botones de opciones con estilo
        VBox optionsBox = new VBox(15);
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setPadding(new Insets(20, 0, 20, 0));
        
        // Crear botones en una disposición 2x2
        HBox row1 = new HBox(15);
        row1.setAlignment(Pos.CENTER);
        row1.getChildren().addAll(optionButtons.get(0), optionButtons.get(1));
        
        HBox row2 = new HBox(15);
        row2.setAlignment(Pos.CENTER);
        row2.getChildren().addAll(optionButtons.get(2), optionButtons.get(3));
        
        optionsBox.getChildren().addAll(row1, row2);
        
        // Estructurar el layout
        VBox centerContent = new VBox(20);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.getChildren().addAll(questionBox, optionsBox, feedbackLabel);
        
        root.setTop(statsBox);
        root.setCenter(centerContent);
        
        // Comenzar un nuevo juego
        loadNewQuestion();
    }

    private void loadNewQuestion() {
        currentQuestion = MathQuestion.generateRandom();
        questionLabel.setText(currentQuestion.getQuestionText());
        feedbackLabel.setText("");
        
        // Generar las opciones de respuesta
        List<Integer> options = new ArrayList<>();
        options.add(currentQuestion.getCorrectAnswer());

        while (options.size() < 4) {
            int fakeAnswer = currentQuestion.getCorrectAnswer() + (int)(Math.random() * 10 - 5);
            if (!options.contains(fakeAnswer)) {
                options.add(fakeAnswer);
            }
        }

        Collections.shuffle(options);

        // Estilo de botón normal
        String buttonStyle = "-fx-background-color: " + toRgbString(Color.web("#6658F5")) + "; " +
                           "-fx-background-radius: 30; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);";

        for (int i = 0; i < 4; i++) {
            optionButtons.get(i).setText(String.valueOf(options.get(i)));
            optionButtons.get(i).setStyle(buttonStyle);
            optionButtons.get(i).setDisable(false);
            
            // Actualizar los handlers de eventos
            int index = i; // Capturar el índice para el lambda
            optionButtons.get(i).setOnMouseEntered(e -> 
                optionButtons.get(index).setStyle(buttonStyle.replace(toRgbString(Color.web("#6658F5")), 
                                                                     toRgbString(Color.web("#5245E3")))));
            optionButtons.get(i).setOnMouseExited(e -> 
                optionButtons.get(index).setStyle(buttonStyle));
        }
        
        // Iniciar el temporizador
        startTimer();
    }

    private void handleAnswer(Button btn) {
        // Detener el temporizador
        if (timer != null) {
            timer.stop();
        }
        
        // Deshabilitar botones para evitar múltiples respuestas
        for (Button button : optionButtons) {
            button.setDisable(true);
        }
        
        int selected = Integer.parseInt(btn.getText());
        if (selected == currentQuestion.getCorrectAnswer()) {
            feedbackLabel.setText("✅ ¡Correcto!");
            feedbackLabel.setTextFill(Color.web("#2ECC71"));
            btn.setStyle("-fx-background-color: #2ECC71; -fx-background-radius: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);");
            increaseScore();
        } else {
            feedbackLabel.setText("❌ Incorrecto. La respuesta correcta era: " + currentQuestion.getCorrectAnswer());
            feedbackLabel.setTextFill(Color.web("#E74C3C"));
            btn.setStyle("-fx-background-color: #E74C3C; -fx-background-radius: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);");
            decreaseLife();
            
            // Resaltar el botón correcto
            for (Button button : optionButtons) {
                if (Integer.parseInt(button.getText()) == currentQuestion.getCorrectAnswer()) {
                    button.setStyle("-fx-background-color: #2ECC71; -fx-background-radius: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);");
                    break;
                }
            }
        }

        // Si aún quedan vidas, cargar la siguiente pregunta después de un breve retraso
        if (lives > 0) {
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(this::loadNewQuestion);
                } catch (InterruptedException ignored) {}
            }).start();
        }
    }
}
