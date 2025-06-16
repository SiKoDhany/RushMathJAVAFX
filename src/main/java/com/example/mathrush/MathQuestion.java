package com.example.mathrush;

import java.util.Random;

public class MathQuestion {
    private final String questionText;
    private final int correctAnswer;
    
    // Contador para aumentar la dificultad gradualmente
    private static int questionCounter = 0;
    private static final int QUESTIONS_PER_LEVEL = 5;

    public MathQuestion(String questionText, int correctAnswer) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public static MathQuestion generateRandom() {
        // Incrementar contador de preguntas
        questionCounter++;
        
        // Determinar nivel de dificultad según el contador
        int difficulty;
        if (questionCounter <= QUESTIONS_PER_LEVEL) {
            difficulty = 0; // Nivel fácil - primeras 5 preguntas
        } else if (questionCounter <= QUESTIONS_PER_LEVEL * 2) {
            difficulty = 1; // Nivel medio - siguientes 5 preguntas
        } else {
            difficulty = 2; // Nivel difícil - después de 10 preguntas
        }
        
        if (difficulty == 0) {
            return generateSimpleQuestion();
        } else if (difficulty == 1) {
            return generateMediumQuestion();
        } else {
            return generateHardQuestion();
        }
    }
    
    // Resetear el contador para un nuevo juego
    public static void resetCounter() {
        questionCounter = 0;
    }
    
    private static MathQuestion generateSimpleQuestion() {
        Random rand = new Random();
        int a = rand.nextInt(10) + 1;
        int b = rand.nextInt(10) + 1;
        int op = rand.nextInt(4);
        String question;
        int result;

        switch (op) {
            case 0 -> {
                // Suma
                question = a + " + " + b;
                result = a + b;
            }
            case 1 -> {
                // Resta (asegurando resultado positivo)
                if (a < b) {
                    int temp = a;
                    a = b;
                    b = temp;
                }
                question = a + " - " + b;
                result = a - b;
            }
            case 2 -> {
                // Multiplicación
                question = a + " × " + b;
                result = a * b;
            }
            case 3 -> {
                // División (asegurando división exacta)
                result = a;
                int prod = a * b;
                question = prod + " ÷ " + b;
            }
            default -> {
                question = "0 + 0";
                result = 0;
            }
        }
        return new MathQuestion(question + " = ?", result);
    }
    
    private static MathQuestion generateMediumQuestion() {
        Random rand = new Random();
        int type = rand.nextInt(3);
        String question;
        int result;
        
        switch (type) {
            case 0 -> {
                // Operaciones combinadas: suma y resta
                int a = rand.nextInt(10) + 1;
                int b = rand.nextInt(10) + 1;
                int c = rand.nextInt(10) + 1;
                question = a + " + " + b + " - " + c;
                result = a + b - c;
            }
            case 1 -> {
                // Operaciones combinadas: multiplicación y suma
                int a = rand.nextInt(5) + 1;
                int b = rand.nextInt(5) + 1;
                int c = rand.nextInt(5) + 1;
                question = a + " × " + b + " + " + c;
                result = a * b + c;
            }
            case 2 -> {
                // Operaciones combinadas: multiplicación y resta
                int a = rand.nextInt(5) + 1;
                int b = rand.nextInt(5) + 1;
                int c = rand.nextInt(5) + 1;
                // Asegurar que el resultado sea positivo
                if (a * b <= c) {
                    c = a * b - 1;
                }
                question = a + " × " + b + " - " + c;
                result = a * b - c;
            }
            default -> {
                question = "1 + 2";
                result = 3;
            }
        }
        return new MathQuestion(question + " = ?", result);
    }
    
    private static MathQuestion generateHardQuestion() {
        Random rand = new Random();
        int type = rand.nextInt(3);
        String question;
        int result;
        
        switch (type) {
            case 0 -> {
                // Operaciones con paréntesis: (a + b) × c
                int a = rand.nextInt(5) + 1;
                int b = rand.nextInt(5) + 1;
                int c = rand.nextInt(5) + 1;
                question = "(" + a + " + " + b + ") × " + c;
                result = (a + b) * c;
            }
            case 1 -> {
                // Exponentes simples: a² o a³
                int base = rand.nextInt(6) + 2; // 2-7
                int exp = rand.nextInt(2) + 2; // 2-3
                question = base + (exp == 2 ? "²" : "³");
                result = (int) Math.pow(base, exp);
            }
            case 2 -> {
                // Operaciones combinadas con paréntesis: a × (b + c)
                int a = rand.nextInt(5) + 1;
                int b = rand.nextInt(5) + 1;
                int c = rand.nextInt(5) + 1;
                question = a + " × (" + b + " + " + c + ")";
                result = a * (b + c);
            }
            default -> {
                question = "2 + 2";
                result = 4;
            }
        }
        return new MathQuestion(question + " = ?", result);
    }
}
