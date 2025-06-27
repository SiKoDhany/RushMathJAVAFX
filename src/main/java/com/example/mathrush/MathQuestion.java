package com.example.mathrush;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Clase que representa una pregunta matemática y proporciona métodos para generar
 * preguntas aleatorias de diferentes niveles de dificultad.
 */
public class MathQuestion {
    private final String questionText;
    private final int correctAnswer;
    
    // Contador para aumentar la dificultad gradualmente
    private static int questionCounter = 0;
    
    // Constantes para la dificultad
    private static final int QUESTIONS_PER_LEVEL = 5;
    private static final int EASY_LEVEL = 0;
    private static final int MEDIUM_LEVEL = 1;
    private static final int HARD_LEVEL = 2;
    
    // Rangos de números para las preguntas según el nivel
    private static final int EASY_MAX_NUMBER = 10;
    private static final int MEDIUM_MAX_NUMBER = 15;
    private static final int HARD_MAX_NUMBER = 20;
    
    // Operaciones matemáticas
    private static final int OPERATION_ADD = 0;
    private static final int OPERATION_SUBTRACT = 1;
    private static final int OPERATION_MULTIPLY = 2;
    private static final int OPERATION_DIVIDE = 3;

    /**
     * Constructor para crear una pregunta matemática.
     * 
     * @param questionText El texto de la pregunta
     * @param correctAnswer La respuesta correcta
     */
    public MathQuestion(String questionText, int correctAnswer) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    /**
     * Obtiene el texto de la pregunta.
     * 
     * @return El texto de la pregunta
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Obtiene la respuesta correcta.
     * 
     * @return La respuesta correcta
     */
    public int getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Genera una pregunta matemática aleatoria con dificultad progresiva.
     * 
     * @return Una nueva pregunta matemática
     */
    public static MathQuestion generateRandom() {
        // Incrementar contador de preguntas
        questionCounter++;
        
        // Determinar nivel de dificultad según el contador
        int difficulty = getDifficultyLevel();
        
        return switch (difficulty) {
            case EASY_LEVEL -> generateSimpleQuestion();
            case MEDIUM_LEVEL -> generateMediumQuestion();
            case HARD_LEVEL -> generateHardQuestion();
            default -> generateSimpleQuestion();
        };
    }
    
    /**
     * Determina el nivel de dificultad actual basado en el contador de preguntas.
     * 
     * @return El nivel de dificultad (0: fácil, 1: medio, 2: difícil)
     */
    private static int getDifficultyLevel() {
        if (questionCounter <= QUESTIONS_PER_LEVEL) {
            return EASY_LEVEL;
        } else if (questionCounter <= QUESTIONS_PER_LEVEL * 2) {
            return MEDIUM_LEVEL;
        } else {
            return HARD_LEVEL;
        }
    }
    
    /**
     * Resetea el contador para un nuevo juego.
     */
    public static void resetCounter() {
        questionCounter = 0;
    }
    
    /**
     * Genera una pregunta matemática simple (nivel fácil).
     * 
     * @return Una pregunta matemática de nivel fácil
     */
    private static MathQuestion generateSimpleQuestion() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int operation = rand.nextInt(4); // 0: suma, 1: resta, 2: multiplicación, 3: división
        
