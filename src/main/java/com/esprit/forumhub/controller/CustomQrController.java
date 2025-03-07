package com.esprit.forumhub.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CustomQrController {

    private Stage stage;

    @FXML
    private ImageView qrCodeImage;

    public void setQrCode(Image qrCode, Stage stage) {
        qrCodeImage.setImage(qrCode);
        this.stage = stage;
    }

    @FXML
    private void closeAlert() {
        if (stage != null) {
            stage.close();
        }
    }
}