package com.esprit.forumhub.controller;

import com.esprit.forumhub.model.Formation;
import com.esprit.forumhub.service.FormationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AddEditFormationController {
    @FXML private TextField titreField;
    @FXML private TextField dureeField;
    @FXML private TextField niveauField;
    @FXML private TextField contenuField;
    @FXML private Label titleLabel;
    @FXML private ImageView imageOperation;

    private FormationService formationService = new FormationService();
    private Formation selectedFormation;
    private Runnable refreshCallback;

    public void setSelectedFormation(Formation formation) {
        this.selectedFormation = formation;
        if (formation != null) {
            titleLabel.setText("Modifier la Formation");
            titreField.setText(formation.getTitre());
            dureeField.setText(formation.getDuree());
            niveauField.setText(formation.getNiveau());
            contenuField.setText(formation.getContenu());
            imageOperation.setImage(new Image(getClass().getResource("/image/edit.png").toExternalForm()));
        } else {
            titleLabel.setText("Ajouter une Formation");
            imageOperation.setImage(new Image(getClass().getResource("/image/add.png").toExternalForm()));
        }
    }

    public void setRefreshCallback(Runnable callback) {
        this.refreshCallback = callback;
    }

    @FXML
    private void handleSave() {
        if (validateFields()) {
            try {
                if (selectedFormation == null) { // Ajouter
                    formationService.addFormation(
                            titreField.getText(),
                            dureeField.getText(),
                            niveauField.getText(),
                            contenuField.getText()
                    );
                } else { // Modifier
                    formationService.updateFormation(
                            selectedFormation.getId(),
                            titreField.getText(),
                            dureeField.getText(),
                            niveauField.getText(),
                            contenuField.getText()
                    );
                }
                if (refreshCallback != null) refreshCallback.run();
                closeWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private boolean validateFields() {
        String titre = titreField.getText();
        String duree = dureeField.getText();
        String niveau = niveauField.getText();
        String contenu = contenuField.getText();

        if (titre.isEmpty()) {
            showCustomAlert("Erreur de saisie", "Le titre est requis.");
            return false;
        }
        if (duree.isEmpty()) {
            showCustomAlert("Erreur de saisie", "La durée est requise.");
            return false;
        }
        if (niveau.isEmpty()) {
            showCustomAlert("Erreur de saisie", "Le niveau est requis.");
            return false;
        }
        if (contenu.isEmpty()) {
            showCustomAlert("Erreur de saisie", "Le contenu est requis.");
            return false;
        }
        return true; // Toutes les validations sont réussies
    }

    private void closeWindow() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    private void showCustomAlert(String title, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esprit/forumhub/CustomAlert.fxml"));
            Parent root = loader.load();

            CustomAlertController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            controller.setMessage(message, stage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}