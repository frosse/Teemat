package test;

import java.util.Random;

public class Games {

    //in games-array true = kotipeli, false = vieraspeli
    //in home- and away-arrays true is if there is break, false...
    // There's is two constructors to use. One which uses random boolean array and second which uses hardcoded...


    Boolean[] games;
    Boolean[] home;
    Boolean[] away;

    public Games(int length) {
        games = new Boolean[length];
        home = new Boolean[length];
        away = new Boolean[length];
        Random r = new Random();
        // Generate random array
        for (int i = 0; i < games.length; i++) {
            games[i] = r.nextBoolean();
        }
        for (int i = 0; i < home.length; i++) {
            home[i] = false;
        }
        for (int i = 0; i < away.length; i++) {
            away[i] = false;
        }
    }

    public Games(Boolean[] preset) {
        games = preset;
        home = new Boolean[games.length];
        away = new Boolean[games.length];

        for (int i = 0; i < home.length; i++) {
            home[i] = false;
        }
        for (int i = 0; i < away.length; i++) {
            away[i] = false;
        }
    }

    public void calculateBreaks() {
        for (int i = 1; i < games.length; i++) {
            if(games[i] != null){
                if(games[i] == games[i-1]){
                    if(games[i]) {
                        home[i] = true;
                    } else {
                        away[i] = true;
                    }
                }
            }
        }
    }
    // prints games and breaks to console
    // if home k, if away v
    public void printBreaks(){
        System.out.println("Peli | kotiB | vierasB");
        for (int i = 0; i < games.length; i++) {
            String g = "";
            if(games[i] != null){
                g = games[i] ? "k":"v";
            }

            String k = home[i] ? "1": " ";
            String v = away[i] ? "1": " ";
            System.out.println(g +"\t | "+ k + "\t | " + v);
        }
    }

    // Returns sum of breaks from array
    public int totalAmount(Boolean[] breakArray){
        int sum = 0;
        for (int i = 0; i < breakArray.length; i++) {
            sum = (breakArray[i]) ? sum+1 : sum;
        }
        return sum;
    }

    public void updateAndCheck(int round, Boolean game) {
        games[round-1] = game;
        checkSpecificRound(round);
    }

    // checks specific round based on number in parameter
    public void checkSpecificRound(int roundNumber) {
        int index = roundNumber - 1;
        int secondIndex = roundNumber - 2;
        int nextIndex = index+1;
        home[index] = false;
        away[index] = false;

        // If index if null, need to find next not null index
        if (games[index] == null) {
            int counter = 0;
            while (games[index + counter] == null) {
                counter++;
            }
            index += counter;
        }

        // if index-1 if null, needs to find next not null index
        if (games[secondIndex] == null) {
            int counter = 0;
            while (games[secondIndex - counter] == null) {
                counter++;
            }
            secondIndex -= counter;
        }

        // if nextIndex is null, needs to find next not null index
        if (games[nextIndex] == null) {
            int counter = 0;
            while (games[nextIndex + counter] == null) {
                counter++;
            }
            nextIndex += counter;
        }

        // if current index is not same with next game's index set both breaks to false
        if(games[index] != games[nextIndex]) {
            home[nextIndex] = false;
            away[nextIndex] = false;
        }

        // if current index is same with next games' index set break to correct array
        if(games[index] == games[nextIndex]) {
            if (games[nextIndex]) {
                home[nextIndex] = true;
            } else {
                away[nextIndex] = true;
            }
        }

        // if current index is same with game before set break to correct array
        if (games[index] == games[secondIndex]) {
            if (games[index]) {
                home[index] = true;
            } else {
                away[index] = true;
            }
        }
    }
}