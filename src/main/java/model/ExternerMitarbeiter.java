package main.java.model;

/**
 * Represents an external employee, extending the Person class.
 */
public class ExternerMitarbeiter extends Person {
    private int mitarbeiternummer;
    private String firma;

    public ExternerMitarbeiter() {}

    public ExternerMitarbeiter(String vorname, String nachname, int mitarbeiternummer, String firma, String strasse, String plz, String ort, String telefon, String email) {
        super(vorname, nachname, strasse, plz, ort, telefon, email);
        this.mitarbeiternummer = mitarbeiternummer;
        this.firma = firma;
    }

    public int getMitarbeiternummer() {
        return mitarbeiternummer;
    }

    public void setMitarbeiternummer(int mitarbeiternummer) {
        this.mitarbeiternummer = mitarbeiternummer;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    @Override
    public String toString() {
        return "ExternerMitarbeiter{" +
                "mitarbeiternummer=" + mitarbeiternummer +
                ", firma='" + firma + '\'' +
                "} " + super.toString();
    }
}
