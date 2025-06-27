package com.example.mathrush;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QuizGame {
    // Componentes de UI
    private final BorderPane root;
    private final Label questionLabel;
    private final Label feedbackLabel;
    private final Label timerLabel;
    private final Label livesLabel;
    private final Label scoreLabel;
    private final List<Button> optionButtons;
    
    // Variables del juego
    private MathQuestion currentQuestion;
    private Timeline timer;
    private int timeRemaining;
    private int lives;
    private int score;
    
    // Executor para manejar tareas en segundo plano
    private final ScheduledExecutorService executor;

    // Constantes de colores
    private static final Color PRIMARY_COLOR = Color.web("#1E88E5");
    private static final Color SECONDARY_COLOR = Color.web("#1565C0");
    private static final Color DARK_BLUE = Color.web("#1A2942");
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = Color.web("#FF9800");
    private static final Color CORRECT_COLOR = Color.web("#2ECC71");
    private static final Color INCORRECT_COLOR = Color.web("#E74C3C");
    private static final Color OPTION_BUTTON_COLOR = Color.web("#6658F5");
    private static final Color OPTION_BUTTON_HOVER_COLOR = Color.web("#5245E3");
    
    // Constantes de estilos CSS
    private static final String PANEL_STYLE = "-fx-background-radius: 15; -fx-padding: 5 15;";
    private static final String BUTTON_STYLE_FORMAT = "-fx-background-color: %s; -fx-background-radius: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);";
    
    // Constantes de dimensiones
    private static final int OPTION_BUTTON_WIDTH = 220;
    private static final int OPTION_BUTTON_HEIGHT = 60;
    private static final int CREDITS_BUTTON_WIDTH = 150;
    private static final int CREDITS_BUTTON_HEIGHT = 40;
    private static final int TITLE_FONT_SIZE = 36;
    private static final int QUESTION_FONT_SIZE = 28;
    private static final int OPTION_FONT_SIZE = 20;
    private static final int FEEDBACK_FONT_SIZE = 20;
    private static final int STATS_FONT_SIZE = 22;
    
    // Constantes de tiempo
    private static final int INITIAL_TIME = 10;
    private static final int DELAY_BETWEEN_QUESTIONS_MS = 1500;

    public QuizGame() {
        // Inicializar variables
        root = new BorderPane();
        questionLabel = new Label();
        feedbackLabel = new Label();
        timerLabel = new Label();
        livesLabel = new Label();
        scoreLabel = new Label();
        optionButtons = new ArrayList<>(4);
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "QuizGame-Background");
            t.setDaemon(true);
            return t;
        });
        
        // Configurar la UI
        setupRootPane();
        setupGameVariables();
        
        // Crear componentes principales
        VBox topContainer = createTopContainer();
        VBox centerContent = createCenterContent();
        
        // Configurar el layout principal
        root.setTop(topContainer);
        root.setCenter(centerContent);
        
        // Iniciar el juego
        loadNewQuestion();
    }
    
    private void setupRootPane() {
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(PRIMARY_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    
    private void setupGameVariables() {
        lives = 3;
        score = 0;
    }
    
    private VBox createTopContainer() {
        // Crear título
        HBox titleBox = createTitleBox();
        
        // Crear panel de estadísticas
        HBox statsBox = createStatsBox();
        
        // Combinar título y estadísticas
        VBox topContainer = new VBox(10);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.getChildren().addAll(titleBox, statsBox);
        
        return topContainer;
    }
    
    private HBox createTitleBox() {
        Label titleLabel = new Label("RUSH MATH");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, TITLE_FONT_SIZE));
        titleLabel.setTextFill(TEXT_COLOR);
        titleLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 3);");
        
        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0, 0, 15, 0));
        
        return titleBox;
    }
    
    private HBox createStatsBox() {
        HBox statsBox = new HBox(30);
        statsBox.setPadding(new Insets(10, 0, 20, 0));
        statsBox.setAlignment(Pos.CENTER);
        
        // Crear componentes de estadísticas
        HBox livesBox = createLivesBox();
        HBox scoreBox = createScoreBox();
        HBox timerBox = createTimerBox();
        
        statsBox.getChildren().addAll(livesBox, scoreBox, timerBox);
        
        return statsBox;
    }
    
    private HBox createLivesBox() {
        HBox livesBox = new HBox(5);
        livesBox.setAlignment(Pos.CENTER);
        livesBox.setStyle("-fx-background-color: " + toRgbString(DARK_BLUE) + "; " + PANEL_STYLE);
        
        Label heartIcon = new Label("❤");
        heartIcon.setFont(Font.font("Arial", FontWeight.BOLD, STATS_FONT_SIZE));
        heartIcon.setTextFill(TEXT_COLOR);
        
        livesLabel.setText(Integer.toString(lives));
        livesLabel.setFont(Font.font("Arial", FontWeight.BOLD, STATS_FONT_SIZE));
        livesLabel.setTextFill(TEXT_COLOR);
        
        livesBox.getChildren().addAll(heartIcon, livesLabel);
        
        return livesBox;
    }
    
    private HBox createScoreBox() {
        HBox scoreBox = new HBox(10);
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setStyle("-fx-background-color: " + toRgbString(DARK_BLUE) + "; " + PANEL_STYLE);
        
        Label scoreTextLabel = new Label("SCORE:");
        scoreTextLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        scoreTextLabel.setTextFill(ACCENT_COLOR);
        
        scoreLabel.setText(Integer.toString(score));
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, STATS_FONT_SIZE));
        scoreLabel.setTextFill(TEXT_COLOR);
        
        scoreBox.getChildren().addAll(scoreTextLabel, scoreLabel);
        
        return scoreBox;
    }
    
    private HBox createTimerBox() {
        HBox timerBox = new HBox();
        timerBox.setAlignment(Pos.CENTER);
        timerBox.setStyle("-fx-background-color: " + toRgbString(DARK_BLUE) + "; " + PANEL_STYLE);
        
        Label clockIcon = new Label("⏱");
        clockIcon.setFont(Font.font("Arial", FontWeight.BOLD, STATS_FONT_SIZE));
        clockIcon.setTextFill(ACCENT_COLOR);
        
        timerLabel.setText(String.valueOf(INITIAL_TIME));
        timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        timerLabel.setTextFill(TEXT_COLOR);
        timerLabel.setMinWidth(30);
        timerLabel.setAlignment(Pos.CENTER);
        
        timerBox.getChildren().addAll(clockIcon, timerLabel);
        
        return timerBox;
    }
    
    private VBox createCenterContent() {
        // Crear panel de pregunta
        VBox questionBox = createQuestionBox();
        
        // Crear panel de opciones
        VBox optionsBox = createOptionsBox();
        
        // Configurar etiqueta de retroalimentación
        setupFeedbackLabel();
        
        // Crear botón de créditos
        HBox creditsBox = createCreditsBox();
        
        // Combinar todos los elementos
        VBox centerContent = new VBox(20);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.getChildren().addAll(questionBox, optionsBox, feedbackLabel, creditsBox);
        
        return centerContent;
    }
    
    private VBox createQuestionBox() {
        VBox questionBox = new VBox(25);
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setPadding(new Insets(20));
        questionBox.setStyle("-fx-background-color: " + toRgbString(DARK_BLUE) + "; -fx-background-radius: 15;");
        questionBox.setMaxWidth(500);
        
        setupQuestionLabel();
        questionBox.getChildren().add(questionLabel);
        
        return questionBox;
    }
    
    private void setupQuestionLabel() {
        questionLabel.setFont(Font.font("Arial", FontWeight.BOLD, QUESTION_FONT_SIZE));
        questionLabel.setTextFill(TEXT_COLOR);
        questionLabel.setWrapText(true);
        questionLabel.setTextAlignment(TextAlignment.CENTER);
    }
    
    private VBox createOptionsBox() {
        VBox optionsBox = new VBox(15);
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setPadding(new Insets(20, 0, 20, 0));
        
        // Crear botones en una disposición 2x2
        HBox row1 = new HBox(15);
        row1.setAlignment(Pos.CENTER);
        
        HBox row2 = new HBox(15);
        row2.setAlignment(Pos.CENTER);
        
        // Crear los 4 botones de opciones
        for (int i = 0; i < 4; i++) {
            Button btn = createOptionButton();
            optionButtons.add(btn);
            
            // Añadir a la fila correspondiente
            if (i < 2) {
                row1.getChildren().add(btn);
            } else {
                row2.getChildren().add(btn);
            }
        }
        
        optionsBox.getChildren().addAll(row1, row2);
        
        return optionsBox;
    }
    
    private Button createOptionButton() {
        Button btn = new Button();
        btn.setPrefWidth(OPTION_BUTTON_WIDTH);
        btn.setPrefHeight(OPTION_BUTTON_HEIGHT);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, OPTION_FONT_SIZE));
        btn.setTextFill(TEXT_COLOR);
        
        // Estilo del botón
        String buttonStyle = String.format(BUTTON_STYLE_FORMAT, toRgbString(OPTION_BUTTON_COLOR));
        btn.setStyle(buttonStyle);
        
        // Efectos de hover
        setupButtonHoverEffects(btn, OPTION_BUTTON_COLOR, OPTION_BUTTON_HOVER_COLOR);
        
        btn.setOnAction(e -> handleAnswer(btn));
        
        return btn;
    }
    
    private void setupButtonHoverEffects(Button btn, Color normalColor, Color hoverColor) {
        String normalStyle = String.format(BUTTON_STYLE_FORMAT, toRgbString(normalColor));
        String hoverStyle = String.format(BUTTON_STYLE_FORMAT, toRgbString(hoverColor));
        
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(normalStyle));
    }
    
    private void setupFeedbackLabel() {
        feedbackLabel.setFont(Font.font("Arial", FontWeight.BOLD, FEEDBACK_FONT_SIZE));
        feedbackLabel.setTextAlignment(TextAlignment.CENTER);
        feedbackLabel.setWrapText(true);
        feedbackLabel.setPadding(new Insets(10, 0, 0, 0));
        feedbackLabel.setTextFill(TEXT_COLOR);
    }
    
    private HBox createCreditsBox() {
        Button creditsButton = new Button("Créditos");
        creditsButton.setPrefWidth(CREDITS_BUTTON_WIDTH);
        creditsButton.setPrefHeight(CREDITS_BUTTON_HEIGHT);
        creditsButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        creditsButton.setTextFill(TEXT_COLOR);
        
        String buttonStyle = String.format(BUTTON_STYLE_FORMAT, toRgbString(ACCENT_COLOR));
        creditsButton.setStyle(buttonStyle);
        
        setupButtonHoverEffects(creditsButton, ACCENT_COLOR, Color.web("#F57C00"));
        
        creditsButton.setOnAction(e -> showCredits());
        
        HBox creditsBox = new HBox(creditsButton);
        creditsBox.setAlignment(Pos.CENTER);
        creditsBox.setPadding(new Insets(15, 0, 0, 0));
        
        return creditsBox;
    }
    
    private void showCredits() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Créditos");
        alert.setHeaderText("Desarrollado por:");
        alert.setContentText("Daniel Felipe Cantor Sequeda\n" +
                           "Paula Cardona Duque\n" +
                           "Anderson Castro Franco\n" +
                           "Juan Guillermo Chacon Garcia\n" +
                           "Joan Morales Pérez");
        alert.showAndWait();
    }

    public BorderPane getRoot() {
        return root;
    }
    
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
        
        timeRemaining = INITIAL_TIME;
        timerLabel.setText(String.valueOf(timeRemaining));
        timerLabel.setTextFill(TEXT_COLOR);
        timerLabel.setStyle("");
        
        timer = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                timeRemaining--;
                timerLabel.setText(String.valueOf(timeRemaining));
                
                updateTimerAppearance();
                
                if (timeRemaining <= 0) {
                    timer.stop();
                    handleTimeOut();
                }
            })
        );
        
        timer.setCycleCount(INITIAL_TIME + 1);
        timer.play();
    }
    
    private void updateTimerAppearance() {
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
    }
    
    private void handleTimeOut() {
        feedbackLabel.setText("⏰ ¡Se acabó el tiempo!");
        feedbackLabel.setTextFill(Color.RED);
        decreaseLife();
        
        scheduleNextQuestion();
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
        
        // Crear la pantalla de fin de juego
        VBox gameOverBox = createGameOverBox();
        
        // Actualizar la interfaz
        VBox centerContent = new VBox();
        centerContent.setAlignment(Pos.CENTER);
        centerContent.getChildren().add(gameOverBox);
        
        root.setTop(null);
        root.setCenter(centerContent);
    }
    
    private VBox createGameOverBox() {
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
        Button restartButton = createRestartButton();
        
        gameOverBox.getChildren().addAll(gameOverLabel, finalScoreLabel, restartButton);
        
        return gameOverBox;
    }
    
    private Button createRestartButton() {
        Button restartButton = new Button("Jugar de nuevo");
        restartButton.setPrefWidth(250);
        restartButton.setPrefHeight(60);
        restartButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        restartButton.setTextFill(TEXT_COLOR);
        
        String buttonStyle = String.format(BUTTON_STYLE_FORMAT, toRgbString(ACCENT_COLOR));
        restartButton.setStyle(buttonStyle);
        
        setupButtonHoverEffects(restartButton, ACCENT_COLOR, Color.web("#F57C00"));
        
        restartButton.setOnAction(e -> resetGame());
        
        return restartButton;
    }
    
    private void resetGame() {
        // Reiniciar las variables del juego
        lives = 3;
        score = 0;
        
        // Reiniciar el contador de preguntas para comenzar con nivel fácil
        MathQuestion.resetCounter();
        
        // Recrear la interfaz de usuario
        VBox topContainer = createTopContainer();
        VBox centerContent = createCenterContent();
        
        root.setTop(topContainer);
        root.setCenter(centerContent);
        
        // Comenzar un nuevo juego
        loadNewQuestion();
    }

    private void loadNewQuestion() {
        currentQuestion = MathQuestion.generateRandom();
        questionLabel.setText(currentQuestion.getQuestionText());
        feedbackLabel.setText("");
        
        generateAnswerOptions();
        
        // Iniciar el temporizador
        startTimer();
    }
    
    private void generateAnswerOptions() {
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
        String buttonStyle = String.format(BUTTON_STYLE_FORMAT, toRgbString(OPTION_BUTTON_COLOR));

        for (int i = 0; i < 4; i++) {
            Button btn = optionButtons.get(i);
            btn.setText(String.valueOf(options.get(i)));
            btn.setStyle(buttonStyle);
            btn.setDisable(false);
            
            setupButtonHoverEffects(btn, OPTION_BUTTON_COLOR, OPTION_BUTTON_HOVER_COLOR);
        }
    }

    private void handleAnswer(Button btn) {
        // Detener el temporizador
        if (timer != null) {
            timer.stop();
        }
        
        // Deshabilitar botones para evitar múltiples respuestas
        optionButtons.forEach(button -> button.setDisable(true));
        
        int selected = Integer.parseInt(btn.getText());
        boolean isCorrect = selected == currentQuestion.getCorrectAnswer();
        
        updateFeedbackForAnswer(isCorrect, btn);
        
        // Si aún quedan vidas, cargar la siguiente pregunta después de un breve retraso
        if (lives > 0) {
            scheduleNextQuestion();
        }
    }
    
    private void updateFeedbackForAnswer(boolean isCorrect, Button selectedButton) {
        if (isCorrect) {
            feedbackLabel.setText("✅ ¡Correcto!");
            feedbackLabel.setTextFill(CORRECT_COLOR);
            selectedButton.setStyle(String.format(BUTTON_STYLE_FORMAT, toRgbString(CORRECT_COLOR)));
            increaseScore();
        } else {
            feedbackLabel.setText("❌ Incorrecto. La respuesta correcta era: " + currentQuestion.getCorrectAnswer());
            feedbackLabel.setTextFill(INCORRECT_COLOR);
            selectedButton.setStyle(String.format(BUTTON_STYLE_FORMAT, toRgbString(INCORRECT_COLOR)));
            decreaseLife();
            
            // Resaltar el botón correcto
            highlightCorrectAnswer();
        }
    }
    
    private void highlightCorrectAnswer() {
        for (Button button : optionButtons) {
            if (Integer.parseInt(button.getText()) == currentQuestion.getCorrectAnswer()) {
                button.setStyle(String.format(BUTTON_STYLE_FORMAT, toRgbString(CORRECT_COLOR)));
                break;
            }
        }
    }
    
    private void scheduleNextQuestion() {
        executor.schedule(() -> Platform.runLater(this::loadNewQuestion), 
                        DELAY_BETWEEN_QUESTIONS_MS, TimeUnit.MILLISECONDS);
    }
    
    // Método para limpiar recursos al cerrar la aplicación
    public void shutdown() {
        if (timer != null) {
            timer.stop();
        }
        
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
    }
}
