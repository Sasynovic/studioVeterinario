import java.io.Console;
import java.util.List;
import java.util.Scanner;
import models.*;
import dao.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== SISTEMA DI GESTIONE ANIMALI ===");
        System.out.println();

        // Mostra menu iniziale
        Utente utenteLoggato = mostraMenuIniziale();

        if (utenteLoggato != null) {
            System.out.println("\n✓ Login effettuato con successo!");
            System.out.println("Benvenuto/a " + utenteLoggato.getNome() + " " + utenteLoggato.getCognome());

            // Procedi con il resto dell'applicazione
            mostraMenuPrincipale(utenteLoggato);
        } else {
            System.out.println("\n✗ Arrivederci!");
        }

        scanner.close();
    }

    private static Utente mostraMenuIniziale() {
        while (true) {
            System.out.println("\n=== MENU INIZIALE ===");
            System.out.println("1. Accedi (Login)");
            System.out.println("2. Registrati");
            System.out.println("3. Esci");
            System.out.print("Scegli un'opzione: ");

            String scelta = scanner.nextLine().trim();

            switch (scelta) {
                case "1":
                    Utente utente = effettuaLogin();
                    if (utente != null) {
                        return utente;
                    }
                    // Se login fallisce, torna al menu iniziale
                    break;
                case "2":
                    effettuaRegistrazione();
                    break;
                case "3":
                    return null;
                default:
                    System.out.println("⚠ Opzione non valida!");
            }
        }
    }

    private static void effettuaRegistrazione() {
        System.out.println("\n--- REGISTRAZIONE ---");

        try {
            // Input dati registrazione
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("⚠ Username non può essere vuoto!");
                return;
            }

            System.out.print("Nome: ");
            String nome = scanner.nextLine().trim();

            if (nome.isEmpty()) {
                System.out.println("⚠ Nome non può essere vuoto!");
                return;
            }

            System.out.print("Cognome: ");
            String cognome = scanner.nextLine().trim();

            if (cognome.isEmpty()) {
                System.out.println("⚠ Cognome non può essere vuoto!");
                return;
            }

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            if (email.isEmpty() || !email.contains("@")) {
                System.out.println("⚠ Email non valida!");
                return;
            }

            // Password nascosta
            String password = leggiPasswordNascosta("Password: ");

            if (password.isEmpty()) {
                System.out.println("⚠ Password non può essere vuota!");
                return;
            }

            // Conferma password
            String confermaPassword = leggiPasswordNascosta("Conferma password: ");

            if (!password.equals(confermaPassword)) {
                System.out.println("⚠ Le password non coincidono!");
                return;
            }

            System.out.print("Immagine profilo (opzionale, premi INVIO per saltare): ");
            String immagineProfilo = scanner.nextLine().trim();

            // Chiama il metodo di registrazione
            registraProprietario(username, nome, cognome, email, password, immagineProfilo);

        } catch (Exception e) {
            System.out.println("✗ Errore durante la registrazione: " + e.getMessage());
        }
    }

    public static void registraProprietario(String username, String nome, String cognome, String email, String password, String immagineProfilo) {
        UtenteDAO utenteDao = new UtenteDAO();
        try {
            Utente nuovoUtente = utenteDao.registrazione(username, nome, cognome, email, password, immagineProfilo);
            System.out.println("✓ Registrazione avvenuta con successo per " + nuovoUtente.getNome() + "!");
        } catch (Exception e) {
            System.out.println("✗ Errore durante la registrazione: " + e.getMessage());
        }
    }

    private static Utente effettuaLogin() {
        UtenteDAO utenteDao = new UtenteDAO();
        int tentativi = 0;
        final int MAX_TENTATIVI = 3;

        while (tentativi < MAX_TENTATIVI) {
            System.out.println("\n--- LOGIN ---");

            // Input username
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("⚠ Username non può essere vuoto!");
                continue;
            }

            // Input password (nascosta)
            String password = leggiPasswordNascosta();

            if (password.isEmpty()) {
                System.out.println("⚠ Password non può essere vuota!");
                continue;
            }

            try {
                Utente utente = utenteDao.login(username, password);
                if (utente != null) {
                    return utente;
                } else {
                    tentativi++;
                    System.out.println("✗ Credenziali errate! Tentativo " + tentativi + "/" + MAX_TENTATIVI);

                    if (tentativi < MAX_TENTATIVI) {
                        System.out.println("Riprova...");
                    }
                }
            } catch (Exception e) {
                System.out.println("✗ Errore durante il login: " + e.getMessage());
                tentativi++;
            }
        }

        System.out.println("\n⚠ Troppi tentativi falliti. Accesso negato.");
        return null;
    }

    private static String leggiPasswordNascosta() {
        return leggiPasswordNascosta("Password: ");
    }

    private static String leggiPasswordNascosta(String prompt) {
        Console console = System.console();

        if (console != null) {
            // Se eseguito da terminale, la password sarà nascosta
            char[] passwordArray = console.readPassword(prompt);
            return new String(passwordArray);
        } else {
            // Se eseguito da IDE, mostra un avviso
            System.out.print(prompt);
            return scanner.nextLine().trim();
        }
    }

    private static void mostraMenuPrincipale(Utente utente) {
        AnimaleDAO dao = new AnimaleDAO();

        while (true) {
            System.out.println("\n=== MENU PRINCIPALE ===");
            System.out.println("1. Visualizza animali");
            System.out.println("2. Aggiungi nuovo animale");
            System.out.println("3. Logout");
            System.out.print("Scegli un'opzione: ");

            String scelta = scanner.nextLine().trim();

            switch (scelta) {
                case "1":
                    visualizzaAnimali(dao, utente);
                    break;
                case "2":
                    aggiungiAnimale(dao, utente);
                    break;
                case "3":
                    System.out.println("\nLogout effettuato. Arrivederci " + utente.getNome() + "!");
                    return;
                default:
                    System.out.println("⚠ Opzione non valida!");
            }
        }
    }

    private static void aggiungiAnimale(AnimaleDAO dao, Utente utente) {
        System.out.println("\n--- AGGIUNGI NUOVO ANIMALE ---");

        try {
            // Input numero chip
            System.out.print("Numero chip: ");
            String chipStr = scanner.nextLine().trim();

            if (chipStr.isEmpty()) {
                System.out.println("⚠ Il numero chip non può essere vuoto!");
                return;
            }

            int chip;
            try {
                chip = Integer.parseInt(chipStr);
            } catch (NumberFormatException e) {
                System.out.println("⚠ Il numero chip deve essere un numero intero!");
                return;
            }

            // Input nome animale
            System.out.print("Nome dell'animale: ");
            String nome = scanner.nextLine().trim();

            if (nome.isEmpty()) {
                System.out.println("⚠ Il nome non può essere vuoto!");
                return;
            }

            // Input razza
            System.out.print("Razza: ");
            String razza = scanner.nextLine().trim();

            if (razza.isEmpty()) {
                System.out.println("⚠ La razza non può essere vuota!");
                return;
            }

            // Input colore
            System.out.print("Colore: ");
            String colore = scanner.nextLine().trim();

            if (colore.isEmpty()) {
                System.out.println("⚠ Il colore non può essere vuoto!");
                return;
            }

            // Input data di nascita
            System.out.print("Data di nascita (YYYY-MM-DD): ");
            String dataNascita = scanner.nextLine().trim();

            if (dataNascita.isEmpty()) {
                System.out.println("⚠ La data di nascita non può essere vuota!");
                return;
            }

            // Validazione formato data (base)
            if (!dataNascita.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("⚠ Formato data non valido! Usa il formato YYYY-MM-DD");
                return;
            }

            // Crea nuovo animale con il costruttore corretto
            Animale nuovoAnimale = new Animale(chip, nome, razza, colore, dataNascita, utente.getUsername());

            // Inserisci nel database
            dao.inserisciAnimale(nuovoAnimale);

            System.out.println("✓ Animale " + nome + " (chip: " + chip + ") aggiunto con successo!");

        } catch (Exception e) {
            System.out.println("✗ Errore durante l'inserimento dell'animale: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nPremi INVIO per continuare...");
        scanner.nextLine();
    }

    private static void visualizzaAnimali(AnimaleDAO dao, Utente utente) {
        try {
            System.out.println("\n--- LISTA ANIMALI ---");

            // Puoi personalizzare questo filtro in base al tipo di utente
            String filtroUtente = utente.getNome() + utente.getCognome(); // null = tutti gli animali

            List<Animale> animali = dao.visualizzaAnimali(filtroUtente);

            if (animali.isEmpty()) {
                System.out.println("Nessun animale trovato.");
            } else {
                System.out.println("Trovati " + animali.size() + " animali:");
                System.out.println("-----------------------------------");

                for (Animale a : animali) {
                    System.out.printf("%-15s | %-12s | %-10s | %s%n",
                            a.getNome(),
                            a.getRazza(),
                            a.getColore(),
                            a.getDataNascita()
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Errore durante il caricamento degli animali: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nPremi INVIO per continuare...");
        scanner.nextLine();
    }
}