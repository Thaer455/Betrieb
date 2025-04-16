package main.java.db;

import main.java.model.Mitarbeiter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementierung eines RowMappers für Mitarbeiter-Objekte.
 * Konvertiert eine Zeile aus dem ResultSet in ein Mitarbeiter-Objekt.
 */
public class MitarbeiterRowMapper implements DatabaseHandler.RowMapper<Mitarbeiter> {

    /**
     * Wandelt eine Zeile aus einem ResultSet in ein Mitarbeiter-Objekt um.
     *
     * @param rs das ResultSet, das die Datenbankergebnisse enthält
     * @return ein Mitarbeiter-Objekt mit den aus der Datenbank gelesenen Werten
     * @throws SQLException falls ein Fehler beim Zugriff auf die Daten auftritt
     */
    @Override
    public Mitarbeiter mapRow(ResultSet rs) throws SQLException {
        Mitarbeiter mitarbeiter = new Mitarbeiter();
        mitarbeiter.setVorname(rs.getString("vorname"));
        mitarbeiter.setNachname(rs.getString("nachname"));
        mitarbeiter.setMitarbeiternummer(rs.getInt("mitarbeiternummer"));
        mitarbeiter.setStrasse(rs.getString("strasse"));
        mitarbeiter.setPlz(rs.getString("plz"));
        mitarbeiter.setOrt(rs.getString("ort"));
        mitarbeiter.setTelefon(rs.getString("telefon"));
        mitarbeiter.setEmail(rs.getString("email"));
        return mitarbeiter;
    }
}
