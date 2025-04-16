package main.java.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.java.db.DatabaseHandler;
import main.java.model.ExternerMitarbeiter;
import main.java.model.Kunde;
import main.java.model.Mitarbeiter;
import main.java.parser.GenericParser;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Die Haupt-GUI-Klasse für die Mitarbeiterverwaltung der Firma ShareEmp Ltd.
 * Diese Klasse erweitert die JavaFX-Klasse {@link Application} und stellt die Benutzeroberfläche
 * für die Verwaltung von Mitarbeitern, externen Mitarbeitern und Kunden bereit.
 */
public class MainGUI extends Application {
    private String selectedDatabase;
    private Stage primaryStage;

    // Observable Listen für die Datentypen
    private final ObservableList<Mitarbeiter> mitarbeiterList = FXCollections.observableArrayList();
    private final ObservableList<ExternerMitarbeiter> externeMitarbeiterList = FXCollections.observableArrayList();
    private final ObservableList<Kunde> kundenList = FXCollections.observableArrayList();
    private GridPane form;

    /**
     * Konstruktor für die MainGUI-Klasse.
     *
     * @param selectedDatabase Die ausgewählte Datenbank, die verwendet werden soll.
     */
    public MainGUI(String selectedDatabase) {
        this.selectedDatabase = selectedDatabase;
    }

    /**
     * Die Hauptmethode, die die JavaFX-Anwendung startet.
     *
     * @param primaryStage Das primäre Fenster der Anwendung.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; // Speichere primaryStage
        primaryStage.setTitle("ShareEmp Ltd - Mitarbeiterverwaltung");

        // Daten laden
        loadFromDatabase();

        // Hauptlayout
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1000, 800);

        // Menüleiste
        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        // Tabs erstellen
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createTab("Mitarbeiter", mitarbeiterList, Mitarbeiter.class),
                createTab("Externe Mitarbeiter", externeMitarbeiterList, ExternerMitarbeiter.class),
                createTab("Kunden", kundenList, Kunde.class)
        );
        root.setCenter(tabPane);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Erstellt die Menüleiste für die Anwendung.
     *
     * @return Die erstellte Menüleiste.
     */
    private MenuBar createMenuBar() {
        Menu fileMenu = new Menu("Datei");

        // XML-Import
        MenuItem loadFromXMLItem = new MenuItem("Daten aus XML laden");
        loadFromXMLItem.setOnAction(e -> loadFromXML(primaryStage));

        // XML-Export
        MenuItem exportToXMLItem = new MenuItem("Daten in XML exportieren");
        exportToXMLItem.setOnAction(e -> exportToXML(primaryStage, mitarbeiterList, Mitarbeiter.class));
        exportToXMLItem.setOnAction(e -> exportToXML(primaryStage, externeMitarbeiterList, ExternerMitarbeiter.class));
        exportToXMLItem.setOnAction(e -> exportToXML(primaryStage, kundenList, Kunde.class));

        // Beenden
        MenuItem exitItem = new MenuItem("Beenden");
        exitItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(loadFromXMLItem, exportToXMLItem, exitItem);

        // Hilfe-Menü
        Menu helpMenu = new Menu("Hilfe");
        MenuItem aboutItem = new MenuItem("Über");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().add(aboutItem);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }

    /**
     * Erstellt Buttons für den XML-Import und -Export.
     *
     * @param primaryStage Das primäre Fenster der Anwendung.
     * @param data Die Daten, die importiert oder exportiert werden sollen.
     * @param type Der Typ der Daten.
     * @return Ein HBox-Container mit den Buttons.
     */
    private HBox createXMLButtons(Stage primaryStage, ObservableList<?> data, Class<?> type) {
        Button importXMLButton = new Button("Daten aus XML laden");
        importXMLButton.setOnAction(e -> loadFromXML(primaryStage));

        Button exportXMLButton = new Button("Daten in XML exportieren");
        exportXMLButton.setOnAction(e -> exportToXML(primaryStage, data, type));

        HBox xmlBox = new HBox(10, importXMLButton, exportXMLButton);
        return xmlBox;
    }

