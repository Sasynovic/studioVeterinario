import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Mappa con chiave composta da data+ora
        Map<String, slot> slotMap = new HashMap<>();

        // Creazione di uno slot
        Date data = sdf.parse("2025-06-04");
        slot slot1 = new slot(data, 10); // ore 10

        // Inserimento nella mappa
        slotMap.put(slot1.getChiave(), slot1);

        // Controllo se lo slot è occupato
        String chiave = "2025-06-04-10";
        if (slotMap.containsKey(chiave)) {
            slot s = slotMap.get(chiave);
            System.out.println(s.toString());

            // Modifica stato
            s.setOccupato(true);
            System.out.println("Dopo modifica: " + s);
        } else {
            System.out.println("Slot non trovato.");
        }
    }
}
