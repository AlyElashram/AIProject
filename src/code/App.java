package code;

public class App {
    public static void main(String[] args) {

        String grid0 = "5,6;50;0,1;0,4,3,3;1,1,90;";

        CoastGuard.solve(grid0,"BF", true);
       // System.out.print(CoastGuard.genGrid());
    }
}
