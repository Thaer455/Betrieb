package main.java.xml;

import javafx.collections.ObservableList;
import main.java.model.ExternerMitarbeiter;
import main.java.model.Kunde;
import main.java.model.Mitarbeiter;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Verarbeitet das Exportieren von Daten in eine XML-Datei.
 */
public class XMLHandler {

    /**
     * Exportiert Daten eines beliebigen Typs in eine XML-Datei.
     *
     * @param filePath Pfad zur XML-Datei
     * @param data Liste der zu exportierenden Daten
     * @param type Der Datentyp (Mitarbeiter, ExternerMitarbeiter, Kunde)
     */
    public static <T> void exportToXML(String filePath, ObservableList<?> data, Class<?> type) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement(type.getSimpleName() + "s");
            doc.appendChild(rootElement);

            for (Object item : data) {
                if (type == Mitarbeiter.class) {
                    rootElement.appendChild(createMitarbeiterElement(doc, (Mitarbeiter) item));
                } else if (type == ExternerMitarbeiter.class) {
                    rootElement.appendChild(createExternerMitarbeiterElement(doc, (ExternerMitarbeiter) item));
                } else if (type == Kunde.class) {
                    rootElement.appendChild(createKundeElement(doc, (Kunde) item));
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

            System.out.println("Daten erfolgreich in XML exportiert: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Element createMitarbeiterElement(Document doc, Mitarbeiter mitarbeiter) {
        Element mitarbeiterElement = doc.createElement("Mitarbeiter");

        addElement(doc, mitarbeiterElement, "vorname", mitarbeiter.getVorname());
        addElement(doc, mitarbeiterElement, "nachname", mitarbeiter.getNachname());
        addElement(doc, mitarbeiterElement, "mitarbeiternummer", String.valueOf(mitarbeiter.getMitarbeiternummer()));
        addElement(doc, mitarbeiterElement, "strasse", mitarbeiter.getStrasse());
        addElement(doc, mitarbeiterElement, "plz", mitarbeiter.getPlz());
        addElement(doc, mitarbeiterElement, "ort", mitarbeiter.getOrt());
        addElement(doc, mitarbeiterElement, "telefon", mitarbeiter.getTelefon());
        addElement(doc, mitarbeiterElement, "email", mitarbeiter.getEmail());

        return mitarbeiterElement;
    }

    private static Element createExternerMitarbeiterElement(Document doc, ExternerMitarbeiter extern) {
        Element externElement = doc.createElement("ExternerMitarbeiter");

        addElement(doc, externElement, "vorname", extern.getVorname());
        addElement(doc, externElement, "nachname", extern.getNachname());
        addElement(doc, externElement, "mitarbeiternummer", String.valueOf(extern.getMitarbeiternummer()));
        addElement(doc, externElement, "firma", extern.getFirma());
        addElement(doc, externElement, "strasse", extern.getStrasse());
        addElement(doc, externElement, "plz", extern.getPlz());
        addElement(doc, externElement, "ort", extern.getOrt());
        addElement(doc, externElement, "telefon", extern.getTelefon());
        addElement(doc, externElement, "email", extern.getEmail());

        return externElement;
    }

    private static Element createKundeElement(Document doc, Kunde kunde) {
        Element kundeElement = doc.createElement("Kunde");

        addElement(doc, kundeElement, "vorname", kunde.getVorname());
        addElement(doc, kundeElement, "nachname", kunde.getNachname());
        addElement(doc, kundeElement, "kundennummer", String.valueOf(kunde.getKundennummer()));
        addElement(doc, kundeElement, "branche", kunde.getBranche());
        addElement(doc, kundeElement, "strasse", kunde.getStrasse());
        addElement(doc, kundeElement, "plz", kunde.getPlz());
        addElement(doc, kundeElement, "ort", kunde.getOrt());
        addElement(doc, kundeElement, "telefon", kunde.getTelefon());
        addElement(doc, kundeElement, "email", kunde.getEmail());

        return kundeElement;
    }

    private static void addElement(Document doc, Element parent, String name, String value) {
        Element element = doc.createElement(name);
        element.appendChild(doc.createTextNode(value));
        parent.appendChild(element);
    }
}
