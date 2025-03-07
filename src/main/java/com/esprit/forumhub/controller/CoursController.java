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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CoursController {

    @FXML
    private TableView<Cours> coursTable;
    @FXML
    private TableColumn<Cours, String> titreColumn;
    @FXML
    private TableColumn<Cours, String> descriptionColumn;
    @FXML
    private TableColumn<Cours, String> pdfPathColumn;
    @FXML
    private TableColumn<Cours, String> videoPathColumn;
    @FXML
    private TableColumn<Cours, String> imagePathColumn;
    @FXML
    private TableColumn<Cours, String> formationTitleColumn;
    @FXML private TextField searchField;

    private CourService courService = new CourService();
    private FormationService formationService = new FormationService();
    private ObservableList<Cours> coursList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        pdfPathColumn.setCellValueFactory(new PropertyValueFactory<>("pdfPath"));
        videoPathColumn.setCellValueFactory(new PropertyValueFactory<>("videoPath"));
        imagePathColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        formationTitleColumn.setCellValueFactory(cellData -> cellData.getValue().getFormation().titreProperty());

        loadCours();
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
        openAddEditWindow(null);
    }

    @FXML
    private void handleUpdateCours() {
        Cours selected = coursTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openAddEditWindow(selected);
        } else {
            showCustomAlert("Aucune sélection", "Veuillez sélectionner un cours à modifier.");
        }
    }

    private void openAddEditWindow(Cours cours) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esprit/forumhub/add_edit_cours.fxml"));
            Parent root = loader.load();
            AddEditCoursController controller = loader.getController();
            controller.setSelectedCours(cours);
            controller.setRefreshCallback(this::loadCours); // Rafraîchir après l'enregistrement

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
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
            showCustomAlert("Aucun cours sélectionné", "Veuillez sélectionner un cours à supprimer.");
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
            Stage stage = (Stage) coursTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();

        ObservableList<Cours> filteredList = FXCollections.observableArrayList();
        for (Cours cours : coursList) {
            if (cours.getTitre().toLowerCase().contains(searchText) ||
                    cours.getDescription().toLowerCase().contains(searchText) ||
                    cours.getPdfPath().toLowerCase().contains(searchText) ||
                    cours.getVideoPath().toLowerCase().contains(searchText) ||
                    cours.getImagePath().toLowerCase().contains(searchText) ||
                    cours.getFormation().getTitre().toLowerCase().contains(searchText)) {
                filteredList.add(cours);
            }
        }

        coursTable.setItems(filteredList);
    }

    @FXML
    private void handleExportExcel() {
        // Create a new workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cours");

        // Create a cell style for the header
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.WHITE.getIndex());  // White text color
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());  // Dark blue header
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Titre", "Description", "PDF Path", "Video Path", "Image Path", "Formation Title"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Create a cell style for the data rows
        CellStyle dataStyle = workbook.createCellStyle();
        Font dataFont = workbook.createFont();
        dataFont.setFontHeightInPoints((short) 10);
        dataStyle.setFont(dataFont);
        dataStyle.setWrapText(true);  // Wrap text in cells
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        // Populate the sheet with course data
        int rowIndex = 1;
        for (Cours cours : coursList) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(cours.getTitre());
            row.createCell(1).setCellValue(cours.getDescription());
            row.createCell(2).setCellValue(cours.getPdfPath());
            row.createCell(3).setCellValue(cours.getVideoPath());
            row.createCell(4).setCellValue(cours.getImagePath());
            row.createCell(5).setCellValue(cours.getFormation().getTitre());

            // Apply the alternating row colors
            CellStyle rowStyle = (rowIndex % 2 == 0) ? createRowStyle(workbook, IndexedColors.LIGHT_YELLOW) : createRowStyle(workbook, IndexedColors.LIGHT_GREEN);

            // Apply the data style and row color to the entire row
            for (int i = 0; i < headers.length; i++) {
                Cell cell = row.getCell(i);
                cell.setCellStyle(rowStyle);  // Apply the alternating row style
            }
        }

        // Adjust column widths automatically to fit the content
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream("cours.xlsx")) {
            workbook.write(fileOut);
            showCustomAlert("Export réussi", "Les cours ont été exportés vers Excel avec succès.");
        } catch (IOException e) {
            e.printStackTrace();
            showCustomAlert("Erreur d'export", "Une erreur est survenue lors de l'exportation des cours.");
        }
    }

    // Helper method to create row style with a specific background color
    private CellStyle createRowStyle(Workbook workbook, IndexedColors color) {
        CellStyle rowStyle = workbook.createCellStyle();
        rowStyle.setFillForegroundColor(color.getIndex());
        rowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        rowStyle.setAlignment(HorizontalAlignment.CENTER);
        rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        rowStyle.setBorderBottom(BorderStyle.THIN);
        rowStyle.setBorderTop(BorderStyle.THIN);
        rowStyle.setBorderLeft(BorderStyle.THIN);
        rowStyle.setBorderRight(BorderStyle.THIN);
        return rowStyle;
    }


}