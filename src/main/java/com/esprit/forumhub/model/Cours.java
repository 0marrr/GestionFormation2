package com.esprit.forumhub.model;

public class Cours {
    private int id;
    private String titre;
    private String description;
    private String pdfPath;
    private String videoPath;
    private String imagePath;
    private Formation formation;
    private int formationId; // Add this property

    public Cours(int id, String titre, String description, String pdfPath, String videoPath, String imagePath, Formation formation, int formationId) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.pdfPath = pdfPath;
        this.videoPath = videoPath;
        this.imagePath = imagePath;
        this.formation = formation;
        this.formationId = formationId; // Initialize the formationId
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }

    public String getVideoPath() { return videoPath; }
    public void setVideoPath(String videoPath) { this.videoPath = videoPath; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public Formation getFormation() { return formation; }
    public void setFormation(Formation formation) { this.formation = formation; }

    public int getFormationId() { return formationId; } // Add this getter
    public void setFormationId(int formationId) { this.formationId = formationId; } // Add this setter

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", pdfPath='" + pdfPath + '\'' +
                ", videoPath='" + videoPath + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", formation=" + formation +
                ", formationId=" + formationId +
                '}';
    }
}