package SeriesSolver;

import java.util.ArrayList;

public class BreaksConstraint implements Cloneable, IConstraint {

    private int[][] teamErrors;
    private ArrayList<ArrayList<Integer>> gameErrors;
    private Breaks[] breaksArray;
    private int teams;
    private int rounds;

    @Override
    public void calculateErrors(Series series) {
        for (int i = 0; i < series.series.size(); i++) {
            for (int j = 0; j < series.series.get(i).size(); j++) {
                breaksArray[series.series.get(i).get(j).getHome()-1].games[i] = true;
                breaksArray[series.series.get(i).get(j).getAway()-1].games[i] = false;
            }
        }
        for (Breaks b :breaksArray) {
            b.calculateBreaks();
            b.printBreaks();
        }

    }

    @Override
    public void printErrors() {

    }

    @Override
    public void initializeConstraint(int rounds, int teams) {
        this.rounds = rounds;
        this.teams = teams;
        breaksArray = new Breaks[teams];

        for (int i = 0; i < breaksArray.length; i++) {
            breaksArray[i] = new Breaks(rounds);
        }
    }

    @Override
    public int[][] getErrors() {
        return new int[0][];
    }

    @Override
    public int getTotalErrorSum() {
        return 0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }
}
