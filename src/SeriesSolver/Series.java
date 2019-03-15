package SeriesSolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class Series implements Cloneable {
    private Random rnd = new Random();
    // Teema 3:ssa nämä ovat kova koodattuja
    private static final int teams = 15;
    private static final int amount = 2; //keskinäiset kohtaamiset
    private static final int rounds = 34;

    private ArrayList<Game> gameList;
    ArrayList<ArrayList<Game>> series;
    ArrayList<IConstraint> constraints;

    public Series(ArrayList<IConstraint> constraintList) {

        this.constraints = constraintList;
        setupConstraints();
        makeSeries(rounds);
        makeGameList();
        addGamesToSeries();
    }

    private void setupConstraints() {
        for (IConstraint constraint : constraints) {
            constraint.initializeConstraint(rounds,teams);
        }
    }

    public void calculateErrors() {
        for (IConstraint constraint : constraints) {
            constraint.calculateErrors(this);
        }
    }

    public void printErrors() {
        for (IConstraint constraint : constraints) {
            constraint.printErrors();
        }
    }

    public Game getGameFromSeries(int i, int j) {
        return series.get(i).get(j);
    }

    //Alustetaan otteluohjelma ja lisätään oikea määrä tyhjiä listoja eli kierroksia.
    private void makeSeries(int rounds) {
        series = new ArrayList<>();
        for(int i = 0; i < rounds; i++) {
            series.add(new ArrayList<>());
        }
    }
    //Lisätään pelilistalta kaikki pelit randomilla otteluohjelmaan.
    // Ainoa esto tässä, että kierroksia on rajoittettu määrä eli series.size()
    private void addGamesToSeries() {
        for (Game g : gameList) {
            series.get(rnd.nextInt(series.size())).add(g);
        }
        addExtraGamesToSeries();
        addPreAssignedGamesToSeries();
    }

    private void addPreAssignedGamesToSeries() {
        BufferedReader reader;
        try {
            // get file to reader
            reader = new BufferedReader(new FileReader("preassignedGames.txt"));
            String line = reader.readLine();
            int home;
            int away;
            int round;
            String[] temp;
            Game tempGame;
            boolean flag;
            while ( line != null ) {
                temp= line.split(" "); // tiedostossa joukkueet ja kierros on eroteltu välilyönnillä
                // Parsitaan jokaisesta integer
                home = Integer.parseInt(temp[0]);
                away = Integer.parseInt(temp[1]);
                round = Integer.parseInt(temp[2]);
                // Loopataan sarjaohjelma ja kun peli löytyy niin siirretään se oikella kierrokselle
                // ja tehdään siitä peli jota ei voi siirtää
                flag = false;
                for (ArrayList<Game> ser : series) {
                    for (int j = 0; j < ser.size(); j++) {
                        if (home == ser.get(j).home && away == ser.get(j).away) {
                            if (ser.get(j).isMovable) {
                            // kopioidaan peli
                            tempGame = ser.get(j);
                            //poistetaan alkuperäisestä paikasta
                            ser.remove(j);
                            // can't touch this
                            tempGame.setMovable(false);
                            // Heitetään oikealle kierrokselle.
                            series.get(round - 1).add(tempGame);
                            flag = true;
                            break;
                            }
                        }
                    }
                    if(flag) break;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addExtraGamesToSeries() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("extraGames.txt"));
            String line = reader.readLine();
            int home;
            int away;
            while ( line != null ) {
                home = Integer.parseInt(line.split(" ")[0]);
                away = Integer.parseInt(line.split(" ")[1]);
                System.out.println(home + " vs " + away);
                series.get(rnd.nextInt(series.size())).add(new Game(home, away));
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tehdään lista jossa kaikki pelit tarvittavat pelit ovat
    private void makeGameList() {
        gameList = new ArrayList<>();

        for (int h = 0; h < amount; h++) {
            for (int i = 1; i < teams+1; i++) {
                for (int j = i+1; j < teams+1; j++) {
                    gameList.add( h%2== 0 ?new Game(i, j) : new Game(j,i));
                }
            }
        }
        // TODO: Teema3:ssa täällä lisätään "ylimääräiset" pelit
    }

    public void printGameList() {
        for (Game g : gameList) {
                System.out.println(g.home + " - " + g.away);
        }
    }

    // Tulostaa sarjataulukon konsoliin.
    public void printSeries() {
        for (int i = 0; i < series.size(); i++) {
            System.out.printf("Round %d: ", i+1);
            for (int j = 0; j < series.get(i).size(); j++) {
                    System.out.print(series.get(i).get(j));
                if(j+1 != series.get(i).size()) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    public int getTeams() {
        return teams;
    }

    public int getRounds() {
        return rounds;
    }
    public Object clone() throws CloneNotSupportedException {
        Series clone = (Series)super.clone();
        clone.series = new ArrayList<>();
        try {
            for (int i = 0; i < series.size(); i++) {
                clone.series.add(new ArrayList<>());
                for (int j = 0; j < series.get(i).size(); j++) {
                    clone.series.get(i).add((Game)series.get(i).get(j).clone());
                }
            }
            //TODO Constraints have to clone also.
            clone.constraints = new ArrayList<>();
            for (IConstraint c : this.constraints) {
                clone.constraints.add((IConstraint)c.clone());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    public void output(int errors) {
        try {
            PrintWriter writer = new PrintWriter("output_teema2.txt", "UTF-8");
            writer.println(errors +" "+ errors);
            for (int i = 0; i < series.size(); i++) {
                for (int j = 0; j < series.get(i).size(); j++) {
                    writer.println((i + 1) + " " + series.get(i).get(j).printOutput() + " ei");
                }
            }
            writer.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public int[][] getTeamsErrors() {
        int[][] errors = new int[rounds][teams+1];
        int[][] temp;
        for (IConstraint c : constraints) {
            temp = c.getErrors();
            for (int i = 0; i < errors.length; i++) {
                for (int j = 0; j < errors[i].length; j++) {
                    errors[i][j] += temp[i][j];
                }
            }
        }
        return errors;
    }
}