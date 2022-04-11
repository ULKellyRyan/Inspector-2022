package controllers;

import javafx.fxml.FXML;

import javafx.scene.Parent;
import models.InstructorModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InstructorController {
    InstructorController instructorController;
    public InstructorModel instructor;
    @FXML private Parent submissionView;
    @FXML SubmissionController submissionViewController;

    void setup(InstructorController instructorController, String email){
        this.instructorController = instructorController;

        //sql query to initialize InstructorModel object
        Connection conn = DatabaseController.dbConnect();
        String sql = "SELECT instructorId, name, importDirectory, resultsDirectory, lastUsedRubric FROM INSTRUCTOR WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            int instructorId = Integer.parseInt(rs.getString(1));
            String name = rs.getString(2);
            String importDirectory = rs.getString(3);
            String resultsDirectory = rs.getString(4);
            String lastUsedRubric = rs.getString(5);
            conn.close();

            // set instructor info
            instructor = new InstructorModel(instructorId, name, email, importDirectory, resultsDirectory, lastUsedRubric);
            submissionViewController.setInstructor(instructor);

            // prepare source code display area
            submissionViewController.setUpSourceCodeDisplay();

            // set key listener
            submissionViewController.initializeListeners();

            // import files
            submissionViewController.displayFileTree(submissionViewController.treeView);



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void setExportDirectory() {
        submissionViewController.setExportDirectory();
    }

    @FXML
    void setImportDirectory() {
        submissionViewController.setImportDirectory();
    }

    @FXML
    void exitApplication(){
        DialogController dialogController = new DialogController();
        dialogController.displayDialog("ExitApplicationDialogView.fxml", "Exit Application");
    }
}
