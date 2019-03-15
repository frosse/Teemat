package SeriesSolver;

import java.util.ArrayList;
import java.util.Random;

public class Solver {
    private Series series;
    private Series tempSeries;
    private int debugInterval;

    private Random rnd = new Random();
    private SA sa = new SA(100, 10);
    private Tabulist tl = new Tabulist(5);


    public Solver(Series series, int debugInterval) {
        this.series = series;
        this.debugInterval = debugInterval;
    }

    public void solve() {
        // TODO lasketaan virheet ensin
        int counter = 0;
        int destination;
        int sumOfErrors = -1;
        int[] gameToMove;

        while( sumOfErrors != 0 ) {
            counter++;
            sa.calcNewProb();

            if( counter % debugInterval == 0 ) {
                String output = "";
                int[][] tempErrors =series.getTeamsErrors();
                    for ( int[] error : tempErrors ) {
                        output += error[error.length - 1]+ " ";
                    }
                System.out.println("Total errors: " + sumOfErrors + " Errors by round: "+ output+ " SA: "+Math.round( sa.acceptenceProb()*100)/100.0d);
            }

            do {
                gameToMove = pickRandomGame(); // Valitaan peli
                // Tarvitaan kopio sarjaohjelmasta
                copySeriesToTemp();
                // Testattavassa listassa pidetään myös se kierros jolta valittu peli tuli, uskon että on helpompaa
                // vain olla huomioimatta yhtä kierrosta kuin poistaa se ja valinnan jälkeen lisätä se oikeaan kohtaan
                populateRoundsWithTestedGame( gameToMove[0], gameToMove[1] );

                // Valitaan kierros johon valittu peli siirretään
                destination = pickDestinationRound( gameToMove[0] );

                // Tehdään niin kauan kunnes löytyy peli ja kierros joka ei ole tabulistassa.
            } while ( tl.isinList( series.getGameFromSeries( gameToMove[0], gameToMove[1] ) , destination ) );

            // TÄMÄ ON TESTI
            // Kopioidaan vanha lista temppiin, koska halutaan verrata koko listan erroreita vanhan listan erroreita
            // Ja tässä vaiheessa ollaan jo haettu paras kierros.
            copySeriesToTemp();

            /* Siirretään valittu peli väliaikasessa sarjaohjelmassa uuteen paikkaan, jotta
            voidaan laskea ja verrata sarjaohjelmien kokonaisvirheitä.
            */
            moveGameToNewRound( tempSeries, destination, gameToMove, false );

            //Lasketaan virheet molemmista vanhasta sekä tempistä
            if ( isGameMoving() ) {
                moveGameToNewRound( series, destination, gameToMove, true );
            }

            /* Toistetaan tätä osuuttaa joku määrä. Ohjeissa 5-80..
                Siirretään kierrokselta jonne äsken siirrettiin peli niin pois peli
                joku toinen kuin se mikä äsken sinne siirrettiin
            */
            for ( int j = 0; j < 5; j++ ) {

                // Jos kierroksella on alle kaksi(2) peliä niin silloin break ja sirrytään alkuun
                if ( series.series.get( destination ).size() > 2 ) {

                    //Valitaan peli jota siirretään
                    gameToMove = pickRandomGameFromSpecificRound( destination );
                    if(gameToMove[0] == -1 && gameToMove[1] == -1) {
                        break;
                    }

                    //Kopioidaan väliaikaiseen sarjaohjelmaan alkuperäinen
                    copySeriesToTemp();

                    //Lisätään väliaikaiseen sarjaohjelmaan valittu peli jokaiselle kierrokselle.
                    populateRoundsWithTestedGame( gameToMove[0], gameToMove[1] );

                    // Valitaan kierros johon valittu peli siirretään
                    destination = pickDestinationRound( gameToMove[0] );

                    // Jos valittu peli ja kierros ovat jo tabulistassa, niin silloin break ja aloitetaan alusta.
                    if( tl.isinList( series.series.get( gameToMove[0] ).get( gameToMove[1] ), destination ) ) {
                        break;
                    }

                    /* Kopioidaan väliaikaiseen sarjaohjelmaan alkuperäinen, koska tällä hetkellä siellä
                    on sama peli joka kierroksella.
                     */

                    copySeriesToTemp();

                     /* Siirretään valittu peli väliaikasessa sarjaohjelmassa uuteen paikkaan, jotta
                    voidaan laskea ja verrata sarjaohjelmien kokonaisvirheitä.
                    */
                    moveGameToNewRound( tempSeries, destination, gameToMove, false );

                    //Lasketaan virheet molemmista vanhasta sekä tempistä
                    // Jos kokonaisvirheet huononevat eikä SA auta niin break
                    if ( isGameMoving() ) {
                        moveGameToNewRound( series, destination, gameToMove, true );
                    }

                } else {
                    break;
                }
            }
            sumOfErrors = getSumOfTotalErrors();
        }

        series.output(sumOfErrors);
        series.printSeries();
    }

