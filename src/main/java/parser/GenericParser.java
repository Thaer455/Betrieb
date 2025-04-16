package main.java.parser;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.List;

public class GenericParser<T> {
    private final String filePath;

    public GenericParser(String filePath) {
        this.filePath = filePath;
    }

    public List<T> parse() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();

            GenericContentHandler<T> handler = new GenericContentHandler<>();
            xmlReader.setContentHandler(handler);

            System.out.println("\n\nParsing started...\n\n");
            xmlReader.parse(new InputSource(new File(filePath).toURI().toString()));

            return handler.getItems();
        } catch (Exception e) {
            System.err.println("Error reading XML:");
            e.printStackTrace();
            return null;
        }
    }
}