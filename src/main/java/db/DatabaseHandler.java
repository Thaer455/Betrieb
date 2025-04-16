package main.java.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.model.*;

/**
 * Diese Klasse verwaltet die Verbindung zur Datenbank und führt CRUD-Operationen aus.
 */
public class DatabaseHandler {
    private static Connection connection;




    /**
     * Interface zur Einstellung von Werten in ein PreparedStatement.
     */
    @FunctionalInterface
    public interface PreparedStatementSetter {
        void setValues(PreparedStatement ps) throws SQLException;
    }

    /**
     * Interface zur Umwandlung einer Datenbankzeile in ein Objekt.
     */
    @FunctionalInterface
    public interface RowMapper<T> {
        T mapRow(ResultSet rs) throws SQLException;
    }
    /**
     * Stellt eine Verbindung zur Datenbank her.
     *
     * @param username Der Benutzername für die Datenbank
     * @param password Das Passwort für die Datenbank
     * @return true, wenn die Verbindung erfolgreich ist, sonst false
     */
    public static boolean connectToDatabase(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/shareemp_ltd";
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Verbindung zur Datenbank erfolgreich.");
            return true;
        } catch (SQLException e) {
            System.err.println("Fehler bei der Verbindung zur Datenbank: " + e.getMessage());
            return false;
        }
    }

    /**
     * Erstellt die erforderlichen Tabellen, falls sie noch nicht existieren.
     */
    public static void initializeDatabase() {
        String createMitarbeiterTable = "CREATE TABLE IF NOT EXISTS mitarbeiter ("
                + "mitarbeiternummer INT PRIMARY KEY, "
                + "vorname VARCHAR(50), "
                + "nachname VARCHAR(50), "
                + "strasse VARCHAR(100), "
                + "plz VARCHAR(10), "
                + "ort VARCHAR(50), "
                + "telefon VARCHAR(20), "
                + "email VARCHAR(100))";

        String createExterneMitarbeiterTable = "CREATE TABLE IF NOT EXISTS externermitarbeiter ("
                + "mitarbeiternummer INT PRIMARY KEY, "
                + "vorname VARCHAR(50), "
                + "nachname VARCHAR(50), "
                + "firma VARCHAR(100), "
                + "strasse VARCHAR(100), "
                + "plz VARCHAR(10), "
                + "ort VARCHAR(50), "
                + "telefon VARCHAR(20), "
                + "email VARCHAR(100))";

        String createKundenTable = "CREATE TABLE IF NOT EXISTS kunde ("
                + "kundennummer INT PRIMARY KEY, "
                + "vorname VARCHAR(50), "
                + "nachname VARCHAR(50), "
                + "branche VARCHAR(100), "
                + "strasse VARCHAR(100), "
                + "plz VARCHAR(10), "
                + "ort VARCHAR(50), "
                + "telefon VARCHAR(20), "
                + "email VARCHAR(100))";

        executeUpdate(createMitarbeiterTable, ps -> {});
        executeUpdate(createExterneMitarbeiterTable, ps -> {});
        executeUpdate(createKundenTable, ps -> {});
    }
    /**
     * Speichert einen Tabellen in der Datenbank.
     *
     */
    public static void saveToDatabase(Mitarbeiter mitarbeiter) {
        String sql = "INSERT INTO mitarbeiter (vorname, nachname, mitarbeiternummer, strasse, plz, ort, telefon, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, mitarbeiter.getVorname());
            pstmt.setString(2, mitarbeiter.getNachname());
            pstmt.setInt(3, mitarbeiter.getMitarbeiternummer());
            pstmt.setString(4, mitarbeiter.getStrasse());
            pstmt.setString(5, mitarbeiter.getPlz());
            pstmt.setString(6, mitarbeiter.getOrt());
            pstmt.setString(7, mitarbeiter.getTelefon());
            pstmt.setString(8, mitarbeiter.getEmail());

            System.out.println("Executing SQL: " + sql); // Debug statement
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting Mitarbeiter: " + e.getMessage());
        }
    }public static void saveToDatabase(ExternerMitarbeiter externerMitarbeiter) {
        String sql = "INSERT INTO externer_mitarbeiter (vorname, nachname, mitarbeiternummer, firma, strasse, plz, ort, telefon, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, externerMitarbeiter.getVorname());
            pstmt.setString(2, externerMitarbeiter.getNachname());
            pstmt.setInt(3, externerMitarbeiter.getMitarbeiternummer());
            pstmt.setString(4, externerMitarbeiter.getFirma());  // Zusätzlicher Wert für externe Mitarbeiter
            pstmt.setString(5, externerMitarbeiter.getStrasse());
            pstmt.setString(6, externerMitarbeiter.getPlz());
            pstmt.setString(7, externerMitarbeiter.getOrt());
            pstmt.setString(8, externerMitarbeiter.getTelefon());
            pstmt.setString(9, externerMitarbeiter.getEmail());

            System.out.println("Executing SQL: " + sql); // Debug statement
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting ExternerMitarbeiter: " + e.getMessage());
        }
    }

    public static void saveToDatabase(Kunde kunde) {
        String sql = "INSERT INTO kunde (vorname, nachname, kundennummer, strasse, plz, ort, telefon, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, kunde.getVorname());
            pstmt.setString(2, kunde.getNachname());
            pstmt.setInt(3, kunde.getKundennummer());
            pstmt.setString(4, kunde.getStrasse());
            pstmt.setString(5, kunde.getPlz());
            pstmt.setString(6, kunde.getOrt());
            pstmt.setString(7, kunde.getTelefon());
            pstmt.setString(8, kunde.getEmail());

            System.out.println("Executing SQL: " + sql); // Debug statement
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting Kunde: " + e.getMessage());
        }
    }


    private static void saveExternerMitarbeiter(ExternerMitarbeiter extern) {
        String sql = "INSERT INTO externermitarbeiter (vorname, nachname, mitarbeiternummer, firma, strasse, plz, ort, telefon, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeUpdate(sql, ps -> {
            ps.setString(1, extern.getVorname());
            ps.setString(2, extern.getNachname());
            ps.setInt(3, extern.getMitarbeiternummer());
            ps.setString(4, extern.getFirma());
            ps.setString(5, extern.getStrasse());
            ps.setString(6, extern.getPlz());
            ps.setString(7, extern.getOrt());
            ps.setString(8, extern.getTelefon());
            ps.setString(9, extern.getEmail());
        });
    }

    private static void saveKunde(Kunde kunde) {
        String sql = "INSERT INTO kunde (vorname, nachname, kundennummer, branche, strasse, plz, ort, telefon, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeUpdate(sql, ps -> {
            ps.setString(1, kunde.getVorname());
            ps.setString(2, kunde.getNachname());
            ps.setInt(3, kunde.getKundennummer());
            ps.setString(4, kunde.getBranche());
            ps.setString(5, kunde.getStrasse());
            ps.setString(6, kunde.getPlz());
            ps.setString(7, kunde.getOrt());
            ps.setString(8, kunde.getTelefon());
            ps.setString(9, kunde.getEmail());
        });
    }

    private static void executeUpdate(String sql, PreparedStatementSetter setter) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            setter.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
