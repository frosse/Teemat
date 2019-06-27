package test;

public class Main {

    public static void main(String[] args) {
        Games games2 = new Games(10);
        Games games = new Games(new Boolean[] {true, true, false, null, null, true, null, null, false, true});
        games.calculateBreaks();
        games.printBreaks();
        System.out.println("kotibreakit: "+ games.totalAmount(games.home));
        System.out.println("vierasbreakit: "+ games.totalAmount(games.away));
        games.updateAndCheck(6, false);
        games.printBreaks();
        System.out.println("kotibreakit: "+ games.totalAmount(games.home));
        System.out.println("vierasbreakit: "+ games.totalAmount(games.away));
    }
}
