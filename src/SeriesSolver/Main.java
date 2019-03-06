package SeriesSolver;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	// write your code here
        ArrayList<IConstraint> rajoitukset = new ArrayList<>();
        rajoitukset.add(new ZeroToOneConstraint());
        Series sr = new Series(12,2, rajoitukset);
        sr.printSeries();
        Solver solver = new Solver(sr);
//        solver.constraints.get(0).calculateErrors(solver.series);
//        solver.constraints.get(0).printErrors();
        solver.solve();

    }
}
