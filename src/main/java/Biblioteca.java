import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class Biblioteca {
    private Map<String, Pubblicazione> catalogo;

    public Biblioteca() {
        this.catalogo = new HashMap<>();
    }

    public void aggiungiPubblicazione() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci i dettagli della Pubblicazione:");

        System.out.println("ISBN:");
        String isbn = scanner.nextLine();

        System.out.println("Titolo:");
        String titolo = scanner.nextLine();

        System.out.println("Anno di Pubblicazione:");
        int annoPubblicazione = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Numero di pagine:");
        int numeroPagine = scanner.nextInt();
        scanner.nextLine();

        catalogo.put(isbn, new Pubblicazione(isbn, titolo, annoPubblicazione, numeroPagine));

        salvaCatalogo("catalogo.txt");

        System.out.println("Pubblicazione aggiunta con successo!");
        stampaCatalogo();
    }

    public void rimuoviPubblicazione() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci l'ISBN della Pubblicazione da rimuovere:");
        String isbn = scanner.nextLine();

        if (catalogo.containsKey(isbn)) {
            catalogo.remove(isbn);
            salvaCatalogo("catalogo.txt");
            System.out.println("Pubblicazione rimossa con successo!");
            stampaCatalogo();
        } else {
            System.out.println("Nessuna pubblicazione trovata con ISBN " + isbn);
        }
    }

    public void salvaCatalogo(String percorsoFile) {
        try {
            List<String> lines = new ArrayList<>();
            for (Pubblicazione pubblicazione : catalogo.values()) {
                lines.add(pubblicazione.toFileString());
            }
            FileUtils.writeLines(new File(percorsoFile), lines, false);
            System.out.println("Catalogo salvato con successo!");
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del catalogo: " + e.getMessage());
        }
    }

    public void caricaCatalogo(String percorsoFile) {
        try {
            List<String> lines = FileUtils.readLines(new File(percorsoFile), Charset.defaultCharset());
            for (String line : lines) {
                Pubblicazione pubblicazione = parsePubblicazioneFromString(line);
                catalogo.put(pubblicazione.getIsbn(), pubblicazione);
            }
            System.out.println("Catalogo caricato con successo!");
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento del catalogo: " + e.getMessage());
        }
    }


    private Pubblicazione parsePubblicazioneFromString(String valueString) {
        String[] parts = valueString.split(";");
        String isbn = "";
        String titolo = "";
        int annoPubblicazione = 0;
        int numeroPagine = 0;

        for (String part : parts) {
            String[] keyValue = part.split("=");
            if (keyValue.length != 2) {
                System.err.println("Formato dati non valido: " + valueString);
                return null;
            }

            String fieldName = keyValue[0].trim();
            String fieldValue = keyValue[1].trim();

            switch (fieldName) {
                case "ISBN":
                    isbn = fieldValue;
                    break;
                case "Titolo":
                    titolo = fieldValue;
                    break;
                case "Anno di pubblicazione":
                    annoPubblicazione = Integer.parseInt(fieldValue);
                    break;
                case "Numero di pagine":
                    numeroPagine = Integer.parseInt(fieldValue);
                    break;
                default:
                    System.err.println("Campo non riconosciuto: " + fieldName);
                    break;
            }
        }

        return new Pubblicazione(isbn, titolo, annoPubblicazione, numeroPagine);
    }

    public void stampaCatalogo() {
        System.out.println("Catalogo attuale:");
        catalogo.values().forEach(System.out::println);
    }

    public Optional<Pubblicazione> ricercaPerIsbn(String isbn) {
        return Optional.ofNullable(catalogo.get(isbn));
    }

    public List<Pubblicazione> ricercaPerAnnoPubblicazione(int anno) {
        return catalogo.values().stream()
                .filter(p -> p.getAnnoPubblicazione() == anno)
                .collect(Collectors.toList());
    }

    public List<Pubblicazione> ricercaPerAutore(String autore) {
        List<Pubblicazione> risultati = new ArrayList<>();
        for (Pubblicazione pubblicazione : catalogo.values()) {
            if (pubblicazione instanceof Libro libro) {
                if (libro.getAutore().equals(autore)) {
                    risultati.add(pubblicazione);
                }
            }
        }
        return risultati;
    }

    public void avvioApplicazione() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Scegli un'azione:");
            System.out.println("1 - Aggiungi Pubblicazione");
            System.out.println("2 - Rimuovi Pubblicazione");
            System.out.println("3 - Ricerca per ISBN");
            System.out.println("4 - Ricerca per Anno di Pubblicazione");
            System.out.println("5 - Ricerca per Autore");
            System.out.println("6 - Stampare Catalogo");
            System.out.println("0 - Esci");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    aggiungiPubblicazione();
                    break;
                case 2:
                    rimuoviPubblicazione();
                    break;
                case 3:
                    System.out.println("Inserisci l'ISBN della Pubblicazione da cercare:");
                    String isbnDaCercare = scanner.nextLine();
                    Optional<Pubblicazione> pubblicazione = ricercaPerIsbn(isbnDaCercare);
                    if (pubblicazione.isPresent()) {
                        System.out.println("Pubblicazione trovata: " + pubblicazione.get());
                    } else {
                        System.out.println("Nessuna pubblicazione trovata con ISBN " + isbnDaCercare);
                    }
                    break;
                case 4:
                    System.out.println("Inserisci l'anno di pubblicazione:");
                    int anno = scanner.nextInt();
                    List<Pubblicazione> pubblicazioniPerAnno = ricercaPerAnnoPubblicazione(anno);
                    if (!pubblicazioniPerAnno.isEmpty()) {
                        System.out.println("Pubblicazioni trovate per l'anno " + anno + ":");
                        pubblicazioniPerAnno.forEach(System.out::println);
                    } else {
                        System.out.println("Nessuna pubblicazione trovata per l'anno " + anno);
                    }
                    break;
                case 5:
                    System.out.println("Inserisci l'autore da cercare:");
                    String autore = scanner.nextLine();
                    List<Pubblicazione> pubblicazioniPerAutore = ricercaPerAutore(autore);
                    if (!pubblicazioniPerAutore.isEmpty()) {
                        System.out.println("Pubblicazioni trovate per l'autore " + autore + ":");
                        pubblicazioniPerAutore.forEach(System.out::println);
                    } else {
                        System.out.println("Nessuna pubblicazione trovata per l'autore " + autore);
                    }
                    break;
                case 6:
                    stampaCatalogo();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Scelta non valida, riprova.");
            }
        }
        scanner.close();
    }
}
