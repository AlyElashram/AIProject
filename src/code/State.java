package code;

import java.util.ArrayList;

public class State {
    int m;
    int n;
    
    int totalPassengers;
    CoastGuard guard;
    int coastGuard_x;
    int coastGuard_y;
    Ship currentShip;
    boolean onShip = false;
    boolean onStation =false;

    
    ArrayList<Station> stations = new ArrayList<Station>();
    ArrayList<Ship> ships = new ArrayList<Ship>();

    int deadPassengers=0;
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
            guard.setCapacity(Integer.parseInt(split[1])); 
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
                int locx = -1;
                int locy = -1;
                if(i%2==0){
                    locx = Integer.parseInt(stationsLocations[i]);
                }else{
                    locy = Integer.parseInt(stationsLocations[i]);
                    stations.add(new Station(locx,locy));
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
                    totalPassengers+=Integer.parseInt(shipsDetails[i]);
                    int passengers = Integer.parseInt(shipsDetails[i]);
                    ships.add(new Ship(passengers,locx,locy));
                }
            }



        

    }
    public State(int saved,int died,boolean onShip,boolean onStation,int reitreived,ArrayList<Ship>ships,int m, int n,int totalPassengers,
    CoastGuard guard,int x,int y,ArrayList<Station> stations){
        this.passengersSaved =saved;
        this.deadPassengers = died;
        this.blackBoxesRetreived = reitreived;
        this.ships = new ArrayList<Ship>();
        ships.forEach(ship->this.ships.add(ship));
        this.m = m;
        this.n = n;
        this.totalPassengers = totalPassengers;
        this.guard=new CoastGuard();
        this.guard.setCapacity(guard.capacity);
        this.coastGuard_x = x;
        this.coastGuard_y = y;
        
        for(int i=0;i<stations.size();i++){
            this.stations.add(stations.get(i));
        }


    }
   public void action(){
    boolean setGoal = true;
    boolean setShip = false;
    boolean setStation = false;
    for(int i=0;i<ships.size();i++){
        Ship current = ships.get(i);
        
        current.action();
        deadPassengers++;
        if(coastGuard_x == current.locationX && coastGuard_y ==current.locationY){
            System.out.println("On Ship");
            setShip = true;
        }
        if(current.numberOfPassengers !=0 || (current.isReitrivable && !current.isReitrieved)) {
            setGoal =false; 
        }
        
    }
    for(int i=0;i<stations.size();i++){
        Station current = stations.get(i);
        if(current.locationX == coastGuard_x && current.locationY == coastGuard_y){
            setStation = true;
        }
    }
    this.onShip = setShip;
    this.onStation = setStation;
    this.isGoal = setGoal;
   }


    public boolean equals(State s){
        if(this.coastGuard_x!=s.coastGuard_x || this.coastGuard_y!=s.coastGuard_y ||
        this.passengersSaved!=s.passengersSaved || this.blackBoxesRetreived!=s.blackBoxesRetreived){return false;}
        
        return true;
    }

    public State deepClone(){
        return new State(this.passengersSaved,this.deadPassengers,this.onShip,this.onStation,this.blackBoxesRetreived,this.ships,this.m,this.n,this.totalPassengers,this.guard,this.coastGuard_x
        ,this.coastGuard_y,this.stations);

    }

}
