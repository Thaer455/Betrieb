package main.java.db;

import main.java.model.ExternerMitarbeiter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper-Klasse zur Umwandlung einer ResultSet-Zeile in ein ExternerMitarbeiter-Objekt.
 */
public class ExternerMitarbeiterRowMapper implements DatabaseHandler.RowMapper<ExternerMitarbeiter> {

    /**
     * Erstellt ein {@link ExternerMitarbeiter}-Objekt aus einer Datenbankzeile.
     *
     * @param rs das ResultSet, das die Datenbankzeile enthält
     * @return ein ExternerMitarbeiter-Objekt mit den ausgelesenen Werten
     * @throws SQLException falls ein Fehler beim Zugriff auf das ResultSet auftritt
     */
    @Override
    public ExternerMitarbeiter mapRow(ResultSet rs) throws SQLException {
        ExternerMitarbeiter externerMitarbeiter = new ExternerMitarbeiter();
        externerMitarbeiter.setVorname(rs.getString("vorname"));
        externerMitarbeiter.setNachname(rs.getString("nachname"));
        externerMitarbeiter.setMitarbeiternummer(rs.getInt("mitarbeiternummer"));
        externerMitarbeiter.setFirma(rs.getString("firma"));
        externerMitarbeiter.setStrasse(rs.getString("strasse"));
        externerMitarbeiter.setPlz(rs.getString("plz"));
        externerMitarbeiter.setOrt(rs.getString("ort"));
        externerMitarbeiter.setTelefon(rs.getString("telefon"));
        externerMitarbeiter.setEmail(rs.getString("email"));
        return externerMitarbeiter;
    }
}