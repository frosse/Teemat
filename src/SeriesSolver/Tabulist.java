package SeriesSolver;//This class is copied from teachers example

/*
This is not coded as it should be coded. This is because you students also have to do something.
*/
public class Tabulist {
    class Tabu {
        int nmbr;
        int place;
        Tabu ( int n, int p ) {
            nmbr = n;
            place = p;
        }
        public String toString() {
            return "(" + nmbr + "," + place + ")";
        }
    }

    Tabu[] tl;
    int currSize;
    Tabulist ( int size ) {
        tl = new Tabu[size];
        currSize = 0;
    }

    public void add ( int n, int p ) {
        Tabu t = new Tabu ( n, p );
        if ( currSize >= tl.length ) {
            shiftValues();
        }
        else currSize++;
        tl[currSize-1] = t;
    }

    public boolean isinList ( int n, int p ) {
        for ( int i = 0; i < currSize; i++ ) {
            Tabu t = tl[i];
            if (( t.nmbr == n ) && ( t.place == p ))
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
