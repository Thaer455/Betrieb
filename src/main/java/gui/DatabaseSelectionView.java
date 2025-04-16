package main.java.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Darstellung der Auswahl einer Tabelle.
 */
public class DatabaseSelectionView {
    private Stage stage;
    private ComboBox<String> dbComboBox;
    private StringProperty selectedDatabaseProperty = new SimpleStringProperty();

    public DatabaseSelectionView(Stage primaryStage) {
        this.stage = new Stage();
        this.stage.setTitle("Tabellenauswahl");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label dbLabel = new Label("Tabelle auswählen:");
        dbComboBox = new ComboBox<>();
        dbComboBox.getItems().addAll("Mitarbeiter", "ExterneMitarbeiter", "Kunden");

        Button selectButton = new Button("Auswählen");
        selectButton.setOnAction(e -> {
            String selectedDB = dbComboBox.getValue();
            selectedDatabaseProperty.set(selectedDB);
            stage.close();
        });

        grid.add(dbLabel, 0, 0);
        grid.add(dbComboBox, 1, 0);
        grid.add(selectButton, 1, 1);

        Scene scene = new Scene(grid, 300, 150);
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }

    /**
     * Gibt die StringProperty zurück, die den ausgewählten Tabelle enthält.
     */
    public StringProperty getSelectedDatabaseProperty() {
        return selectedDatabaseProperty;
    }

    /**
     * Gibt den aktuell ausgewählten Datenbanknamen zurück.
     */
    public String getSelectedDatabase() {
        return dbComboBox.getValue();
    }
}
