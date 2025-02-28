package com.esprit.forumhub.service;

import com.esprit.forumhub.model.Cours;
import com.esprit.forumhub.model.Formation;
import com.esprit.forumhub.model.User;
import com.esprit.forumhub.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FormationService {
    private Connection cnx = DataSource.getInstance().getCnx();

    // Create a new Formation
    public void addFormation(String titre, String duree, String niveau, String contenu) throws SQLException {
        String query = "INSERT INTO formation (titre, duree, niveau, contenu) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, titre);
            pst.setString(2, duree);
            pst.setString(3, niveau);
            pst.setString(4, contenu);
            pst.executeUpdate();
        }
    }

    public List<Formation> getAllFormations() throws SQLException {
        List<Formation> formations = new ArrayList<>();
        String query = "SELECT * FROM formation";
        try (PreparedStatement pst = cnx.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                int formationId = rs.getInt("id");

                // Fetch related users for this formation
                List<User> utilisateurs = getUsersByFormationId(formationId);

                // Fetch related courses for this formation
                List<Cours> cours = getCoursByFormationId(formationId);

                // Create the Formation object
                Formation formation = new Formation(
                        formationId,
                        rs.getString("titre"),
                        rs.getString("duree"),
                        rs.getString("niveau"),
                        rs.getString("contenu"),
                        utilisateurs,
                        cours
                );
                formations.add(formation);
            }
        }
        return formations;
    }

    // Method to fetch users associated with a formation
    private List<User> getUsersByFormationId(int formationId) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT u.* FROM user u " +
                "JOIN user_formation uf ON u.id = uf.user_id " +
                "WHERE uf.formation_id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, formationId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email")
                    );
                    users.add(user);
                }
            }
        }
        return users;
    }

    // Method to fetch courses associated with a formation
    private List<Cours> getCoursByFormationId(int formationId) throws SQLException {
        List<Cours> coursList = new ArrayList<>();
        String query = "SELECT * FROM cours WHERE formation_id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, formationId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Cours cours = new Cours(
                            rs.getInt("id"),
                            rs.getString("titre"),
                            rs.getString("description"),
                            rs.getString("pdfPath"),
                            rs.getString("videoPath"),
                            rs.getString("imagePath"),
                            null,
                            formationId
                    );
                    coursList.add(cours);
                }
            }
        }
        return coursList;
    }

    public Formation getFormationById(int formationId) throws SQLException {
        String query = "SELECT * FROM formation WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, formationId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Formation(
                            rs.getInt("id"),
                            rs.getString("titre"),
                            rs.getString("duree"),
                            rs.getString("niveau"),
                            rs.getString("contenu"),
                            null, // You can fetch users if needed
                            null  // You can fetch courses if needed
                    );
                }
            }
        }
        return null;
    }

    // Update a Formation
    public void updateFormation(int id, String titre, String duree, String niveau, String contenu) throws SQLException {
        String query = "UPDATE formation SET titre = ?, duree = ?, niveau = ?, contenu = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, titre);
            pst.setString(2, duree);
            pst.setString(3, niveau);
            pst.setString(4, contenu);
            pst.setInt(5, id);
            pst.executeUpdate();
        }
    }

    // Delete a Formation
    public void deleteFormation(int id) throws SQLException {
        String query = "DELETE FROM formation WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}