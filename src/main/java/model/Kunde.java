package main.java.model;

/**
 * Represents a customer, extending the Person class.
 */
public class Kunde extends Person {
    private int kundennummer;
    private String branche;

    public Kunde() {}

    public Kunde(String vorname, String nachname, int kundennummer, String branche, String strasse, String plz, String ort, String telefon, String email) {
        super(vorname, nachname, strasse, plz, ort, telefon, email);
        this.kundennummer = kundennummer;
        this.branche = branche;
    }

    public int getKundennummer() {
        return kundennummer;
    }

    public void setKundennummer(int kundennummer) {
        this.kundennummer = kundennummer;
    }

    public String getBranche() {
        return branche;
    }

    public void setBranche(String branche) {
        this.branche = branche;
    }

    @Override
    public String toString() {
        return "Kunde{" +
                "kundennummer=" + kundennummer +
                ", branche='" + branche + '\'' +
                "} " + super.toString();
    }
}
