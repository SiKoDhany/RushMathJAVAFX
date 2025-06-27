package com.example.mathrush;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    
    @FXML
    protected void onCreditsButtonClick() {
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
}