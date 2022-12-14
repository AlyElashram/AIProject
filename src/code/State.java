package code;
import java.util.ArrayList;

public class State {
    int m;
    int n;
    int heuristicCost;
    int totalPassengers;
    CoastGuard guard;
    int coastGuard_x;
    int coastGuard_y;
    Ship currentShip;
    Station currentStation;
    boolean onShip = false;
    boolean onStation =false;
    boolean onWreck = false;

    
    ArrayList<Station> stations = new ArrayList<Station>();
    ArrayList<Ship> ships = new ArrayList<Ship>();

    int deadPassengers=0;
    int passengersSaved;
    int blackBoxesRetreived;

    boolean isGoal;

    @Override
    public String toString(){
        String s = "";
        for(int i=0;i<ships.size();i++){
            s+=ships.get(i).blackBoxDamage;
        }
        s+= this.guard.toString()+this.coastGuard_x+this.coastGuard_y+this.deadPassengers+this.blackBoxesRetreived;
        return s;
    }

    public State(String grid,CoastGuard guard){
        this.guard = guard;
        //parse el grid ba2a hena
            String[] split = grid.split(";");
       
            //Awel index heya el width wel height
           String[]dimensions = split[0].split(",");
            m= Integer.parseInt(dimensions[0]);
            n= Integer.parseInt(dimensions[1]);
            //Tany index Capacity Coast Guard 
            guard.setCapacity(Integer.parseInt(split[1])); 
            //Talta location coast guard
            String[]location = split[2].split(",");
            coastGuard_x = Integer.parseInt(location[0]);
            coastGuard_y = Integer.parseInt(location[1]);
            //rab3a locations of stations
            String[] stationsLocations = split[3].split(",");
            int locx = -1;
            int locy = -1;
            for(int i=0;i<stationsLocations.length;i++){    
                if(i%2==0){
                    locx = Integer.parseInt(stationsLocations[i]);
                }else{
                    locy = Integer.parseInt(stationsLocations[i]);
                    stations.add(new Station(locx,locy));
                }
            }
            //5amsa location and number of passengers of ship
            String[] shipsDetails = split[4].split(",");
            
            locx = -1;
            locy = -1;
            for(int i =0 ;i<shipsDetails.length;i++){

                if(i%3==0){
                    locx = Integer.parseInt(shipsDetails[i]);

                }
                else if(i%3==1){
                    locy = Integer.parseInt(shipsDetails[i]);

                }
                else{
                    totalPassengers+=Integer.parseInt(shipsDetails[i]);
                    int passengers = Integer.parseInt(shipsDetails[i]);
                    ships.add(new Ship(passengers,locx,locy));
                }
            }




        

    }
    public State(int saved,int died,boolean onShip,boolean onWreck,Ship ship ,boolean onStation,int reitreived,ArrayList<Ship>ships,int m, int n,int totalPassengers,
    CoastGuard guard,int x,int y,ArrayList<Station> stations,int heuristicCost){
        this.passengersSaved =saved;
        this.deadPassengers = died;
        this.blackBoxesRetreived = reitreived;
        this.currentShip = ship;
        this.ships = new ArrayList<Ship>();
        for(int i=0;i<ships.size();i++){
            this.ships.add(ships.get(i).deepClone());
        }
        this.m = m;
        this.n = n;
        this.totalPassengers = totalPassengers;
        this.guard=guard.deepClone();
        this.guard.setCapacity(guard.capacity);
        this.coastGuard_x = x;
        this.coastGuard_y = y;
        
        for(int i=0;i<stations.size();i++){
            this.stations.add(stations.get(i));
        }


    }
   
   public void checks(){
    boolean setGoal = true;
    boolean setShip = false;
    boolean setStation = false;
    boolean setWreck = false;

    for(int i=0;i<ships.size();i++){
        Ship current1 = ships.get(i);
        if(coastGuard_x == current1.locationX && coastGuard_y ==current1.locationY){
            setShip = true;
            currentShip = current1;
            if(currentShip.isWreck){
                setWreck = true;
            }
        }
        if(current1.numberOfPassengers !=0 || (current1.isReitrivable && !current1.isReitrieved) ||guard.numberOfPassengers !=0) {
            setGoal =false;
        } 
        
    }
    
    for(int i=0;i<stations.size();i++){
        Station current = stations.get(i);
        if(current.locationX == coastGuard_x && current.locationY == coastGuard_y){
            setStation = true;
            currentStation = current;
        }
    }
    this.onShip = setShip;
    this.onStation = setStation;
    this.isGoal = setGoal;
    this.onWreck = setWreck;

   }


   public void calculateHeuristic(int heuristicNumber) {
    if (heuristicNumber == 1) {

        //get passengers on ships
        int passengersOnShips = this.totalPassengers-this.deadPassengers;
        int ReitrivableBlackBoxes=0;
        for(int i=0;i<ships.size();i++){
            if(ships.get(i).isReitrivable && !ships.get(i).isReitrieved){
                ReitrivableBlackBoxes++;
            }
        }
        int blackBoxesNotRetrieved=ReitrivableBlackBoxes-this.blackBoxesRetreived;

        if(passengersOnShips==0){
            this.heuristicCost = blackBoxesNotRetrieved;
        }
        else{
            this.heuristicCost = (int) Math.ceil(2 * (passengersOnShips / this.guard.capacity)) ;
        }


        }
    
    
    else if (heuristicNumber == 2) {
        int passengersOnShips = this.totalPassengers -this.deadPassengers;
        int blackBoxesNotRetrieved=0;


        int passengersOnGuardShip = 0;
        if (this.guard.numberOfPassengers > 1) {
            passengersOnGuardShip = 1;
        }
        int ReitrivableBlackBoxes=0;
        for(int i=0;i<ships.size();i++){
            if(ships.get(i).isReitrivable && !ships.get(i).isReitrieved){
                ReitrivableBlackBoxes++;
            }
        }
        blackBoxesNotRetrieved=ReitrivableBlackBoxes-this.blackBoxesRetreived;
        if(passengersOnShips==0){
            this.heuristicCost =blackBoxesNotRetrieved+passengersOnGuardShip;

        }
        else{
            this.heuristicCost = (int) Math.ceil(2 * (passengersOnShips / this.guard.capacity)) + passengersOnGuardShip;

        }
    }
}


    //This function is called after every action
    //It fetches all the ships and does the logic (Adding damage to black box or reducing passenger)
    //If it reduces passengers it returns true so that we can track in the state how many passengers died without having
    //to loop on each ship again to track
    public int action(){
        for(int i=0;i<ships.size();i++){
            //Check if action reduced number of passengers
            boolean reduced = ships.get(i).action();
            if(reduced){
                deadPassengers++;
            }
        }
        return deadPassengers;

   }

  


   

    public State deepClone(){
        return new State(this.passengersSaved,this.deadPassengers,this.onShip,this.onWreck,this.currentShip,this.onStation,this.blackBoxesRetreived,this.ships,this.m,this.n,this.totalPassengers,this.guard,this.coastGuard_x
        ,this.coastGuard_y,this.stations,this.heuristicCost);

    }


}
