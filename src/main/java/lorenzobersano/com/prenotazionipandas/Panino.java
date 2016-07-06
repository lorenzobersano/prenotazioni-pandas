package lorenzobersano.com.prenotazionipandas;

/**
 * Created by Lorenzo Bersano on 09/04/2016.
 */
public class Panino {
    String nome;
    String dimensione;
    String tipo;
    String caldoFreddo;
    String ingredienti;
    String salse;

    Panino(String nome, String dimensione, String tipo, String caldoFreddo, String ingredienti, String salse) {
        this(nome, dimensione, tipo, caldoFreddo, ingredienti);
        this.salse = salse;
    }

    Panino(String nome, String dimensione, String tipo, String caldoFreddo, String ingredienti) {
        this.nome = nome;
        this.dimensione = dimensione;
        this.tipo = tipo;
        this.caldoFreddo = caldoFreddo;
        this.ingredienti = ingredienti;
    }
}
