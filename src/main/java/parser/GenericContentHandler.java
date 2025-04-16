package main.java.parser;

import main.java.model.ExternerMitarbeiter;
import main.java.model.Kunde;
import main.java.model.Mitarbeiter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Ein SAX-Handler für das Parsen von XML-Daten in Java-Objekte.
 * Der Handler liest XML-Daten und mappt sie auf Objekte der Typen {@link Mitarbeiter}, {@link ExternerMitarbeiter} oder {@link Kunde}.
 * Die entsprechenden Eigenschaften der Objekte werden durch die XML-Tags gesetzt.
 *
 * @param <T> Der Typ der Objekte, die aus dem XML-Dokument erstellt werden sollen
 */
public class GenericContentHandler<T> extends DefaultHandler {
    private List<T> items; // Liste der erstellten Objekte
    private StringBuilder data; // Zwischenpuffer für die XML-Daten
    private String currentElement; // Aktuelles XML-Element
    private T currentItem; // Aktuelles Objekt, das erstellt wird

    /**
     * Konstruktor für den Handler.
     * Initialisiert die Liste für Objekte und den Datenpuffer.
     */
    public GenericContentHandler() {
        items = new ArrayList<>();
        data = new StringBuilder();
    }

    /**
     * Wird zu Beginn des Parsens aufgerufen.
     * Zeigt eine Meldung an, dass das Lesen des XML-Dokuments begonnen hat.
     */
    @Override
    public void startDocument() throws SAXException {
        System.out.println("Start reading XML...");
    }

    /**
     * Wird am Ende des Parsens aufgerufen.
     * Zeigt eine Meldung an, dass das XML-Dokument erfolgreich gelesen wurde.
     */
    @Override
    public void endDocument() throws SAXException {
        System.out.println("XML read successfully.");
    }

    /**
     * Wird aufgerufen, wenn ein neues XML-Element beginnt.
     * Erstellt ein neues Objekt basierend auf dem XML-Tag (Mitarbeiter, ExternerMitarbeiter, Kunde).
     *
     * @param uri Der Namensraum des Elements
     * @param localName Der lokale Name des Elements
     * @param qName Der qualifizierte Name des Elements
     * @param attributes Die Attribute des Elements
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentElement = qName;

        // Wenn ein Mitarbeiter, ExternerMitarbeiter oder Kunde-Tag gefunden wird, ein neues Objekt erstellen
        if (qName.equals("Mitarbeiter") || qName.equals("ExternerMitarbeiter") || qName.equals("Kunde")) {
            try {
                Class<?> clazz;
                if (qName.equals("Mitarbeiter")) {
                    clazz = Mitarbeiter.class;
                } else if (qName.equals("ExternerMitarbeiter")) {
                    clazz = ExternerMitarbeiter.class;
                } else {
                    clazz = Kunde.class;
                }
                currentItem = (T) clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                System.err.println("Fehler beim Erstellen einer Instanz für: " + qName + " - " + e.getMessage());
                currentItem = null;
            }
        }
    }

    /**
     * Wird aufgerufen, wenn ein XML-Element endet.
     * Setzt den Wert des aktuellen Objekts basierend auf den XML-Daten.
     * Fügt das Objekt der Liste hinzu, wenn es ein Mitarbeiter-, ExternerMitarbeiter- oder Kunde-Tag ist.
     *
     * @param uri Der Namensraum des Elements
     * @param localName Der lokale Name des Elements
     * @param qName Der qualifizierte Name des Elements
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentItem != null) {
            String value = data.toString().trim();
            if (!value.isEmpty()) {
                if (qName.equals("Mitarbeiternummer") || qName.equals("Kundennummer")) {
                    setProperty(currentItem, qName, value, int.class);
                } else {
                    setProperty(currentItem, qName, value, String.class);
                }
            }
        }

        // Wenn ein Mitarbeiter, ExternerMitarbeiter oder Kunde-Tag endet, das Objekt zur Liste hinzufügen
        if (qName.equals("Mitarbeiter") || qName.equals("ExternerMitarbeiter") || qName.equals("Kunde")) {
            if (currentItem != null) {
                items.add(currentItem);
            }
            currentItem = null;
        }

        data.setLength(0);
    }

    /**
     * Wird aufgerufen, um die Zeichen innerhalb eines XML-Elements zu verarbeiten.
     * Fügt die Zeichen zum Datenpuffer hinzu.
     *
     * @param ch Das Zeichenarray
     * @param start Der Startindex
     * @param length Die Anzahl der Zeichen
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        data.append(ch, start, length);
    }

    /**
     * Setzt den Wert einer Eigenschaft des Objekts basierend auf dem XML-Tag.
     *
     * @param obj Das Objekt, dessen Eigenschaft gesetzt werden soll
     * @param property Der Name der Eigenschaft
     * @param value Der Wert, der gesetzt werden soll
     * @param type Der Typ der Eigenschaft
     */
    private void setProperty(Object obj, String property, String value, Class<?> type) {
        try {
            String setterName = "set" + capitalize(property);
            Method method = obj.getClass().getMethod(setterName, type);
            Object convertedValue = convertValue(value, type);
            method.invoke(obj, convertedValue);
        } catch (NoSuchMethodException e) {
            System.err.println("Setter nicht gefunden für: " + property);
        } catch (Exception e) {
            System.err.println("Fehler beim Setzen von '" + property + "': " + e.getMessage());
        }
    }

    /**
     * Konvertiert den Wert von String in den entsprechenden Typ.
     *
     * @param value Der zu konvertierende Wert
     * @param type Der Typ, in den der Wert konvertiert werden soll
     * @return Der konvertierte Wert
     */
    private Object convertValue(String value, Class<?> type) {
        value = value.trim();
        try {
            if (type == int.class || type == Integer.class) {
                return Integer.parseInt(value);
            } else if (type == double.class || type == Double.class) {
                return Double.parseDouble(value);
            } else if (type == boolean.class || type == Boolean.class) {
                return Boolean.parseBoolean(value);
            }
        } catch (NumberFormatException e) {
            System.err.println("Ungültiger Wert für " + type.getSimpleName() + ": " + value);
        }
        return value;
    }

    /**
     * Wandelt den ersten Buchstaben eines Strings in Großbuchstaben um.
     *
     * @param str Der String, dessen ersten Buchstaben großgeschrieben werden soll
     * @return Der modifizierte String mit dem ersten Großbuchstaben
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Gibt die Liste der erstellten Objekte zurück.
     *
     * @return Die Liste der Objekte
     */
    public List<T> getItems() {
        return items;
    }
}
