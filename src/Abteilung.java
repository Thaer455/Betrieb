public class Abteilung {
    private String name;
    private int id;
    private String standort;

    public Abteilung(String name, int id, String standort) {
        this.name = name;
        this.id = id;
        this.standort = standort;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getStandort() {
        return standort;
    }

    @Override
    public String toString() {
        return "Abteilung [Name: " + name + ", ID: " + id + ", Standort: " + standort + "]";
    }
}