public class Manager {
    private String name;
    private int id;
    private Abteilung abteilung;

    public Manager(String name, int id, Abteilung abteilung) {
        this.name = name;
        this.id = id;
        this.abteilung = abteilung;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Abteilung getAbteilung() {
        return abteilung;
    }

    @Override
    public String toString() {
        return "Manager [Name: " + name + ", ID: " + id + ", Abteilung: " + abteilung.getName() + "]";
    }
}