    /**
     * Erstellt einen Tab für die Anzeige und Verwaltung von Daten.
     *
     * @param title Der Titel des Tabs.
     * @param data Die Daten, die im Tab angezeigt werden sollen.
     * @param type Der Typ der Daten.
     * @param <T> Der generische Typ der Daten.
     * @return Der erstellte Tab.
     */
    private <T> Tab createTab(String title, ObservableList<T> data, Class<T> type) {
        Tab tab = new Tab(title);
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        // Tabelle erstellen
        TableView<T> tableView = createTableView(data, type);
        content.getChildren().add(tableView);

        // Formular erstellen
        GridPane form = createForm(title, type);
        content.getChildren().add(form);

        // Navigationsbuttons hinzufügen
        HBox navigationBox = createNavigationButtons(tableView, type);
        content.getChildren().add(navigationBox);

        // XML-Import/Export-Buttons hinzufügen
        HBox xmlBox = createXMLButtons(primaryStage, data, type);
        content.getChildren().add(xmlBox);

        tab.setContent(content);
        return tab;
    }

    /**
     * Erstellt eine TableView für die Anzeige von Daten.
     *
     * @param data Die Daten, die in der TableView angezeigt werden sollen.
     * @param type Der Typ der Daten.
     * @param <T> Der generische Typ der Daten.
     * @return Die erstellte TableView.
     */
    private <T> TableView<T> createTableView(ObservableList<T> data, Class<T> type) {
        TableView<T> tableView = new TableView<>();
        configureTable(tableView, type);
        tableView.setItems(data);

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tableView.getSelectionModel().isEmpty()) {
                T selectedItem = tableView.getSelectionModel().getSelectedItem();
                DatabaseHandler.deleteFromDatabase(selectedItem, type);
                data.remove(selectedItem);
            }
        });

        return tableView;
    }

    /**
     * Konfiguriert die Spalten einer TableView basierend auf dem Datentyp.
     *
     * @param table Die TableView, die konfiguriert werden soll.
     * @param type Der Typ der Daten.
     * @param <T> Der generische Typ der Daten.
     */
    private <T> void configureTable(TableView<T> table, Class<T> type) {
        table.getColumns().clear();

        if (type == Mitarbeiter.class) {
            table.getColumns().addAll(
                    createColumn("Vorname", "vorname", String.class),
                    createColumn("Nachname", "nachname", String.class),
                    createColumn("Mitarbeiternummer", "mitarbeiternummer", Integer.class),
                    createColumn("Straße", "strasse", String.class),
                    createColumn("PLZ", "plz", String.class),
                    createColumn("Ort", "ort", String.class),
                    createColumn("Telefon", "telefon", String.class),
                    createColumn("E-Mail", "email", String.class)
            );
        } else if (type == ExternerMitarbeiter.class) {
            table.getColumns().addAll(
                    createColumn("Vorname", "vorname", String.class),
                    createColumn("Nachname", "nachname", String.class),
                    createColumn("Mitarbeiternummer", "mitarbeiternummer", Integer.class),
                    createColumn("Firma", "firma", String.class),
                    createColumn("Straße", "strasse", String.class),
                    createColumn("PLZ", "plz", String.class),
                    createColumn("Ort", "ort", String.class),
                    createColumn("Telefon", "telefon", String.class),
                    createColumn("E-Mail", "email", String.class)
            );
        } else if (type == Kunde.class) {
            table.getColumns().addAll(
                    createColumn("Vorname", "vorname", String.class),
                    createColumn("Nachname", "nachname", String.class),
                    createColumn("Kundennummer", "kundennummer", Integer.class),
                    createColumn("Branche", "branche", String.class),
                    createColumn("Straße", "strasse", String.class),
                    createColumn("PLZ", "plz", String.class),
                    createColumn("Ort", "ort", String.class),
                    createColumn("Telefon", "telefon", String.class),
                    createColumn("E-Mail", "email", String.class)
            );
        }
    }

    /**
     * Erstellt eine TableColumn für die TableView.
     *
     * @param title Der Titel der Spalte.
     * @param property Der Name der Eigenschaft, die in der Spalte angezeigt werden soll.
     * @param type Der Typ der Daten in der Spalte.
     * @param <T> Der generische Typ der Daten.
     * @param <V> Der generische Typ der Spalte.
     * @return Die erstellte TableColumn.
     */
    private <T, V> TableColumn<T, V> createColumn(String title, String property, Class<V> type) {
        TableColumn<T, V> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    /**
     * Erstellt ein Formular für die Eingabe von Daten.
     *
     * @param title Der Titel des Formulars.
     * @param type Der Typ der Daten, die eingegeben werden sollen.
     * @return Das erstellte GridPane-Formular.
     */
    private GridPane createForm(String title, Class<?> type) {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        Label vornameLabel = new Label("Vorname:");
        TextField vornameField = new TextField();
        form.add(vornameLabel, 0, 0);
        form.add(vornameField, 1, 0);

        Label nachnameLabel = new Label("Nachname:");
        TextField nachnameField = new TextField();
        form.add(nachnameLabel, 0, 1);
        form.add(nachnameField, 1, 1);

        if (type == Mitarbeiter.class) {
            addMitarbeiterFields(form, vornameField, nachnameField);
        } else if (type == ExternerMitarbeiter.class) {
            addExternerMitarbeiterFields(form, vornameField, nachnameField);
        } else if (type == Kunde.class) {
            addKundeFields(form, vornameField, nachnameField);
        }

        return form;
    }

    /**
     * Fügt Felder für die Eingabe von Mitarbeiterdaten hinzu.
     *
     * @param form Das GridPane-Formular, dem die Felder hinzugefügt werden sollen.
     * @param vornameField Das TextField für den Vornamen.
     * @param nachnameField Das TextField für den Nachnamen.
     */
    private void addMitarbeiterFields(GridPane form, TextField vornameField, TextField nachnameField) {
        addTextField(form, "Mitarbeiternummer:", 2);
        addTextField(form, "Straße:", 3);
        addTextField(form, "PLZ:", 4);
        addTextField(form, "Ort:", 5);
        addTextField(form, "Telefon:", 6);
        addTextField(form, "E-Mail:", 7);

        Button addButton = new Button("Hinzufügen");
        addButton.setOnAction(e -> addMitarbeiter(
                vornameField.getText(),
                nachnameField.getText(),
                Integer.parseInt(getFieldValue(form, 2)),
                getFieldValue(form, 3),
                getFieldValue(form, 4),
                getFieldValue(form, 5),
                getFieldValue(form, 6),
                getFieldValue(form, 7)
        ));
        form.add(addButton, 1, 8);
    }

    /**
     * Fügt Felder für die Eingabe von externen Mitarbeiterdaten hinzu.
     *
     * @param form Das GridPane-Formular, dem die Felder hinzugefügt werden sollen.
     * @param vornameField Das TextField für den Vornamen.
     * @param nachnameField Das TextField für den Nachnamen.
     */
    private void addExternerMitarbeiterFields(GridPane form, TextField vornameField, TextField nachnameField) {
        addTextField(form, "Mitarbeiternummer:", 2);
        addTextField(form, "Firma:", 3);
        addTextField(form, "Straße:", 4);
        addTextField(form, "PLZ:", 5);
        addTextField(form, "Ort:", 6);
        addTextField(form, "Telefon:", 7);
        addTextField(form, "E-Mail:", 8);

        Button addButton = new Button("Hinzufügen");
        addButton.setOnAction(e -> addExternerMitarbeiter(
                vornameField.getText(),
                nachnameField.getText(),
                getFieldValue(form, 2),
                getFieldValue(form, 3),
                getFieldValue(form, 4),
                getFieldValue(form, 5),
                getFieldValue(form, 6),
                getFieldValue(form, 7),
                getFieldValue(form, 8)
        ));
        form.add(addButton, 1, 9);
    }

    /**
     * Fügt Felder für die Eingabe von Kundendaten hinzu.
     *
     * @param form Das GridPane-Formular, dem die Felder hinzugefügt werden sollen.
     * @param vornameField Das TextField für den Vornamen.
     * @param nachnameField Das TextField für den Nachnamen.
     */
    private void addKundeFields(GridPane form, TextField vornameField, TextField nachnameField) {
        addTextField(form, "Kundennummer:", 2);
        addTextField(form, "Branche:", 3);
        addTextField(form, "Straße:", 4);
        addTextField(form, "PLZ:", 5);
        addTextField(form, "Ort:", 6);
        addTextField(form, "Telefon:", 7);
        addTextField(form, "E-Mail:", 8);

        Button addButton = new Button("Hinzufügen");
        addButton.setOnAction(e -> addKunde(
                vornameField.getText(),
                nachnameField.getText(),
                getFieldValue(form, 2),
                getFieldValue(form, 3),
                getFieldValue(form, 4),
                getFieldValue(form, 5),
                getFieldValue(form, 6),
                getFieldValue(form, 7),
                getFieldValue(form, 8)
        ));
        form.add(addButton, 1, 9);
    }

    /**
     * Fügt ein TextField zum Formular hinzu.
     *
     * @param form Das GridPane-Formular, dem das TextField hinzugefügt werden soll.
     * @param label Das Label für das TextField.
     * @param row Die Zeile, in der das TextField hinzugefügt werden soll.
     */
    private void addTextField(GridPane form, String label, int row) {
        form.add(new Label(label), 0, row);
        form.add(new TextField(), 1, row);
    }

    /**
     * Gibt den Wert eines TextFields im Formular zurück.
     *
     * @param form Das GridPane-Formular, das das TextField enthält.
     * @param row Die Zeile, in der das TextField liegt.
     * @return Der Wert des TextFields.
     */
    private String getFieldValue(GridPane form, int row) {
        return ((TextField) form.getChildren().get(row * 2 + 1)).getText();
    }

    /**
     * Löscht den Inhalt aller TextFields im Formular.
     *
     * @param form Das GridPane-Formular, dessen TextFields geleert werden sollen.
     */
    private void clearFields(GridPane form) {
        for (Node node : form.getChildren()) {
            if (node instanceof TextField textField) {
                textField.clear();
            }
        }
    }

    /**
     * Lädt Daten aus der Datenbank und füllt die entsprechenden Listen.
     */
    private void loadFromDatabase() {
        // Mitarbeiter laden
        List<Mitarbeiter> mitarbeiterListData = DatabaseHandler.fetchFromDatabase("mitarbeiter", rs -> {
            Mitarbeiter m = new Mitarbeiter();
            m.setVorname(rs.getString("vorname"));
            m.setNachname(rs.getString("nachname"));
            m.setMitarbeiternummer(rs.getInt("mitarbeiternummer"));
            m.setStrasse(rs.getString("strasse"));
            m.setPlz(rs.getString("plz"));
            m.setOrt(rs.getString("ort"));
            m.setTelefon(rs.getString("telefon"));
            m.setEmail(rs.getString("email"));
            return m;
        });
        this.mitarbeiterList.setAll(mitarbeiterListData);

        // Externe Mitarbeiter laden
        List<ExternerMitarbeiter> externeMitarbeiterListData = DatabaseHandler.fetchFromDatabase("externermitarbeiter", rs -> {
            ExternerMitarbeiter em = new ExternerMitarbeiter();
            em.setVorname(rs.getString("vorname"));
            em.setNachname(rs.getString("nachname"));
            em.setMitarbeiternummer(rs.getInt("mitarbeiternummer"));
            em.setFirma(rs.getString("firma"));
            em.setStrasse(rs.getString("strasse"));
            em.setPlz(rs.getString("plz"));
            em.setOrt(rs.getString("ort"));
            em.setTelefon(rs.getString("telefon"));
            em.setEmail(rs.getString("email"));
            return em;
        });
        this.externeMitarbeiterList.setAll(externeMitarbeiterListData);

        // Kunden laden
        List<Kunde> kundenListData = DatabaseHandler.fetchFromDatabase("kunde", rs -> {
            Kunde k = new Kunde();
            k.setVorname(rs.getString("vorname"));
            k.setNachname(rs.getString("nachname"));
            k.setKundennummer(rs.getInt("kundennummer"));
            k.setBranche(rs.getString("branche"));
            k.setStrasse(rs.getString("strasse"));
            k.setPlz(rs.getString("plz"));
            k.setOrt(rs.getString("ort"));
            k.setTelefon(rs.getString("telefon"));
            k.setEmail(rs.getString("email"));
            return k;
        });
        this.kundenList.setAll(kundenListData);
    }

    /**
     * Lädt Daten aus einer XML-Datei und füllt die entsprechenden Listen.
     *
     * @param primaryStage Das primäre Fenster der Anwendung.
     */
    private void loadFromXML(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("XML-Ordner auswählen");
        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        if (selectedDirectory != null) {
            String folderPath = selectedDirectory.getAbsolutePath();
            String mitarbeiterPath = folderPath + "/Mitarbeiter.xml";
            String externeMitarbeiterPath = folderPath + "/ExterneMitarbeiter.xml";
            String kundenPath = folderPath + "/Kunden.xml";

            GenericParser<Mitarbeiter> mitarbeiterParser = new GenericParser<>(mitarbeiterPath);
            GenericParser<ExternerMitarbeiter> externeMitarbeiterParser = new GenericParser<>(externeMitarbeiterPath);
            GenericParser<Kunde> kundeParser = new GenericParser<>(kundenPath);

            List<Mitarbeiter> mitarbeiter = mitarbeiterParser.parse();
            List<ExternerMitarbeiter> externeMitarbeiter = externeMitarbeiterParser.parse();
            List<Kunde> kunden = kundeParser.parse();

            if (mitarbeiter != null) {
                mitarbeiterList.setAll(mitarbeiter);
            }
            if (externeMitarbeiter != null) {
                externeMitarbeiterList.setAll(externeMitarbeiter);
            }
            if (kunden != null) {
                kundenList.setAll(kunden);
            }
        }
    }

    /**
     * Exportiert Daten in eine XML-Datei.
     *
     * @param primaryStage Das primäre Fenster der Anwendung.
     * @param data Die Daten, die exportiert werden sollen.
     * @param type Der Typ der Daten.
     */
    private void exportToXML(Stage primaryStage, ObservableList<?> data, Class<?> type) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("XML-Datei speichern");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML-Dateien", "*.xml"));
        File selectedFile = fileChooser.showSaveDialog(primaryStage);

        if (selectedFile != null) {
            main.java.xml.XMLHandler.exportToXML(selectedFile.getAbsolutePath(), data, type);
        }
    }

    /**
     * Erstellt Navigationsbuttons für die TableView.
     *
     * @param table Die TableView, für die die Buttons erstellt werden sollen.
     * @param type Der Typ der Daten in der TableView.
     * @return Ein HBox-Container mit den Buttons.
     */
    private HBox createNavigationButtons(TableView<?> table, Class<?> type) {
        Button prevButton = new Button("Vorheriger");
        prevButton.setOnAction(e -> navigate(table, -1));

        Button nextButton = new Button("Nächster");
        nextButton.setOnAction(e -> navigate(table, 1));

        Button deleteButton = new Button("Löschen");
        deleteButton.setOnAction(e -> deleteEntry(table, type));

        HBox navigation = new HBox(10, prevButton, nextButton, deleteButton);
        return navigation;
    }

    /**
     * Navigiert in der TableView zu einem vorherigen oder nächsten Eintrag.
     *
     * @param table Die TableView, in der navigiert werden soll.
     * @param direction Die Richtung der Navigation (-1 für vorheriger, 1 für nächster).
     */
    private void navigate(TableView<?> table, int direction) {
        int currentIndex = table.getSelectionModel().getSelectedIndex();
        int newIndex = Math.max(0, Math.min(currentIndex + direction, table.getItems().size() - 1));

        if (newIndex >= 0 && newIndex < table.getItems().size()) {
            table.getSelectionModel().select(newIndex);
        }
    }

    /**
     * Löscht einen Eintrag aus der TableView und der Datenbank.
     *
     * @param table Die TableView, aus der der Eintrag gelöscht werden soll.
     * @param type Der Typ der Daten in der TableView.
     */
    private void deleteEntry(TableView<?> table, Class<?> type) {
        ObservableList<?> items = table.getItems();
        Object selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bestätigung");
            alert.setHeaderText("Eintrag löschen");
            alert.setContentText("Sind Sie sicher, dass Sie diesen Eintrag löschen möchten?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                DatabaseHandler.deleteFromDatabase(selectedItem, type);
                items.remove(selectedItem);
            }
        }
    }

    /**
     * Fügt einen neuen Mitarbeiter zur Liste und zur Datenbank hinzu.
     *
     * @param vorname Der Vorname des Mitarbeiters.
     * @param nachname Der Nachname des Mitarbeiters.
     * @param mitarbeiternummer Die Mitarbeiternummer des Mitarbeiters.
     * @param strasse Die Straße des Mitarbeiters.
     * @param plz Die Postleitzahl des Mitarbeiters.
     * @param ort Der Ort des Mitarbeiters.
     * @param telefon Die Telefonnummer des Mitarbeiters.
     * @param email Die E-Mail-Adresse des Mitarbeiters.
     */
    private void addMitarbeiter(String vorname, String nachname, int mitarbeiternummer, String strasse, String plz, String ort, String telefon, String email) {
        if (!vorname.isEmpty() && !nachname.isEmpty() ) {
            try {
                int mitarbeiterNummer = Integer.parseInt(String.valueOf(mitarbeiternummer));
                Mitarbeiter mitarbeiter = new Mitarbeiter(vorname, nachname, mitarbeiterNummer, strasse, plz, ort, telefon, email);
                DatabaseHandler.saveToDatabase(mitarbeiter);
                mitarbeiterList.add(mitarbeiter);
                clearFields(form); // Pass the form object to clearFields
            } catch (NumberFormatException e) {
                showAlert("Fehler", "Ungültige Eingabe", "Bitte stellen Sie sicher, dass die Mitarbeiternummer eine Zahl ist.");
            }
        } else {
            showAlert("Fehler", "Ungültige Eingaben", "Bitte füllen Sie alle Felder korrekt aus.");
        }
    }

    /**
     * Fügt einen neuen externen Mitarbeiter zur Liste und zur Datenbank hinzu.
     *
     * @param vorname Der Vorname des externen Mitarbeiters.
     * @param nachname Der Nachname des externen Mitarbeiters.
     * @param mitarbeiternummer Die Mitarbeiternummer des externen Mitarbeiters.
     * @param firma Die Firma des externen Mitarbeiters.
     * @param strasse Die Straße des externen Mitarbeiters.
     * @param plz Die Postleitzahl des externen Mitarbeiters.
     * @param ort Der Ort des externen Mitarbeiters.
     * @param telefon Die Telefonnummer des externen Mitarbeiters.
     * @param email Die E-Mail-Adresse des externen Mitarbeiters.
     */
    private void addExternerMitarbeiter(String vorname, String nachname, String mitarbeiternummer, String firma, String strasse, String plz, String ort, String telefon, String email) {
        if (!vorname.isEmpty() && !nachname.isEmpty() && !mitarbeiternummer.isEmpty() && !firma.isEmpty() && !strasse.isEmpty() && !plz.isEmpty() && !ort.isEmpty() && !telefon.isEmpty() && !email.isEmpty()) {
            try {
                int mitarbeiterNummer = Integer.parseInt(mitarbeiternummer.trim());
                ExternerMitarbeiter externerMitarbeiter = new ExternerMitarbeiter(vorname, nachname, mitarbeiterNummer, firma, strasse, plz, ort, telefon, email);
                DatabaseHandler.saveToDatabase(externerMitarbeiter);
                externeMitarbeiterList.add(externerMitarbeiter);
                clearFields(form); // Pass the form object to clearFields
            } catch (NumberFormatException e) {
                showAlert("Fehler", "Ungültige Eingabe", "Bitte stellen Sie sicher, dass die Mitarbeiternummer eine Zahl ist.");
            }
        } else {
            showAlert("Fehler", "Ungültige Eingaben", "Bitte füllen Sie alle Felder korrekt aus.");
        }
    }

    /**
     * Fügt einen neuen Kunden zur Liste und zur Datenbank hinzu.
     *
     * @param vorname Der Vorname des Kunden.
     * @param nachname Der Nachname des Kunden.
     * @param kundennummer Die Kundennummer des Kunden.
     * @param branche Die Branche des Kunden.
     * @param strasse Die Straße des Kunden.
     * @param plz Die Postleitzahl des Kunden.
     * @param ort Der Ort des Kunden.
     * @param telefon Die Telefonnummer des Kunden.
     * @param email Die E-Mail-Adresse des Kunden.
     */
    private void addKunde(String vorname, String nachname, String kundennummer, String branche, String strasse, String plz, String ort, String telefon, String email) {
        if (!vorname.isEmpty() && !nachname.isEmpty() && !kundennummer.isEmpty() && !branche.isEmpty() && !strasse.isEmpty() && !plz.isEmpty() && !ort.isEmpty() && !telefon.isEmpty() && !email.isEmpty()) {
            try {
                int kundenNummer = Integer.parseInt(kundennummer.trim());
                Kunde kunde = new Kunde(vorname, nachname, kundenNummer, branche, strasse, plz, ort, telefon, email);
                DatabaseHandler.saveToDatabase(kunde);
                kundenList.add(kunde);
                clearFields(form); // Pass the form object to clearFields
            } catch (NumberFormatException e) {
                showAlert("Fehler", "Ungültige Eingabe", "Bitte stellen Sie sicher, dass die Kundennummer eine Zahl ist.");
            }
        } else {
            showAlert("Fehler", "Ungültige Eingaben", "Bitte füllen Sie alle Felder korrekt aus.");
        }
    }

    /**
     * Zeigt eine Informationsmeldung an.
     *
     * @param title Der Titel der Meldung.
     * @param header Der Header der Meldung.
     * @param content Der Inhalt der Meldung.
     */
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Zeigt einen Dialog mit Informationen über die Anwendung an.
     */
    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Über");
        alert.setHeaderText("ShareEmp Ltd");
        alert.setContentText("Mitarbeiterverwaltung und -verwaltung in einem professionellen System.");
        alert.showAndWait();
    }

    /**
     * Die Hauptmethode, die die Anwendung startet.
     *
     * @param args Die Kommandozeilenargumente.
     */
    public static void main(String[] args) {
        launch(args);
    }
}