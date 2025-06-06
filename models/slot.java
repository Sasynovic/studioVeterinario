package models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class slot {
    private Date data;
    private int ora;
    private boolean occupato;

    public slot(Date data, int ora) {
        this.data = data;
        this.ora = ora;
        this.occupato = false; // di default libero
    }

    public boolean isOccupato() {
        return occupato;
    }

    public void setOccupato(boolean occupato) {
        this.occupato = occupato;
    }

    public String getChiave() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(data) + "-" + ora; // es: 2025-06-04-10
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return "Slot: " + sdf.format(data) + " alle ore " + ora + ":00 - " + (occupato ? "OCCUPATO" : "LIBERO");
    }
}
