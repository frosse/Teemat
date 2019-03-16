package SeriesSolver;

public interface IConstraint {
     void calculateErrors(Series series);
     void printErrors();
     void initializeConstraint(int rounds, int teams);
     int[][] getErrors();
     int getTotalErrorSum();
     Object clone() throws CloneNotSupportedException;
}
