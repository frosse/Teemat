package SeriesSolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;


public class HomeConstraint implements IConstraint, Cloneable{

    private int[][] teamErrors;
    private ArrayList<ArrayList<Integer>> gameErrors;
    private HashMap<Integer, ArrayList<Integer>> cantPlay;
    private int rounds;
    private int teams;

    @Override
    public void calculateErrors(Series series) {
        calculateTeamErrors(series);
    }

    private void calculateTeamErrors(Series series) {
        teamErrors  = new int[rounds][teams+1];
        for ( int i = 0; i < series.series.size(); i++ ) {
            for ( int j = 0; j < series.series.get (i ).size(); j++ ) {
                if( cantPlay.containsKey(i+1)) {
                    for ( int s :cantPlay.get( i+1 ) ) {
                        if( s == series.series.get( i ).get( j ).getHome() ) {
                            teamErrors[j][s-1]++;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < teamErrors.length; i++) {
            int sum = 0;
            for (int j = 0; j < teamErrors[i].length-1; j++) {
                sum += teamErrors[i][j];
            }
            teamErrors[i][teamErrors[i].length-1] = sum;
        }
    }

    @Override
    public void printErrors() {
        System.out.println("Home constraint");
        System.out.println("Errors per team: ");
        for (int i = 0; i < teamErrors.length; i++) {
            for (int j = 0; j < teamErrors[i].length; j++) {
                System.out.print(teamErrors[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void initializeConstraint(int rounds, int teams) {
        this.rounds = rounds;
        this.teams = teams;
        readInfoFromFile();

        this.teamErrors = new int[rounds][teams+1]; // TODO teams+1 is not correct
        gameErrors = new ArrayList<>();
        for (int i = 0; i < rounds; i++) {
            gameErrors.add(new ArrayList<>());
        }
    }

    private void readInfoFromFile() {
        cantPlay = new HashMap<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("homeConstraint.txt"));
            String line = reader.readLine();
            int team;
            int round;
            while ( line != null ) {
                team = Integer.parseInt(line.split(" ")[0]);
                round = Integer.parseInt(line.split(" ")[1]);
                if(!cantPlay.containsKey(round)) {
                    cantPlay.put(round, new ArrayList<>());
                }

                cantPlay.get(round).add(team);
                line = reader.readLine();

            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getTotalErrorSum() {
        int sumOfErrors = 0;
        for ( int[] error : teamErrors ) {
            sumOfErrors += error[error.length - 1];
        }
        return sumOfErrors;
    }

    @Override
    public int[][] getErrors() {
        return teamErrors;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        HomeConstraint clone = (HomeConstraint)super.clone();
        int[][] newArray = new int[teamErrors.length][teamErrors[0].length];
        for (int i = 0; i < teamErrors.length; i++) {
            newArray[i] = teamErrors[i].clone();
        }
        clone.teamErrors = newArray;
        clone.cantPlay = (HashMap<Integer, ArrayList<Integer>>)this.cantPlay.clone();
        return clone;
    }
}
