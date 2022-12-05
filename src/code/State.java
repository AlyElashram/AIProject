package code;

import java.util.ArrayList;

public class State {
    int m;
    int n;
    
    
    int capacity;
    CoastGuard guard;
    int coastGuard_x;
    int coastGuard_y;
    boolean onShip;
    boolean onStation;

    //2D array 3ashan a3raf arbot


    ArrayList<Integer> stationLocationX = new ArrayList<Integer>();
    ArrayList<Integer> stationLocationY = new ArrayList<Integer>();


    ArrayList<Ship> ships = new ArrayList<Ship>();


    int passengersSaved;
    int blackBoxesRetreived;

    boolean isGoal;

    public State(String grid,CoastGuard guard){
        this.guard = guard;
        //parse el grid ba2a hena
            String[] split = grid.split(";");
       
            //Awel index heya el width wel height
           String[]dimensions = split[0].split(",");
            m= Integer.parseInt(dimensions[0]);
            n= Integer.parseInt(dimensions[1]);
            //Tany index Capacity Coast Guard
            this.capacity = Integer.parseInt(split[1]);
            //Talta location coast guard
            String[]location = split[2].split(",");
            coastGuard_x = Integer.parseInt(location[0]);
            coastGuard_y = Integer.parseInt(location[1]);
            //rab3a locations of stations
            String[] stationsLocations = split[3].split(",");
            for(int i=0;i<stationsLocations.length;i++){
                //Even indeces are the X locations
                //Odd indeces are the Y locations
                //No need to create station it self as it has no maximum capcity
                //and we are already keeping track of people saved in the states
                if(i%2==0){
                    stationLocationX.add(Integer.parseInt(stationsLocations[i]));
                }
                else{
                    stationLocationY.add(Integer.parseInt(stationsLocations[i]));
                }
            }
            //5amsa location and number of passengers of ship
            String[] shipsDetails = split[4].split(",");
            int locx = -1;
            int locy = -1;
            for(int i =0 ;i<shipsDetails.length;i++){
                if(i%3==0){
                    locx = Integer.parseInt(shipsDetails[i]);
                }
                else if(i%3==1){
                    locy = Integer.parseInt(shipsDetails[i]);
                }
                else{
                    int passengers = Integer.parseInt(shipsDetails[i]);
                    ships.add(new Ship(passengers,locx,locy));
                }
            }



        

    }
    public State(){}
   public void action(){
    
    for(int i=0;i<ships.size();i++){
        Ship current = ships.get(i);
        current.action();
        if(coastGuard_x == current.locationX && coastGuard_y ==current.locationY){
            onShip = true;
        }else{
            onShip =false;
        }
        if(current.numberOfPassengers !=0 || (current.isReitrivable && !current.isReitrieved)) {
            this.isGoal =false; 
            return;
        }
        
    }
    if(stationLocationX.contains(coastGuard_x) && stationLocationY.contains(coastGuard_y)){
        onStation =true;
    }else{
        onStation=false;
    }
   }


    public boolean equals(State s){
        if(this.coastGuard_x!=s.coastGuard_x || this.coastGuard_y!=s.coastGuard_y ||
        this.passengersSaved!=s.passengersSaved || this.blackBoxesRetreived!=s.blackBoxesRetreived){return false;}
        
        return true;
    }
}
