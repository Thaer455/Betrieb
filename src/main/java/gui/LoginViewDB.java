package main.java.gui;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.db.DatabaseHandler;

/**
 * Die Login-Ansicht für die Verbindung zur Datenbank.
 */
public class LoginViewDB extends Application {
    private Stage primaryStage;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginView();
    }

    private void showLoginView() {
        primaryStage.setTitle("ShareEmp - Datenbank Login");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(new Label("Benutzername:"), 0, 0);
        usernameField = new TextField();
        grid.add(usernameField, 1, 0);

        grid.add(new Label("Passwort:"), 0, 1);
        passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);

        Button loginButton = new Button("Verbinden");
        loginButton.setOnAction(e -> connectAndOpenDatabaseSelection());
        grid.add(loginButton, 1, 2);

        statusLabel = new Label();
        grid.add(statusLabel, 0, 3, 2, 1);

        Scene scene = new Scene(grid, 350, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectAndOpenDatabaseSelection() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Bitte füllen Sie alle Felder aus!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        boolean connected = DatabaseHandler.connectToDatabase(username, password);

        if (connected) {
            statusLabel.setText("Verbindung erfolgreich!");
            statusLabel.setStyle("-fx-text-fill: green;");
            showDatabaseSelection();
        } else {
            statusLabel.setText("Verbindung fehlgeschlagen! Überprüfen Sie die Anmeldedaten.");
            statusLabel.setStyle("-fx-text-fill: red;");
            passwordField.clear();
        }
    }

    private void showDatabaseSelection() {
        DatabaseSelectionView dbSelectionView = new DatabaseSelectionView(primaryStage);
        dbSelectionView.show();

        dbSelectionView.getSelectedDatabaseProperty().addListener((ObservableValue<? extends String> obs, String oldVal, String newVal) -> {
            if (newVal != null) {
                openMainGUI(newVal);
            }
        });
    }

    private void openMainGUI(String selectedDB) {
        MainGUI mainGUI = new MainGUI(selectedDB);
        Stage mainStage = new Stage();
        mainGUI.start(mainStage);
        primaryStage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
