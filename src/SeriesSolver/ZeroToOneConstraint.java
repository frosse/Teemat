package SeriesSolver;


import java.util.ArrayList;
// käydään kaikki joukkueet läpi, jos joukkue on kierroksella useammin kuin kerran niin +1
//
public class ZeroToOneConstraint implements IConstraint, Cloneable{
    private int[][] teamErrors;
    private ArrayList<ArrayList<Integer>> gameErrors;

    public void initializeConstraint(int rounds, int teams) {
        // +1 koska viimeinen solu on kierroksen summa
        this.teamErrors = new int[rounds][teams+1]; // TODO teams+1 is not correct
        gameErrors = new ArrayList<>();
        for (int i = 0; i < rounds; i++) {
            gameErrors.add(new ArrayList<>());
        }
    }

    @Override
    public int[][] getErrors() {
        return teamErrors;
    }

    @Override
    public void calculateErrors(Series series) {
        calculateTeamErrors(series);
        calculateGameErrors(series);
    }

    private void calculateGameErrors(Series series) {
        for (int i = 0; i < series.series.size(); i++) {
            gameErrors.remove(i);
            gameErrors.add(i, new ArrayList<>());
            for (int j = 0; j < series.series.get(i).size(); j++) {
               gameErrors.get(i).add(j,teamErrors[i][series.series.get(i).get(j).home-1] + teamErrors[i][series.series.get(i).get(j).away-1]);
            }
        }
    }

    private void calculateTeamErrors(Series series) {
        int counter;
        //Calculates every team errors
        for(int i = 1; i < series.getTeams()+1; i++) { // Starts at 1 cause teams also starts at 1
            for(int j = 0; j < series.series.size(); j++) { //Rounds
                counter = 0;
                teamErrors[j][i-1] = 0;  // Resets earlier errors
                for ( int k = 0; k < series.series.get(j).size(); k++) {

                    if(i == series.series.get(j).get(k).home || i == series.series.get(j).get(k).away) {
                        if(counter > 0 )teamErrors[j][i-1]++;
                        counter++;
                    }
                }
            }
        }
        // Calculates sum of errors per round and adds it to last index
        for (int i = 0; i < teamErrors.length; i++) {
            int sum = 0;
            for (int j = 0; j < teamErrors[i].length-1; j++) {
                sum += teamErrors[i][j];
            }
            teamErrors[i][teamErrors[i].length-1] = sum;
        }
    }

    public void printErrors() {
        System.out.println("Errors per team: ");
        for (int i = 0; i < teamErrors.length; i++) {
            for (int j = 0; j < teamErrors[i].length; j++) {
                System.out.print(teamErrors[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Errros per game: ");
        for (int i = 0; i < gameErrors.size(); i++) {
            for (int j = 0; j < gameErrors.get(i).size(); j++) {
                System.out.print(gameErrors.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
    public Object clone() throws CloneNotSupportedException {
        ZeroToOneConstraint clone = (ZeroToOneConstraint)super.clone();
        int[][] newArray = new int[teamErrors.length][teamErrors[0].length];
        for (int i = 0; i < teamErrors.length; i++) {
            newArray[i] = teamErrors[i].clone();
        }
        clone.teamErrors = newArray;
        return clone;
    }
}
