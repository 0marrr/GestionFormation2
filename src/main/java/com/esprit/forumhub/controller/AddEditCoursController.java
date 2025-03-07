package com.esprit.forumhub.controller;

import com.esprit.forumhub.model.Cours;
import com.esprit.forumhub.model.Formation;
import com.esprit.forumhub.service.CourService;
import com.esprit.forumhub.service.FormationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class AddEditCoursController {
    @FXML private Label titleLabel;
    @FXML private TextField titreField;
    @FXML private TextField descriptionField;
    @FXML private TextField pdfPathField;
    @FXML private TextField videoPathField;
    @FXML private TextField imagePathField;
    @FXML private ComboBox<Formation> formationComboBox;
    @FXML private ImageView iconOperation;

    private Cours selectedCours;
    private Runnable refreshCallback;
    private CourService courService = new CourService();
    private FormationService formationService = new FormationService();

    public void setSelectedCours(Cours cours) {
        this.selectedCours = cours;
        if (cours != null) {
            titleLabel.setText("Modifier le Cours");
            titreField.setText(cours.getTitre());
            descriptionField.setText(cours.getDescription());
            pdfPathField.setText(cours.getPdfPath());
            videoPathField.setText(cours.getVideoPath());
            imagePathField.setText(cours.getImagePath());
            formationComboBox.getSelectionModel().select(cours.getFormation());
            iconOperation.setImage(new Image(getClass().getResource("/image/edit.png").toExternalForm()));
        } else {
            titleLabel.setText("Ajouter un Cours");
            iconOperation.setImage(new Image(getClass().getResource("/image/add.png").toExternalForm()));
        }
        loadFormations();
    }

    public void setRefreshCallback(Runnable callback) {
        this.refreshCallback = callback;
    }

    private void loadFormations() {
        try {
            formationComboBox.getItems().addAll(formationService.getAllFormations());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSave() {
        if (validateFields()) {
            try {
                Formation selectedFormation = formationComboBox.getSelectionModel().getSelectedItem();
                if (selectedFormation != null) {
                    if (selectedCours == null) { // Ajouter
                        courService.addCour(
                                titreField.getText(),
                                descriptionField.getText(),
                                pdfPathField.getText(),
                                videoPathField.getText(),
                                imagePathField.getText(),
                                selectedFormation.getId()
                        );
                    } else { // Modifier
                        courService.updateCour(
                                selectedCours.getId(),
                                titreField.getText(),
                                descriptionField.getText(),
                                pdfPathField.getText(),
                                videoPathField.getText(),
                                imagePathField.getText(),
                                selectedFormation.getId()
                        );
                    }
                    if (refreshCallback != null) refreshCallback.run();
                    closeWindow();
                } else {
                    showCustomAlert("Erreur de saisie", "Veuillez sélectionner une formation.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    @FXML
    private void handlePdfBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(pdfPathField.getScene().getWindow());
        if (selectedFile != null) {
            pdfPathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleImageBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Image", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(imagePathField.getScene().getWindow());
        if (selectedFile != null) {
            imagePathField.setText(selectedFile.getAbsolutePath());
        }
    }


    private boolean validateFields() {
        String titre = titreField.getText();
        String description = descriptionField.getText();

        if (titre.isEmpty()) {
            showCustomAlert("Erreur de saisie", "Le titre est requis.");
            return false;
        }
        if (description.isEmpty()) {
            showCustomAlert("Erreur de saisie", "La description est requise.");
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