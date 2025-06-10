package com.example.mathrush;

import java.util.Random;

public class MathQuestion {
    private final String questionText;
    private final int correctAnswer;

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
        Random rand = new Random();
        int a = rand.nextInt(10) + 1;
        int b = rand.nextInt(10) + 1;
        int op = rand.nextInt(4);
        String question;
        int result;

        switch (op) {
            case 0 -> {
                question = a + " + " + b;
                result = a + b;
            }
            case 1 -> {
                question = a + " - " + b;
                result = a - b;
            }
            case 2 -> {
                question = a + " * " + b;
                result = a * b;
            }
            case 3 -> {
                result = a;
                int prod = a * b;
                question = prod + " / " + b;
            }
            default -> {
                question = "0 + 0";
                result = 0;
            }
        }
        return new MathQuestion(question + " = ?", result);
    }
}
