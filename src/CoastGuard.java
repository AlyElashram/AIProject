import java.util.Random;
public class CoastGuard {
    int capacity;
    int numberOfPassengers;
    int blackBoxes;
    static Grid grid = new Grid();




    public CoastGuard(){
        Random rand = new Random();
        capacity = rand.nextInt(30,101);
        numberOfPassengers = 0;
        blackBoxes = 0;
        
    }

    
    public void pickUp(Ship ship){
        this.numberOfPassengers += ship.pickUpPassengers(this.capacity-this.numberOfPassengers);

    }
    public void drop(Station station){
        station.addSurvivors(this.numberOfPassengers);
        numberOfPassengers = 0;
    }
    public void reitreiveBox(Ship ship){
        if(ship.isWreck){
            if(ship.isReitrivable){
                blackBoxes++;
            }
        }
    }

    public static String genGrid(){
        String grid_string = grid.genGrid();
        return grid_string;
    }

}
