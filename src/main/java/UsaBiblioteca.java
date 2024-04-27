import java.util.Scanner;

public class UsaBiblioteca {

    public static void main(String[] args) {
        final String catalogoUrl = "/Users/mv/Desktop/FS0124IT-A/backend/u01/w02/d05/u04w02d05-progettosettimanale/catalogo.txt";

        Biblioteca biblioteca = new Biblioteca();

        biblioteca.caricaCatalogo(catalogoUrl);

        biblioteca.avvioApplicazione();

        biblioteca.salvaCatalogo(catalogoUrl);
    }
}
