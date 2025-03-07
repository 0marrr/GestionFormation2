package com.esprit.forumhub.controller;

import com.esprit.forumhub.model.Formation;
import com.esprit.forumhub.service.FormationService;
import com.esprit.forumhub.utils.QRCodeUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FormationController {

    @FXML private TableView<Formation> formationTable;
    @FXML private TableColumn<Formation, String> titreColumn;
    @FXML private TableColumn<Formation, String> dureeColumn;
    @FXML private TableColumn<Formation, String> niveauColumn;
    @FXML private TableColumn<Formation, String> contenuColumn;
    @FXML private TextField searchField;
    @FXML private HBox paginationContainer;

    private FormationService formationService = new FormationService();
    private ObservableList<Formation> formationList = FXCollections.observableArrayList();
    private ObservableList<Formation> filteredList = FXCollections.observableArrayList();
    private int itemsPerPage = 2; // Number of items per page
    private int currentPage = 1; // Current page number
    private boolean isSearchActive = false; // Track if search is active

    @FXML
    public void initialize() {
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        dureeColumn.setCellValueFactory(new PropertyValueFactory<>("duree"));
        niveauColumn.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        contenuColumn.setCellValueFactory(new PropertyValueFactory<>("contenu"));

        formationTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Detect double-click
                Formation selectedFormation = formationTable.getSelectionModel().getSelectedItem();
                if (selectedFormation != null) {
                    openQrWindow(selectedFormation); // Open the QR Code window
                }
            }
        });

        loadFormations();
        setupPagination();
    }

    private void loadFormations() {
        formationList.clear();
        try {
            List<Formation> allFormations = formationService.getAllFormations();
            formationList.addAll(allFormations);
            updateTable();
            setupPagination(); // Reset pagination after loading formations
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTable() {
        ObservableList<Formation> dataToDisplay = isSearchActive ? filteredList : formationList;
        int fromIndex = (currentPage - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, dataToDisplay.size());
        formationTable.setItems(FXCollections.observableArrayList(dataToDisplay.subList(fromIndex, toIndex)));
    }

    private void setupPagination() {
        paginationContainer.getChildren().clear();
        ObservableList<Formation> dataToPaginate = isSearchActive ? filteredList : formationList;
        int totalPages = (int) Math.ceil((double) dataToPaginate.size() / itemsPerPage);

        for (int i = 1; i <= totalPages; i++) {
            Button pageButton = new Button(String.valueOf(i));
            pageButton.getStyleClass().add("pagination-button");
            if (i == currentPage) {
                pageButton.getStyleClass().add("active");
            }
            pageButton.setOnAction(event -> {
                currentPage = Integer.parseInt(((Button) event.getSource()).getText());
                updateTable();
                setupPagination(); // Refresh pagination buttons
            });
            paginationContainer.getChildren().add(pageButton);
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            // If search is cleared, reset to original data
            isSearchActive = false;
            currentPage = 1;
            updateTable();
            setupPagination();
        } else {
            // Filter the data based on search text
            isSearchActive = true;
            filteredList.clear();
            for (Formation formation : formationList) {
                if (formation.getTitre().toLowerCase().contains(searchText) ||
                        formation.getDuree().toLowerCase().contains(searchText) ||
                        formation.getNiveau().toLowerCase().contains(searchText) ||
                        formation.getContenu().toLowerCase().contains(searchText)) {
                    filteredList.add(formation);
                }
            }
            currentPage = 1;
            updateTable();
            setupPagination();
        }
    }

    @FXML
    private void handleAddFormation() {
        openAddEditWindow(null);
    }

    @FXML
    private void handleUpdateFormation() {
        Formation selected = formationTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openAddEditWindow(selected);
        } else {
            showCustomAlert("Aucune sélection", "Veuillez sélectionner une formation à modifier.");
        }
    }

    private void openAddEditWindow(Formation formation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esprit/forumhub/add_edit_formation.fxml"));
            Parent root = loader.load();
            AddEditFormationController controller = loader.getController();
            controller.setSelectedFormation(formation);
            controller.setRefreshCallback(() -> {
                loadFormations(); // Refresh the table and pagination after adding/editing
            });

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteFormation() {
        Formation selectedFormation = formationTable.getSelectionModel().getSelectedItem();
        if (selectedFormation != null) {
            try {
                formationService.deleteFormation(selectedFormation.getId());
                loadFormations(); // Refresh the table and pagination after deletion
                showCustomAlert("Succès", "Formation supprimée avec succès!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showCustomAlert("Aucune formation sélectionnée", "Veuillez sélectionner une formation à supprimer.");
        }
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
            Stage stage = (Stage) formationTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openQrWindow(Formation selectedFormation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esprit/forumhub/CustomQr.fxml"));
            Parent root = loader.load();

            CustomQrController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);

            String formationData = "Titre: " + selectedFormation.getTitre() + "\n" +
                    "Durée: " + selectedFormation.getDuree() + "\n" +
                    "Niveau: " + selectedFormation.getNiveau() + "\n" +
                    "Contenu: " + selectedFormation.getContenu();

            // Generate the QR Code with the concatenated data
            Image qrImage = QRCodeUtil.generateQRCode(formationData);

            // Set QR code and pass the stage to the controller
            controller.setQrCode(qrImage, stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}