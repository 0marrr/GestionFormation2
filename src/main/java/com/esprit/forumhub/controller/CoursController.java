package com.esprit.forumhub.controller;

import com.esprit.forumhub.model.Cours;
import com.esprit.forumhub.model.Formation;
import com.esprit.forumhub.service.CourService;
import com.esprit.forumhub.service.FormationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CoursController {

    @FXML private TextField titreField;
    @FXML private TextField descriptionField;
    @FXML private TextField pdfPathField;
    @FXML private TextField videoPathField;
    @FXML private TextField imagePathField;
    @FXML private ComboBox<Formation> formationComboBox;

    @FXML private TableView<Cours> coursTable;
    @FXML private TableColumn<Cours, Integer> idColumn;
    @FXML private TableColumn<Cours, String> titreColumn;
    @FXML private TableColumn<Cours, String> descriptionColumn;
    @FXML private TableColumn<Cours, String> pdfPathColumn;
    @FXML private TableColumn<Cours, String> videoPathColumn;
    @FXML private TableColumn<Cours, String> imagePathColumn;
    @FXML private TableColumn<Cours, String> formationTitleColumn;

    private CourService courService = new CourService();
    private FormationService formationService = new FormationService();
    private ObservableList<Cours> coursList = FXCollections.observableArrayList();
    private ObservableList<Formation> formationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        pdfPathColumn.setCellValueFactory(new PropertyValueFactory<>("pdfPath"));
        videoPathColumn.setCellValueFactory(new PropertyValueFactory<>("videoPath"));
        imagePathColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        formationTitleColumn.setCellValueFactory(cellData -> cellData.getValue().getFormation().titreProperty());

        loadFormations();
        loadCours();

        coursTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleTableDoubleClick();
            }
        });
    }

    private void handleTableDoubleClick() {
        Cours selectedCours = coursTable.getSelectionModel().getSelectedItem();
        if (selectedCours != null) {
            // Populate the input fields with the selected course's data
            titreField.setText(selectedCours.getTitre());
            descriptionField.setText(selectedCours.getDescription());
            pdfPathField.setText(selectedCours.getPdfPath());
            videoPathField.setText(selectedCours.getVideoPath());
            imagePathField.setText(selectedCours.getImagePath());

            // Set the selected formation in the ComboBox
            formationComboBox.setValue(selectedCours.getFormation());
        } else {
            showCustomAlert("No Course Selected", "Please select a course to update.");
        }
    }

    private void loadFormations() {
        formationList.clear();
        try {
            formationList.addAll(formationService.getAllFormations());
            formationComboBox.setItems(formationList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCours() {
        coursList.clear();
        try {
            List<Cours> cours = courService.getAllCours();
            for (Cours c : cours) {
                Formation formation = formationService.getFormationById(c.getFormationId());
                c.setFormation(formation);
            }
            coursList.addAll(cours);
            coursTable.setItems(coursList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddCours() {
        if (validateFields()) {
            try {
                Formation selectedFormation = formationComboBox.getSelectionModel().getSelectedItem();
                if (selectedFormation != null) {
                    courService.addCour(
                            titreField.getText(),
                            descriptionField.getText(),
                            pdfPathField.getText(),
                            videoPathField.getText(),
                            imagePathField.getText(),
                            selectedFormation.getId()
                    );
                    loadCours();
                } else {
                    showCustomAlert("Input Error", "Please select a formation.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleUpdateCours() {
        Cours selectedCours = coursTable.getSelectionModel().getSelectedItem();
        if (selectedCours != null) {
            if (validateFields()) {
                try {
                    courService.updateCour(
                            selectedCours.getId(),
                            titreField.getText(),
                            descriptionField.getText(),
                            pdfPathField.getText(),
                            videoPathField.getText(),
                            imagePathField.getText()
                    );
                    loadCours();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            showCustomAlert("No Course Selected", "Please select a course to update.");
        }
    }

    @FXML
    private void handleDeleteCours() {
        Cours selectedCours = coursTable.getSelectionModel().getSelectedItem();
        if (selectedCours != null) {
            try {
                courService.deleteCour(selectedCours.getId());
                loadCours();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showCustomAlert("No Course Selected", "Please select a course to delete.");
        }
    }

    private boolean validateFields() {
        String titre = titreField.getText();
        String description = descriptionField.getText();

        if (titre.isEmpty()) {
            showCustomAlert("Input Error", "Title is required.");
            return false;
        }
        if (description.isEmpty()) {
            showCustomAlert("Input Error", "Description is required.");
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
    private void handlePdfBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(pdfPathField.getScene().getWindow());
        if (selectedFile != null) {
            pdfPathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleImageBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(imagePathField.getScene().getWindow());
        if (selectedFile != null) {
            imagePathField.setText(selectedFile.getAbsolutePath());
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