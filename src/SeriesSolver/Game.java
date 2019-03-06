package SeriesSolver;

public class Game implements Cloneable {
    int home;
    int away;

    public Game(int home, int away) {
        this.home = home;
        this.away = away;
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

    @Override
    public String toString() {
        return home + " vs " + away;
    }
}
