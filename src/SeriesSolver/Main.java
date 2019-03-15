package SeriesSolver;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<IConstraint> rajoitukset = new ArrayList<>();
        // Lisätään rajoitukset listaan, tämä annetaan sarjaohjelmalle parametrina
        rajoitukset.add(new ZeroToOneConstraint());

        Series sr = new Series(rajoitukset);
        sr.printSeries();
        Solver solver = new Solver(sr,500);

        solver.solve();

    }
}
