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

        int[] gameToMove = pickRandomGame(); // Valitaan peli
        // Tarvitaan kopio sarjaohjelmasta
        try {
            tempSeries = (Series)series.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        // Testattavassa listassa pidetään myös se kierros jolta valittu peli tuli, uskon että on helpompaa
        // vain olla huomioimatta yhtä kierrosta kuin poistaa se ja valinnan jälkeen saada oikea indexi.
        populateRoundsWithTestedGame(gameToMove[0], gameToMove[1]);

        int destination = pickDestinationRound(gameToMove[0]);

        tempSeries.calculateErrors();
        //tempSeries.printSeries();
        series.printErrors();
        tempSeries.printErrors();
        System.out.println(series.series.get(gameToMove[0]).get(gameToMove[1]));
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

        int pickedRound;
        if(picked.size() > 1) {
            pickedRound = picked.get(rnd.nextInt(picked.size()));
        } else {
            pickedRound = picked.get(0);
        }
        System.out.println("VALITTU KIERROS ON: " + pickedRound);



        System.out.println("SUMMIEN EROTUKSET");
        for (int i :erotukset) {
            System.out.println(i);
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