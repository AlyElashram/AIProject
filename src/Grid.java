import java.util.ArrayList;
import java.util.Random;

public class Grid {
    int width;
    int height;
    ArrayList<Integer> ship_location_i = new ArrayList<Integer>();
    ArrayList<Integer> ship_location_j = new ArrayList<Integer>();
    ArrayList<Integer> station_location_i = new ArrayList<Integer>();
    ArrayList<Integer> station_location_j = new ArrayList<Integer>();
    ArrayList<Ship> ships = new ArrayList<Ship>();
    ArrayList<Station> stations = new ArrayList<Station>();
    CoastGuard coastGuard;
    int cg_i ;
    int cg_j;
    Object[][] grid;





    public Grid(){
        boolean min_ship = false;
        boolean min_station = false;
        Random rand = new Random();
        width = rand.nextInt(5,16);
        height = rand.nextInt(5,16);
        grid = new Object[height][width];
        for(int i =0 ; i<height;i++){
            for (int j=0;j<width;j++){
                int num = rand.nextInt(0,31);
                if(num==5){
                    Ship ship = new Ship();
                    ships.add(ship);
                    grid[i][j] = ship;
                    ship_location_i.add(i);
                    ship_location_j.add(j);
                    min_ship = true;
                }
                if(num == 10){
                    Station station = new Station();
                    stations.add(station);
                    grid[i][j] = station;
                    station_location_i.add(i);
                    station_location_j.add(j);
                    min_station = true;
                }

            }
        }
        //If there were no ships or stations generated then generate the minimum of at least one
        if(!min_ship || !min_station){
            int i_ship = rand.nextInt(0,height);
            int j_ship = rand.nextInt(0,width);
            int i_station = rand.nextInt(0,height);
            int  j_station = rand.nextInt(0,width);
            while(i_ship==i_station && j_ship==j_station){
                i_ship = rand.nextInt(0,height);
                j_ship = rand.nextInt(0,width);
                i_station = rand.nextInt(0,height);
                j_station = rand.nextInt(0,width);
            }
            ship_location_i.add(i_ship);
            ship_location_j.add(j_ship);
            station_location_i.add(i_station);
            station_location_j.add(j_station);

            Ship ship=new Ship();
            ships.add(ship);
            grid[i_ship][j_ship] = ship;

            Station station = new Station();
            stations.add(station);
            grid[i_station][j_station] = station;

            

        }
        
        this.cg_i = rand.nextInt(0,height);
        this.cg_j = rand.nextInt(0,width);
        while((ship_location_i.contains(this.cg_i) && ship_location_j.contains(this.cg_j)) || (station_location_i.contains(this.cg_i) &&station_location_j.contains(this.cg_j) )){
            this.cg_i = rand.nextInt(0,height);
            this.cg_j = rand.nextInt(0,width);
        }

        this.coastGuard = new CoastGuard();
        grid[this.cg_i][this.cg_j] = this.coastGuard;

    } 
   
   
    public String genGrid(){
        String grids ="";
        grids += width+","+height+";";
        grids+=this.coastGuard.capacity+";";
        grids +=cg_i+","+cg_j+";";
        for(int i=0;i<ship_location_i.size();i++){
        grids += ship_location_i.get(i) + "," + ship_location_j.get(i) +","+ships.get(i).numberOfPassengers+";";
        }
        for(int i=0;i<station_location_i.size();i++){
            grids +=station_location_i.get(i) + "," + station_location_j.get(i) +";";
        }
        
       
        return grids;

    }



}
