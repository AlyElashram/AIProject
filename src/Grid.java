import java.util.Random;
import java.awt.Point;
public class Grid {
    int width;
    int height;
    Ship ship;
    CoastGuard coastGuard;
    Station station;
    Object[][] grid;
    Point ship_location;
    Point station_location;
    Point coastGuard_location;




    public Grid(){
        Random rand = new Random();
        ship = new Ship();
        station = new Station();
        coastGuard = new CoastGuard();
        width = rand.nextInt(5,16);
        height = rand.nextInt(5,16);
        grid = new Object[width][height];
        this.ship_location = new Point(0,0);
        this.station_location =new Point(0,0);
        this. coastGuard_location = new Point(0,0);

        while(this.ship_location == this.station_location || this.station_location==this.coastGuard_location ||this.coastGuard_location == this.ship_location){
            this.ship_location.x= rand.nextInt(0,width+1);
           this.station_location.x = rand.nextInt(0,width+1);
           this.coastGuard_location.x = rand.nextInt(0,width+1);

           this.ship_location.y = rand.nextInt(0,height+1);
           this.station_location.y =rand.nextInt(0,height+1);
           this.coastGuard_location.y= rand.nextInt(0,height+1);
        }
        grid[ship_location.x][ship_location.y]=ship;
        grid[station_location.x][station_location.y] = station;
        grid[coastGuard_location.x][coastGuard_location.y] = coastGuard;
        
    } 
    public String genGrid(){
        String grids ="";
        grids += width+","+height+";";
        grids+=coastGuard.capacity+";";
        grids +=coastGuard_location.x+","+coastGuard_location.y+";";
        grids +=station_location.x+","+station_location.y+";";
        grids +=ship_location.x+","+ship_location.y+','+ship.numberOfPassengers+";";
        return grids;

    }
}
