import java.io.*;
import java.util.*;
import java.util.function.Predicate;

public class Betrieb {
    private final ArrayList<Angestellte> angestellteListe = new ArrayList<>();
    private final ArrayList<Manager> managerListe = new ArrayList<>();
    private final ArrayList<Abteilung> abteilungListe = new ArrayList<>();
    private final ArrayList<Produkt> produktListe = new ArrayList<>();

    public void readFromCSV(String filename, String type) {
        File file = new File(filename);
        if (!file.exists() || !file.canRead()) {
            System.out.println("Datei existiert nicht oder kann nicht gelesen werden: " + filename);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                try {
                    switch (type) {
                        case "Angestellte":
                            if (data.length == 5) {
                                String vorname = data[0].trim();
                                String nachname = data[1].trim();
                                int id = Integer.parseInt(data[2].trim());
                                double gehalt = Double.parseDouble(data[3].trim());
                                String abteilungName = data[4].trim();

                                Abteilung abteilung = findAbteilungByName(abteilungName);
                                if (abteilung != null) {
                                    angestellteListe.add(new Angestellte(vorname, nachname, id, gehalt, abteilung));
                                } else {
                                    System.out.println("Abteilung nicht gefunden: " + abteilungName);
                                }
                            } else {
                                System.out.println("Ungültige Daten in der Zeile: " + line);
                            }
                            break;
                        case "Manager":
                            if (data.length == 3) {
                                String name = data[0].trim();
                                int id = Integer.parseInt(data[1].trim());
                                String abteilungName = data[2].trim();

                                Abteilung abteilung = findAbteilungByName(abteilungName);
                                if (abteilung != null) {
                                    managerListe.add(new Manager(name, id, abteilung));
                                } else {
                                    System.out.println("Abteilung nicht gefunden: " + abteilungName);
                                }
                            } else {
                                System.out.println("Ungültige Daten in der Zeile: " + line);
                            }
                            break;
                        case "Abteilung":
                            if (data.length == 3) {
                                String name = data[0].trim();
                                int id = Integer.parseInt(data[1].trim());
                                String standort = data[2].trim();
                                abteilungListe.add(new Abteilung(name, id, standort));
                            } else {
                                System.out.println("Ungültige Daten in der Zeile: " + line);
                            }
                            break;
                        case "Produkt":
                            if (data.length == 3) {
                                String name = data[0].trim();
                                int id = Integer.parseInt(data[1].trim());
                                double preis = Double.parseDouble(data[2].trim());
                                produktListe.add(new Produkt(name, id, preis));
                            } else {
                                System.out.println("Ungültige Daten in der Zeile: " + line);
                            }
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Fehler beim Konvertieren der Daten in der Zeile: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToCSV(String filename, String type) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            switch (type) {
                case "Angestellte":
                    for (Angestellte a : angestellteListe) {
                        bw.write(a.getVorname() + ", " + a.getNachname() + ", " + a.getId() + ", " + a.getGehalt() + ", " + a.getAbteilung().getName());
                        bw.newLine();
                    }
                    break;
                case "Manager":
                    for (Manager m : managerListe) {
                        bw.write(m.getName() + ", " + m.getId() + ", " + m.getAbteilung().getName());
                        bw.newLine();
                    }
                    break;
                case "Abteilung":
                    for (Abteilung abt : abteilungListe) {
                        bw.write(abt.getName() + ", " + abt.getId() + ", " + abt.getStandort());
                        bw.newLine();
                    }
                    break;
                case "Produkt":
                    for (Produkt p : produktListe) {
                        bw.write(p.getName() + ", " + p.getId() + ", " + p.getPreis());
                        bw.newLine();
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortAngestellte(Comparator<Angestellte> comparator) {
        angestellteListe.sort(comparator);
    }

    public void sortProdukte(Comparator<Produkt> comparator) {
        produktListe.sort(comparator);
    }

    public ArrayList<Angestellte> searchAngestellte(Predicate<Angestellte> predicate) {
        ArrayList<Angestellte> results = new ArrayList<>();
        for (Angestellte a : angestellteListe) {
            if (predicate.test(a)) {
                results.add(a);
            }
        }
        return results;
    }

    public ArrayList<Produkt> searchProdukt(Predicate<Produkt> predicate) {
        ArrayList<Produkt> results = new ArrayList<>();
        for (Produkt p : produktListe) {
            if (predicate.test(p)) {
                results.add(p);
            }
        }
        return results;
    }

    public ArrayList<Manager> searchManager(Predicate<Manager> predicate) {
        ArrayList<Manager> results = new ArrayList<>();
        for (Manager m : managerListe) {
            if (predicate.test(m)) {
                results.add(m);
            }
        }
        return results;
    }

    private Abteilung findAbteilungByName(String name) {
        for (Abteilung abt : abteilungListe) {
            if (abt.getName().equalsIgnoreCase(name)) {
                return abt;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Betrieb main = new Betrieb();
        main.readFromCSV("data/abteilung.csv", "Abteilung");
        main.readFromCSV("data/angestellte.csv", "Angestellte");
        main.readFromCSV("data/manager.csv", "Manager");
        main.readFromCSV("data/produkte.csv", "Produkt");

        main.sortAngestellte(Comparator.comparing(Angestellte::getNachname));
        main.sortAngestellte(Comparator.comparing(Angestellte::getGehalt).reversed());
        main.sortProdukte(Comparator.comparing(Produkt::getPreis));


        main.writeToCSV("data/sorted_angestellte.csv", "Angestellte");
        main.writeToCSV("data/sorted_produkte.csv", "Produkt");


        ArrayList<Angestellte> angestellteNachname = main.searchAngestellte(a -> a.getNachname().equalsIgnoreCase("Haddad"));
        ArrayList<Produkt> produkteByName = main.searchProdukt(p -> p.getName().equalsIgnoreCase("Maus"));
        ArrayList<Manager> managerById = main.searchManager(m -> m.getId() == 1);

        System.out.println("Angestellte mit Nachname 'Haddad':");
        for (Angestellte a : angestellteNachname) {
            System.out.println(a);
        }
        System.out.println();

        System.out.println("Name des Produkt 'Maus':");
        for (Produkt p : produkteByName) {
            System.out.println(p);
        }
        System.out.println();

        System.out.println("Manager mit ID 1:");
        for (Manager m : managerById) {
            System.out.println(m);
        }
        System.out.println();

        System.out.println("Alle Manager:");
        for (Manager m : main.managerListe) {
            System.out.println(m);
        }
        System.out.println();


    }
}