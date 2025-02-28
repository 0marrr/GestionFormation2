package com.esprit.forumhub.utils;

import com.esprit.forumhub.controller.CustomAlertController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class AlertUtil {

    public static void showCustomAlert(String message, Stage owner) {
        try {
            FXMLLoader loader = new FXMLLoader(AlertUtil.class.getResource("/CustomAlert.fxml"));
            Parent root = loader.load();

            CustomAlertController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            controller.setMessage(message, stage);
            stage.initOwner(owner);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
