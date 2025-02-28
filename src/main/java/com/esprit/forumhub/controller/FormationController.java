package com.esprit.forumhub.controller;

import com.esprit.forumhub.model.Formation;
import com.esprit.forumhub.service.FormationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class FormationController {

    @FXML private TextField titreField;
    @FXML private TextField dureeField;
    @FXML private TextField niveauField;
    @FXML private TextField contenuField;

    @FXML private TableView<Formation> formationTable;
    @FXML private TableColumn<Formation, Integer> idColumn;
    @FXML private TableColumn<Formation, String> titreColumn;
    @FXML private TableColumn<Formation, String> dureeColumn;
    @FXML private TableColumn<Formation, String> niveauColumn;
    @FXML private TableColumn<Formation, String> contenuColumn;

    private FormationService formationService = new FormationService();
    private ObservableList<Formation> formationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        dureeColumn.setCellValueFactory(new PropertyValueFactory<>("duree"));
        niveauColumn.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        contenuColumn.setCellValueFactory(new PropertyValueFactory<>("contenu"));

        loadFormations();

        formationTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleTableDoubleClick();
            }
        });

    }

    private void handleTableDoubleClick() {
        Formation selectedFormation = formationTable.getSelectionModel().getSelectedItem();
        if (selectedFormation != null) {
            titreField.setText(selectedFormation.getTitre());
            dureeField.setText(selectedFormation.getDuree());
            niveauField.setText(selectedFormation.getNiveau());
            contenuField.setText(selectedFormation.getContenu());
        } else {
            showCustomAlert("No Formation Selected", "Please select a formation to update.");
        }
    }

    private void loadFormations() {
        formationList.clear();
        try {
            formationList.addAll(formationService.getAllFormations());
            formationTable.setItems(formationList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddFormation() {
        if (validateFields()) {
            try {
                formationService.addFormation(
                        titreField.getText(),
                        dureeField.getText(),
                        niveauField.getText(),
                        contenuField.getText()
                );
                loadFormations();
                showCustomAlert("Success", "Formation added successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleUpdateFormation() {
        Formation selectedFormation = formationTable.getSelectionModel().getSelectedItem();
        if (selectedFormation != null) {
            if (validateFields()) {
                try {
                    formationService.updateFormation(
                            selectedFormation.getId(),
                            titreField.getText(),
                            dureeField.getText(),
                            niveauField.getText(),
                            contenuField.getText()
                    );
                    loadFormations();
                    showCustomAlert("Success", "Formation updated successfully!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            showCustomAlert("No Formation Selected", "Please select a formation to update.");
        }
    }

    @FXML
    private void handleDeleteFormation() {
        Formation selectedFormation = formationTable.getSelectionModel().getSelectedItem();
        if (selectedFormation != null) {
            try {
                formationService.deleteFormation(selectedFormation.getId());
                loadFormations();
                showCustomAlert("Success", "Formation deleted successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showCustomAlert("No Formation Selected", "Please select a formation to delete.");
        }
    }

    private boolean validateFields() {
        String titre = titreField.getText();
        String duree = dureeField.getText();
        String niveau = niveauField.getText();
        String contenu = contenuField.getText();

        if (titre.isEmpty()) {
            showCustomAlert("Input Error", "Title is required.");
            return false;
        }
        if (duree.isEmpty()) {
            showCustomAlert("Input Error", "Duration is required.");
            return false;
        }
        if (niveau.isEmpty()) {
            showCustomAlert("Input Error", "Level is required.");
            return false;
        }
        if (contenu.isEmpty()) {
            showCustomAlert("Input Error", "Content is required.");
            return false;
        }
        return true; // All validations passed
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

    @FXML
    private void handleFormationClick() {
        loadScene("/com/esprit/forumhub/formation.fxml");
    }

    @FXML
    private void handleCoursClick() {
        loadScene("/com/esprit/forumhub/cours.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) titreField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}