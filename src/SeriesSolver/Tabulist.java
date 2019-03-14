package SeriesSolver;
//This class is copied from teachers example

/*
This is not coded as it should be coded. This is because you students also have to do something.
*/
public class Tabulist {
    class Tabu {
        Game game;
        int rnd;
        Tabu ( Game game, int rnd ) {
            this.game = game;
            this.rnd = rnd;
        }
        public String toString() {
            return "(" + game + " rnd: " + rnd + ")";
        }
    }

    Tabu[] tl;
    int currSize;
    Tabulist ( int size ) {
        tl = new Tabu[size];
        currSize = 0;
    }

    public void add ( Game g, int rnd ) {
        Tabu t = new Tabu ( g, rnd );
        if ( currSize >= tl.length ) {
            shiftValues();
        }
        else currSize++;
        tl[currSize-1] = t;
    }

    public boolean isinList ( Game g, int rnd ) {
        for ( int i = 0; i < currSize; i++ ) {
            Tabu t = tl[i];
            if (( t.game == g ) && ( t.rnd == rnd ))
                return true;
        }
        return false;
    }

    // Testers.
    public void print () {
        for ( int i = 0; i < currSize; i++ )
            System.out.println ( tl[i] );
    }

    // Priavtes
    private void shiftValues() {
        for ( int i = 0; i < currSize-1; i++ ) {
            tl[i] = tl[i+1];
        }
    }
}