    private void copySeriesToTemp() {
        try {
            tempSeries = (Series) series.clone();
        } catch ( CloneNotSupportedException e ) {
            e.printStackTrace();
        }
    }

    private int getSumOfTotalErrors() {

        int sumOfErrors = 0;
        series.calculateErrors();

        int[][] errors = series.getTeamsErrors();

        for ( int[] error : errors ) {
            sumOfErrors += error[error.length - 1];
        }
        return sumOfErrors;
    }

    private boolean isGameMoving() {
        series.calculateErrors();
        tempSeries.calculateErrors();
        int[][] errors = series.getTeamsErrors();
        int[][] tempSeriesErrors = tempSeries.getTeamsErrors();
        int index = errors[0].length-1;
        int seriesSum = 0;
        int tempSum = 0;

        // Laske erotukset summa soluista
        for ( int i = 0; i < errors.length; i++ ) { // i = 0-5
            //int sum1 = errors[i][errors[i].length];
            seriesSum += errors[i][index];
            tempSum += tempSeriesErrors[i][index];
        }

        return seriesSum >= tempSum || sa.accept();
    }

    private int[] pickRandomGameFromSpecificRound( int destination ) {
        int[] gameIndex = new int[2];
        int counter = 0;
        gameIndex[0] = destination;
        gameIndex[1] = rnd.nextInt( series.series.get( destination ).size() - 1);
        Game picked = series.getGameFromSeries( destination, gameIndex[1] );

        while( !picked.isMovable ) {
            if(counter == series.series.get(destination).size()) {
                return new int[] {-1, -1};
            }
            gameIndex[1] = rnd.nextInt( series.series.get( destination ).size() - 1);
            picked = series.getGameFromSeries( destination, gameIndex[1] );
            counter++;
        }

        return gameIndex;
    }

    private void moveGameToNewRound( Series series, int destination, int[] gameToMove, boolean isAddedToTabulist ) {

        if( isAddedToTabulist ) {
            tl.add( series.series.get( gameToMove[0] ).get( gameToMove[1] ), gameToMove[0] );
        }

        Game temp = series.series.get( gameToMove[0] ).get( gameToMove[1] );
        series.series.get( gameToMove[0] ).remove( gameToMove[1] );
        series.series.get( destination ).add( temp );
    }

    private int pickDestinationRound( int ignoredRound ) {

        //Lista koska pelejä voidaan valita enemmän kuin yksi
        ArrayList<Integer> picked;

        //Lasketaan virheet molemmille sarjaohjelmille
        series.calculateErrors();
        tempSeries.calculateErrors();

        // Lasketaan alkuperäisen sarjaohjelman ja väliaikaisen sarjaohjelman kierroskohtaisten rankkarien summien erotus
        int[] difference = calculateErrorDifference( ignoredRound );

        // Valitaan paras paikka sijoittaa peli.
        picked = pickBestPlaceToPutGame( ignoredRound, difference );

        // Jos "parhaita" kierroksia enemmän kuin yksi niin valitaan randomilla
        if( picked.size() > 1 ) {
            return picked.get( rnd.nextInt( picked.size() ) );
        } else {
            return picked.get(0);
        }
    }

