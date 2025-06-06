import java.util.List;
import models.*;
import dao.*;

public class Main {
    public static void main(String[] args) throws Exception {

        AnimaleDAO dao = new AnimaleDAO();

        try {
            // Recupero
            List<Animale> animali = dao.readAnimale(null); // Passa null per ottenere tutti gli animali
            for (Animale a : animali) {
                System.out.println(a.getNome() + " " + a.getRazza() + " " + a.getColore() + " " + a.getDataNascita());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
