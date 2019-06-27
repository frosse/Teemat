package SeriesSolver;

import java.util.ArrayList;

/*
    Ohjelma tulostaa aluksi randomilla tehdyn sarjaohjelman johon on lisätty ylimääräiset pelit sekä pakotetut pelit on oikeilla paikoilla.
    Tulostaa tasaisin väliajoin kaikkien rankkarien summan | Rankkarit kierroksittain | Rankkarit rajoituksittain ( 0-1, kotiesto, vierasesto) | sekä SA:n arvon
    Kun ohjelma löytää ratkaisun, tulostaa konsoliin sarjaohjelman sekä tuottaa tekstitiedoston "output_teema3.txt".
    Tiedostossa ekalla rivillä on kaikki virheet, hard virheet HUOM! Tässä ratkaisussa ei ole käytössä soft virheitä lainkaan,
    en nähnyt niitä tarpeellisiksi, koska ratkaisu saadaan vaikka vierasrajoitteen rankkareita käsitellään hardeina.
    Toisella rivillä rankkari rajoitteittain ( 0-1, kotiesto, vierasesto)
    Tämän jälkeen joka rivillä: kierros, koti, vieras, onko peli pakotettu
 */
public class Main {

    public static void main(String[] args) {

        ArrayList<IConstraint> rajoitukset = new ArrayList<>();
        // Lisätään rajoitukset listaan, tämä annetaan sarjaohjelmalle parametrina
        // Rajoitusten lisäämis järjestys vaikuttaa ratkaisun tulokstukseen
        // nyt tulostaa toiselle rivillä -> pelatut virheet, kotiesto_virheet, vierasesto_virheet
        rajoitukset.add(new ZeroToOneConstraint());
        rajoitukset.add(new HomeConstraint());
        rajoitukset.add(new AwayConstraint());
        //rajoitukset.add(new BreaksConstraint());

        Series sr = new Series(rajoitukset);
        sr.printSeries();
        Solver solver = new Solver(sr,750);

        solver.solve();

    }
}
