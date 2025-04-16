package main.java.model;

/**
 * Represents a person with basic contact information.
 */
public class Person {
    private String vorname;
    private String nachname;
    private String strasse;
    private String plz;
    private String ort;
    private String telefon;
    private String email;

    public Person(String vorname, String nachname, String strasse, String plz, String ort, String telefon, String email) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.strasse = strasse;
        this.plz = plz;
        this.ort = ort;
        this.telefon = telefon;
        this.email = email;
    }

    public Person() {}

    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getNachname() { return nachname; }
    public void setNachname(String nachname) { this.nachname = nachname; }

    public String getStrasse() { return strasse; }
    public void setStrasse(String strasse) { this.strasse = strasse; }

    public String getPlz() { return plz; }
    public void setPlz(String plz) { this.plz = plz; }

    public String getOrt() { return ort; }
    public void setOrt(String ort) { this.ort = ort; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Person{" +
                "Vorname='" + vorname + '\'' +
                ", Nachname='" + nachname + '\'' +
                ", Strasse='" + strasse + '\'' +
                ", Plz='" + plz + '\'' +
                ", Ort='" + ort + '\'' +
                ", Telefon='" + telefon + '\'' +
                ", Email='" + email + '\'' +
                '}';
    }
}
