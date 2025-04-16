package main.java.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mappt eine Zeile aus dem ResultSet auf ein Objekt vom Typ T.
 * @param <T> der Typ des Objekts, auf das die Zeile gemappt wird
 */
public interface RowMapper<T> {
    T mapRow(ResultSet rs) throws SQLException;
}
