public class Produkt {
    private String name;
    private int id;
    private double preis;

    public Produkt(String name, int id, double preis) {
        this.name = name;
        this.id = id;
        this.preis = preis;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getPreis() {
        return preis;
    }

    @Override
    public String toString() {
        return "Produkt [Name: " + name + ", ID: " + id + ", Preis: " + preis + "]";
    }
}