        return switch (operation) {
            case OPERATION_ADD -> generateAdditionQuestion(EASY_MAX_NUMBER);
            case OPERATION_SUBTRACT -> generateSubtractionQuestion(EASY_MAX_NUMBER);
            case OPERATION_MULTIPLY -> generateMultiplicationQuestion(EASY_MAX_NUMBER);
            case OPERATION_DIVIDE -> generateDivisionQuestion(EASY_MAX_NUMBER);
            default -> generateAdditionQuestion(EASY_MAX_NUMBER);
        };
    }
    
    /**
     * Genera una pregunta matemática de nivel medio.
     * 
     * @return Una pregunta matemática de nivel medio
     */
    private static MathQuestion generateMediumQuestion() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int type = rand.nextInt(3);
        
        return switch (type) {
            case 0 -> generateCombinedAddSubtractQuestion(MEDIUM_MAX_NUMBER);
            case 1 -> generateCombinedMultiplyAddQuestion(MEDIUM_MAX_NUMBER);
            case 2 -> generateCombinedMultiplySubtractQuestion(MEDIUM_MAX_NUMBER);
            default -> generateCombinedAddSubtractQuestion(MEDIUM_MAX_NUMBER);
        };
    }
    
    /**
     * Genera una pregunta matemática de nivel difícil.
     * 
     * @return Una pregunta matemática de nivel difícil
     */
    private static MathQuestion generateHardQuestion() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int type = rand.nextInt(3);
        
        return switch (type) {
            case 0 -> generateParenthesisAddMultiplyQuestion(HARD_MAX_NUMBER);
            case 1 -> generateExponentQuestion(HARD_MAX_NUMBER);
            case 2 -> generateParenthesisMultiplyAddQuestion(HARD_MAX_NUMBER);
            default -> generateParenthesisAddMultiplyQuestion(HARD_MAX_NUMBER);
        };
    }
    
    /**
     * Genera una pregunta de suma.
     * 
     * @param maxNumber El número máximo para los operandos
     * @return Una pregunta de suma
     */
    private static MathQuestion generateAdditionQuestion(int maxNumber) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int a = rand.nextInt(1, maxNumber + 1);
        int b = rand.nextInt(1, maxNumber + 1);
        
        String question = a + " + " + b;
        int result = a + b;
        
        return new MathQuestion(question + " = ?", result);
    }
    
    /**
     * Genera una pregunta de resta.
     * 
     * @param maxNumber El número máximo para los operandos
     * @return Una pregunta de resta
     */
    private static MathQuestion generateSubtractionQuestion(int maxNumber) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int a = rand.nextInt(1, maxNumber + 1);
        int b = rand.nextInt(1, maxNumber + 1);
        
        // Asegurar resultado positivo
        if (a < b) {
            int temp = a;
            a = b;
            b = temp;
        }
        
        String question = a + " - " + b;
        int result = a - b;
        
        return new MathQuestion(question + " = ?", result);
    }
    
    /**
     * Genera una pregunta de multiplicación.
     * 
     * @param maxNumber El número máximo para los operandos
     * @return Una pregunta de multiplicación
     */
    private static MathQuestion generateMultiplicationQuestion(int maxNumber) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int a = rand.nextInt(1, Math.min(maxNumber, 10) + 1); // Limitar para que no sea muy difícil
        int b = rand.nextInt(1, Math.min(maxNumber, 10) + 1);
        
        String question = a + " × " + b;
        int result = a * b;
        
        return new MathQuestion(question + " = ?", result);
    }
    
    /**
     * Genera una pregunta de división.
     * 
     * @param maxNumber El número máximo para los operandos
     * @return Una pregunta de división
     */
    private static MathQuestion generateDivisionQuestion(int maxNumber) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int divisor = rand.nextInt(1, Math.min(maxNumber, 10) + 1);
        int result = rand.nextInt(1, Math.min(maxNumber, 10) + 1);
        
        // Calcular el dividendo para asegurar división exacta
        int dividend = result * divisor;
        
        String question = dividend + " ÷ " + divisor;
        
        return new MathQuestion(question + " = ?", result);
    }
    
    /**
     * Genera una pregunta combinada de suma y resta.
     * 
     * @param maxNumber El número máximo para los operandos
     * @return Una pregunta combinada de suma y resta
     */
    private static MathQuestion generateCombinedAddSubtractQuestion(int maxNumber) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int a = rand.nextInt(1, maxNumber + 1);
        int b = rand.nextInt(1, maxNumber + 1);
        int c = rand.nextInt(1, maxNumber + 1);
        
        String question = a + " + " + b + " - " + c;
        int result = a + b - c;
        
        return new MathQuestion(question + " = ?", result);
    }
    
    /**
     * Genera una pregunta combinada de multiplicación y suma.
     * 
     * @param maxNumber El número máximo para los operandos
     * @return Una pregunta combinada de multiplicación y suma
     */
    private static MathQuestion generateCombinedMultiplyAddQuestion(int maxNumber) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int a = rand.nextInt(1, Math.min(maxNumber, 5) + 1);
        int b = rand.nextInt(1, Math.min(maxNumber, 5) + 1);
        int c = rand.nextInt(1, maxNumber + 1);
        
        String question = a + " × " + b + " + " + c;
        int result = a * b + c;
        
        return new MathQuestion(question + " = ?", result);
    }
    
    /**
     * Genera una pregunta combinada de multiplicación y resta.
     * 
     * @param maxNumber El número máximo para los operandos
     * @return Una pregunta combinada de multiplicación y resta
     */
    private static MathQuestion generateCombinedMultiplySubtractQuestion(int maxNumber) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int a = rand.nextInt(1, Math.min(maxNumber, 5) + 1);
        int b = rand.nextInt(1, Math.min(maxNumber, 5) + 1);
        int product = a * b;
        // Asegurar que el resultado sea positivo
        int c = rand.nextInt(1, product);
        
        String question = a + " × " + b + " - " + c;
        int result = product - c;
        
        return new MathQuestion(question + " = ?", result);
    }
    
    /**
     * Genera una pregunta con paréntesis: (a + b) × c
     * 
     * @param maxNumber El número máximo para los operandos
     * @return Una pregunta con paréntesis
     */
    private static MathQuestion generateParenthesisAddMultiplyQuestion(int maxNumber) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int a = rand.nextInt(1, Math.min(maxNumber, 10) + 1);
        int b = rand.nextInt(1, Math.min(maxNumber, 10) + 1);
        int c = rand.nextInt(1, Math.min(maxNumber, 5) + 1);
        
        String question = "(" + a + " + " + b + ") × " + c;
        int result = (a + b) * c;
        
        return new MathQuestion(question + " = ?", result);
    }
    
    /**
     * Genera una pregunta con exponentes: a² o a³
     * 
     * @param maxNumber El número máximo para la base
     * @return Una pregunta con exponentes
     */
    private static MathQuestion generateExponentQuestion(int maxNumber) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int base = rand.nextInt(2, Math.min(maxNumber, 8) + 1);
        int exp = rand.nextInt(2, 4); // 2 o 3
        
        String question = base + (exp == 2 ? "²" : "³");
        int result = (int) Math.pow(base, exp);
        
        return new MathQuestion(question + " = ?", result);
    }
    
    /**
     * Genera una pregunta con paréntesis: a × (b + c)
     * 
     * @param maxNumber El número máximo para los operandos
     * @return Una pregunta con paréntesis
     */
    private static MathQuestion generateParenthesisMultiplyAddQuestion(int maxNumber) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int a = rand.nextInt(1, Math.min(maxNumber, 5) + 1);
        int b = rand.nextInt(1, Math.min(maxNumber, 10) + 1);
        int c = rand.nextInt(1, Math.min(maxNumber, 10) + 1);
        
        String question = a + " × (" + b + " + " + c + ")";
        int result = a * (b + c);
        
        return new MathQuestion(question + " = ?", result);
    }
}