    private int[] calculateErrorDifference( int ignoredRound ) {
        /* Haetaan listojen joukkuekohtaiset rankkarit väliaikaisiin muuttujiin.
        Näistä lasketaan sarjaohjelmien kierroskohtaiset rankkarien erotukset, jotta tiedetään
        mille kierrokselle/kierroksille on paras sijoittaa peli */
        int[][] errors = series.getTeamsErrors();
        int[][] tempErrors = tempSeries.getTeamsErrors();

        // Tähän lasketaan sarjaohjelmien kierroskohtaisten rankkarien erotukset
        int[] difference = new int[errors.length];

        /* Aiemmin haetuissa rankkarimatriiseissa, viimeinen sarake on kierroskohtainen summa
        tarvitaan pelkästään sitä ja tällä päästään siihen käsiksi */
        int index = errors[0].length-1;

        /* Laskee erotukset joukkuekohtaisten rankkarien summa soluista
        Jos kierros on se miltä peli siirretään eli ignoredRound, niin skipataan se
        Tällä hetkellä siihen asetetaan 1000... TODO: tämä saattaa vaihtua. */
        for ( int i = 0; i < errors.length; i++ ) {
            if( i != ignoredRound ) {
                difference[i] = ( tempErrors[i][index] - errors[i][index] );
            } else {
                difference[i] = 1000;
            }
        }
        return difference;
    }

    private ArrayList<Integer> pickBestPlaceToPutGame( int ignoredRound, int[] dif ) {
        ArrayList<Integer> picked = new ArrayList<>();
    /*
    Käydään äsken laskettu lista läpi ja aina kun löytyy pienempi erotus
    niin tyhjennetään lista ja lisätään kyseinen indexi listaan. Jos erotukset ovat samat niin lisätään indexi listaan.
    Jos kierros on se miltä peli siirretään eli ignoredRound, niin skipataan se.
    Jos lista on tyhjä niin lisätään aina vuorossa oleva index ( Vain ensimmäisen kohdalla totta)
    indexit otetaan talteen koska niitä tarvitaan seuraavassa osuudessa
    */
        for ( int i = 0; i < dif.length; i++ ) {
            if( i != ignoredRound ) {
                if( picked.size() == 0 ) {
                    picked.add(i);
                } else {
                    if( dif[i] < dif[picked.get(0)] ) {
                        picked.clear();
                        picked.add(i);
                    }else if ( dif[i] == dif[picked.get(0)] ) {
                        picked.add(i);
                    }
                }
            }
        }
        return picked;
    }

    // Lisätään peli kaikille muille kierroksille paitsi sille jossa se jo on
    private void populateRoundsWithTestedGame( int round, int game ) {
        for ( int i = 0; i < tempSeries.series.size(); i++ ) {
            if( i != round ) {
                tempSeries.series.get( i ).add( tempSeries.series.get( round ).get( game ) );
            }
        }
    }

    private int[] pickRandomGame() {
        int round = rnd.nextInt(series.series.size());
        int game = -1;
        int counter = 0;
        boolean flag = false;
        while (true) {
            counter = 0;
            while (series.series.get(round).size() == 0 || flag) { // Tyhjältä kierrokselta ei voida ottaa peliä, joten arvotaan toinen kierros
                round = rnd.nextInt(series.series.size());
                flag = false;
            }

            while (series.series.get(round).size() >= counter) {
                game = rnd.nextInt(series.series.get(round).size());
                Game picked = series.getGameFromSeries(round, game);
                if (picked.isMovable()) {
                    return new int[]{round, game};
                }
                counter++;
            }
            flag = true;
        }
    }
}