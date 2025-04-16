package main.java.model;

import main.java.db.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Mitarbeiter extends Person implements RowMapper<Mitarbeiter> {
    private int mitarbeiternummer;

    /**
     * Konstruktor mit Parametern.
     *
     * @param vorname   Der Vorname des Mitarbeiters.
     * @param nachname  Der Nachname des Mitarbeiters.
     * @param mitarbeiternummer Die Mitarbeiternummer des Mitarbeiters als String.
     * @param strasse   Die Straße des Mitarbeiters.
     * @param plz       Die PLZ des Mitarbeiters.
     * @param ort       Der Ort des Mitarbeiters.
     * @param telefon   Die Telefonnummer des Mitarbeiters.
     * @param email     Die E-Mail-Adresse des Mitarbeiters.
     */
    public Mitarbeiter(String vorname, String nachname, int mitarbeiternummer, String strasse, String plz, String ort, String telefon, String email) {
        super(vorname, nachname, strasse, plz, ort, telefon, email);
        try {
            this.mitarbeiternummer = Integer.parseInt(String.valueOf(mitarbeiternummer));
        } catch (NumberFormatException e) {
            System.err.println("Ungültige Mitarbeiternummer: " + mitarbeiternummer);
            this.mitarbeiternummer = -1; // Standardwert für ungültige Eingaben
        }
    }

    /**
     * Standardkonstruktor.
     */
    public Mitarbeiter() {}

    /**
     * Gibt die Mitarbeiternummer zurück.
     *
     * @return Die Mitarbeiternummer.
     */
    public int getMitarbeiternummer() {
        return mitarbeiternummer;
    }

    /**
     * Setzt die Mitarbeiternummer.
     *
     * @param mitarbeiternummer Die neue Mitarbeiternummer als int.
     */
    public void setMitarbeiternummer(int mitarbeiternummer) {
        this.mitarbeiternummer = mitarbeiternummer;
    }

    /**
     * Zusätzliche Methode zur Unterstützung von String-Eingaben im Parser.
     *
     * @param mitarbeiternummer Die Mitarbeiternummer als String.
     */
    public void setMitarbeiternummer(String mitarbeiternummer) {
        try {
            this.mitarbeiternummer = Integer.parseInt(mitarbeiternummer.trim());
        } catch (NumberFormatException e) {
            System.err.println("Ungültige Mitarbeiternummer: " + mitarbeiternummer);
            this.mitarbeiternummer = -1; // Standardwert für ungültige Eingaben
        }
    }

    /**
     * Implementierung der RowMapper-Schnittstelle.
     *
     * @param rs Das ResultSet aus der Datenbank.
     * @return Ein Mitarbeiter-Objekt.
     * @throws SQLException Bei Fehlern beim Zugriff auf das ResultSet.
     */
    @Override
    public Mitarbeiter mapRow(ResultSet rs) throws SQLException {
        Mitarbeiter mitarbeiter = new Mitarbeiter();

        // Setze die Werte über die Setter-Methoden
        mitarbeiter.setVorname(rs.getString("vorname"));
        mitarbeiter.setNachname(rs.getString("nachname"));
        mitarbeiter.setMitarbeiternummer(rs.getInt("mitarbeiternummer")); // Hier wird die int-Version des Setters verwendet
        mitarbeiter.setStrasse(rs.getString("strasse"));
        mitarbeiter.setPlz(rs.getString("plz"));
        mitarbeiter.setOrt(rs.getString("ort"));
        mitarbeiter.setTelefon(rs.getString("telefon"));
        mitarbeiter.setEmail(rs.getString("email"));

        return mitarbeiter;
    }

    /**
     * Gibt eine Zeichenkette mit den Informationen des Mitarbeiters zurück.
     *
     * @return Eine Zeichenkette mit den Informationen des Mitarbeiters.
     */
    @Override
    public String toString() {
        return "Mitarbeiter{" +
                "Mitarbeiternummer=" + mitarbeiternummer +
                "} " + super.toString();
    }
}