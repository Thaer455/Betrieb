public class Angestellte {
    private int id;
    private String vorname;
    private String nachname;
    private double gehalt;
    private Abteilung abteilung; // Verweis auf ein Abteilung-Objekt

    public Angestellte(String vorname, String nachname, int id, double gehalt, Abteilung abteilung) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.gehalt = gehalt;
        this.abteilung = abteilung; // Abteilung als Objekt
    }

    public int getId() {
        return id;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public double getGehalt() {
        return gehalt;
    }

    public Abteilung getAbteilung() {
        return abteilung;
    }

    @Override
    public String toString() {
        return "Angestellte [ID: " + id + ", Name: " + vorname + " " + nachname + ", Gehalt: " + gehalt + ", Abteilung: " + abteilung.getName() + "]";
    }
}