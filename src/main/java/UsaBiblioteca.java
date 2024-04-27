import java.util.Scanner;

public class UsaBiblioteca {

    public static void main(String[] args) {
        final String catalogoUrl = "./catalogo.txt";

        Biblioteca biblioteca = new Biblioteca();

        biblioteca.caricaCatalogo(catalogoUrl);

        biblioteca.avvioApplicazione();

        biblioteca.salvaCatalogo(catalogoUrl);
    }
}
