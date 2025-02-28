module com.esprit.forumhub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.esprit.forumhub to javafx.fxml;
    opens com.esprit.forumhub.controller to javafx.fxml;
    exports com.esprit.forumhub;
    exports com.esprit.forumhub.controller;

    opens com.esprit.forumhub.model to javafx.base; // Open the model package to javafx.base
    exports com.esprit.forumhub.model; // Export the model package if needed
}