/**
 * Löscht ein Objekt aus der Datenbank.
 *
 * @param item Das zu löschende Objekt
 */
    public static void deleteFromDatabase(Object item, Class<?> type) {
        String sql = "";
        int id;

        if (item instanceof Mitarbeiter m) {
            sql = "DELETE FROM mitarbeiter WHERE mitarbeiternummer = ?";
            id = m.getMitarbeiternummer();
        } else if (item instanceof ExternerMitarbeiter em) {
            sql = "DELETE FROM externermitarbeiter WHERE mitarbeiternummer = ?";
            id = em.getMitarbeiternummer();
        } else if (item instanceof Kunde k) {
            sql = "DELETE FROM kunde WHERE kundennummer = ?";
            id = k.getKundennummer();
        } else {
            id = 0;
        }

        if (!sql.isEmpty()) {
            executeUpdate(sql, ps -> ps.setInt(1, id));
        }
    }

    /**
     * Ruft eine Liste von Objekten aus der Datenbank ab.
     *
     * @param table  Der Name der Tabelle
     * @param mapper Der RowMapper zur Umwandlung von Zeilen
     * @return Eine Liste der Objekte
     */
    public static <T> List<T> fetchFromDatabase(String table, RowMapper<T> mapper) {
        List<T> items = new ArrayList<>();
        String sql = "SELECT * FROM " + table;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(mapper.mapRow(rs)); // Verwende den RowMapper, um die Zeile in ein Objekt vom Typ T zu mappen
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }
    /**
     * Schließt die Verbindung zur Datenbank.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Verbindung geschlossen.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}