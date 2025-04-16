package main.java.db;

import main.java.model.Kunde;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Diese Klasse implementiert den RowMapper für die Entität Kunde.
 * Sie wandelt eine Datenbankzeile in ein Kunde-Objekt um.
 */
public class KundeRowMapper implements DatabaseHandler.RowMapper<Kunde> {

    /**
     * Mappt eine Zeile aus dem ResultSet auf ein Kunde-Objekt.
     *
     * @param rs das ResultSet, das die aktuelle Zeile enthält
     * @return ein Kunde-Objekt mit den aus der Datenbank gelesenen Werten
     * @throws SQLException falls ein Fehler beim Zugriff auf die Spalten auftritt
     */
    @Override
    public Kunde mapRow(ResultSet rs) throws SQLException {
        Kunde kunde = new Kunde();
        kunde.setVorname(rs.getString("vorname"));
        kunde.setNachname(rs.getString("nachname"));
        kunde.setKundennummer(rs.getInt("kundennummer"));
        kunde.setBranche(rs.getString("branche"));
        kunde.setStrasse(rs.getString("strasse"));
        kunde.setPlz(rs.getString("plz"));
        kunde.setOrt(rs.getString("ort"));
        kunde.setTelefon(rs.getString("telefon"));
        kunde.setEmail(rs.getString("email"));
        return kunde;
    }
}
