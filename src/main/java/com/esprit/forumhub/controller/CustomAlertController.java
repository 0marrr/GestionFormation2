package com.esprit.forumhub.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CustomAlertController {
    @FXML
    private Label alertMessage;

    private Stage stage;

    public void setMessage(String message, Stage stage) {
        this.alertMessage.setText(message);
        this.stage = stage;
    }

    @FXML
    private void closeAlert() {
        if (stage != null) {
            stage.close();
        }
    }
}
