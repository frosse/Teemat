package SeriesSolver;

import java.util.ArrayList;
import java.util.Random;

public class Solver {
     Series series;
     Series tempSeries;
     Random rnd = new Random();

    public Solver(Series series) {
        this.series = series;

    }
    public void solve() {
        // TODO lasketaan virheet ensin
        series.calculateErrors();
        int[][] errorit = series.constraints.get(0).getErrors();
        int sumOfErrors = 100;

        while(sumOfErrors != 0) {
            series.printSeries();
            sumOfErrors = 0;
            series.calculateErrors();
            for (int i = 0; i < errorit.length; i++) { // i = 0-5
                //int sum1 = errors[i][errors[i].length];
                sumOfErrors += errorit[i][4];
            }
            int[] gameToMove = pickRandomGame(); // Valitaan peli
            // Tarvitaan kopio sarjaohjelmasta
            try {
                tempSeries = (Series) series.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            // Testattavassa listassa pidetään myös se kierros jolta valittu peli tuli, uskon että on helpompaa
            // vain olla huomioimatta yhtä kierrosta kuin poistaa se ja valinnan jälkeen saada oikea indexi.
            populateRoundsWithTestedGame(gameToMove[0], gameToMove[1]);

            tempSeries.calculateErrors();
            int destination = pickDestinationRound(gameToMove[0]);
            moveGameToNewRound(destination, gameToMove);
            //series.printSeries();
            series.calculateErrors();
            for (int j = 0; j < 5; j++) {
                //tempSeries.printSeries();
                //series.printErrors();
                //tempSeries.printErrors();
                if (series.series.get(destination).size() > 2) {
                    gameToMove = pickRandomGameFromSpecificRound(destination);
                    try {
                        tempSeries = (Series) series.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    populateRoundsWithTestedGame(gameToMove[0], gameToMove[1]);
                    destination = pickDestinationRound(gameToMove[0]);
                    moveGameToNewRound(destination, gameToMove);
                    //series.printSeries();

                } else {
                    break;
                }
            }
        }
    }

    private int[] pickRandomGameFromSpecificRound(int destination) {
        int[] gameIndex = new int[2];
        gameIndex[0] = destination;
        gameIndex[1] = rnd.nextInt(series.series.get(destination).size()-1); // -1 koska tiedetään, että listaan on lisätty juuri peli ja sitä ei saa ottaa

        return gameIndex;
    }

    private void moveGameToNewRound(int destination, int[] gameToMove) {
        Game temp = series.series.get(gameToMove[0]).get(gameToMove[1]);
        series.series.get(gameToMove[0]).remove(gameToMove[1]);
        series.series.get(destination).add(temp);
    }

    private int pickDestinationRound(int ignoredRound) {

        ArrayList<Integer> picked = new ArrayList<>();
        tempSeries.calculateErrors();  // Lasketaan virheet uudelle listalle
        // TODO: Tämä on vain teema 2:sta varten, jossa meillä on vain yksi rajoitus
        // Ei tule toimimaan teema 3:ssa, mutta nyt kova koodattu
        int[][] errors = series.constraints.get(0).getErrors(); //Haetaan errorit
        int[][] tempErrors = tempSeries.constraints.get(0).getErrors();
        int[] erotukset = new int[errors.length];
        int index = errors[0].length-1;

        // Laske erotukset summa soluista
        for (int i = 0; i < errors.length; i++) { // i = 0-5
            //int sum1 = errors[i][errors[i].length];
            if(i != ignoredRound) {
                int tulos = (tempErrors[i][index] - errors[i][index] );
                erotukset[i] = tulos;
            } else {
                erotukset[i] = 1000; //TODO vaihda tämä kun debuggaus on ohi, kuitenkin jatkossakin ignorataan.
            }
        }

        // Valitaan paras paikka sijoittaa peli
        for (int i = 0; i < erotukset.length; i++) {
            if( i != ignoredRound) {
                if(picked.size() == 0) {
                    picked.add(i);
                } else {
                    if( erotukset[i] < erotukset[picked.get(0)] ) {
                        picked.clear();
                        picked.add(i);
                    }else if (erotukset[i] == erotukset[picked.get(0)]) {
                        picked.add(i);
                    }
                }
            }
        }
        // Jos "parhaita" kierroksia enemmän kuin yksi niin valitaan randomilla
        // TODO: Tällä hetkellä vain valitaan "paras" kierros, todellisuudessa pitää katsoa huononeeko kokonais virheet
        // Ja jos huononee niin SA tulee kuvioihin
        int pickedRound;
        if(picked.size() > 1) {
            pickedRound = picked.get(rnd.nextInt(picked.size()));
        } else {
            pickedRound = picked.get(0);
        }

        return pickedRound;
    }

    // Lisätään peli kaikille muille kierroksille paitsi sille jossa se jo on
    private void populateRoundsWithTestedGame(int round, int game) {
        for (int i = 0; i < tempSeries.series.size(); i++) {
            if(i != round) {
                tempSeries.series.get(i).add(tempSeries.series.get(round).get(game));
            }
        }
    }

    private int[] pickRandomGame() {
        int round = rnd.nextInt(series.series.size());
        while(series.series.get(round).size() == 0) { // Tyhjältä kierrokselta ei voida ottaa peliä, joten arvotaan toinen kierros
            round = rnd.nextInt(series.series.size());
        }
        int game = rnd.nextInt(series.series.get(round).size());
        return new int[]{round, game};
    }
}