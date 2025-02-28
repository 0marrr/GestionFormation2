package com.esprit.forumhub.service;

import com.esprit.forumhub.model.Cours;
import com.esprit.forumhub.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourService {
    private Connection cnx = DataSource.getInstance().getCnx();

    // Create a new Course
    public void addCour(String titre, String description, String pdfPath, String videoPath, String imagePath, int formationId) throws SQLException {
        String query = "INSERT INTO cours (titre, description, pdfPath, videoPath, imagePath, formation_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, titre);
            pst.setString(2, description);
            pst.setString(3, pdfPath);
            pst.setString(4, videoPath);
            pst.setString(5, imagePath);
            pst.setInt(6, formationId);
            pst.executeUpdate();
        }
    }

    // Read all Courses
    public List<Cours> getAllCours() throws SQLException {
        List<Cours> coursList = new ArrayList<>();
        String query = "SELECT * FROM cours";
        try (PreparedStatement pst = cnx.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Cours cours = new Cours(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("pdfPath"),
                        rs.getString("videoPath"),
                        rs.getString("imagePath"),
                        null, // You can set the Formation object here if needed
                        rs.getInt("formation_id") // Add the formationId
                );
                coursList.add(cours);
            }
        }
        return coursList;
    }

    // Update a Course
    public void updateCour(int id, String titre, String description, String pdfPath, String videoPath, String imagePath) throws SQLException {
        String query = "UPDATE cours SET titre = ?, description = ?, pdfPath = ?, videoPath = ?, imagePath = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, titre);
            pst.setString(2, description);
            pst.setString(3, pdfPath);
            pst.setString(4, videoPath);
            pst.setString(5, imagePath);
            pst.setInt(6, id);
            pst.executeUpdate();
        }
    }

    // Delete a Course
    public void deleteCour(int id) throws SQLException {
        String query = "DELETE FROM cours WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}