package SeriesSolver;

public class Game implements Cloneable {
    int home;
    int away;
    boolean isMovable;

    public Game(int home, int away) {
        this.home = home;
        this.away = away;
        this.isMovable = true; // Tällä flagilla pystytään asettamaan peli siirtämättömäksi
    }

    public int getHome() {
        return home;
    }

    public int getAway() {
        return away;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean isMovable() {
        return isMovable;
    }
    public void setMovable(boolean mov) {
        this.isMovable = mov;
    }

    public String printOutput() {
        return home + " " + away;
    }
    @Override
    public String toString() {
        return home + " vs " + away;
    }
}
