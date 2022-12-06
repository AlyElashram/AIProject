package code;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
public class CoastGuard {
    int capacity;
    int numberOfPassengers;
    int blackBoxes;

    public CoastGuard(){
        capacity = 0;
        numberOfPassengers = 0;
        blackBoxes = 0;
        
    }
    public void setCapacity(int cap){
        this.capacity = cap;
    }

    
    public void pickUp(Ship ship){
        this.numberOfPassengers += ship.pickUpPassengers(this.capacity-this.numberOfPassengers);

    }


    public void reitreiveBox(Ship ship){
        if(ship.isWreck){
            if(ship.isReitrivable){
                blackBoxes++;
            }
        }
    }

    public static String genGrid(){
        int width;
        int height;
        ArrayList<Integer> ship_location_i = new ArrayList<Integer>();
        ArrayList<Integer> ship_location_j = new ArrayList<Integer>();
        ArrayList<Integer> station_location_i = new ArrayList<Integer>();
        ArrayList<Integer> station_location_j = new ArrayList<Integer>();
        ArrayList<Ship> ships = new ArrayList<Ship>();
        CoastGuard coastGuard;
        int cg_i ;
        int cg_j;
        boolean min_ship = false;
        boolean min_station = false;
        Random rand = new Random();
        width = rand.nextInt(5,16);
        height = rand.nextInt(5,16);
        for(int i =0 ; i<height;i++){
            for (int j=0;j<width;j++){
                int num = rand.nextInt(0,31);
                if(num==5){
                    ship_location_i.add(i);
                    ship_location_j.add(j);
                    min_ship = true;
                }
                if(num == 10){
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



            

        }
        
        cg_i = rand.nextInt(0,height);
        cg_j = rand.nextInt(0,width);
        while((ship_location_i.contains(cg_i) && ship_location_j.contains(cg_j)) || (station_location_i.contains(cg_i) &&station_location_j.contains(cg_j) )){
            cg_i = rand.nextInt(0,height);
            cg_j = rand.nextInt(0,width);
        }

        coastGuard = new CoastGuard();




        String stringGrid ="";
        stringGrid += width+","+height+";";
        stringGrid+=coastGuard.capacity+";";
        stringGrid +=cg_i+","+cg_j+";";
        
        for(int i=0;i<station_location_i.size();i++){
            stringGrid +=station_location_i.get(i) + "," + station_location_j.get(i) +",";
        }
        stringGrid+=";";
        for(int i=0;i<ship_location_i.size();i++){
            stringGrid += ship_location_i.get(i) + "," + ship_location_j.get(i) +","+ships.get(i).numberOfPassengers+",";
            }
        stringGrid+=";";
        
       
        return stringGrid;

    }

    public static String solve(String grid,String approach,boolean visualise){
        String passengers ="";
        String actions = "";
        String died = "";
        String total = "";
        if(approach.equalsIgnoreCase("BF")){
            Queue<Node> q = new LinkedList<Node>();
            CoastGuard guard = new CoastGuard();
            State initialState = new State(grid,guard);
            Node initialNode = new Node(initialState, "Root", 0, 0);
            q.add(initialNode);



            boolean goal = false;
            while(!goal){
                Node n = q.poll();
                System.out.println("X is:"+n.state.coastGuard_x +" Y is:"+n.state.coastGuard_y+"operator is:"+n.operator);

                if(n.state.isGoal){
                    System.out.println("Goal State Found");
                    passengers = n.state.passengersSaved +"";
                    died=n.state.deadPassengers+"";
                    total = n.state.totalPassengers+"";
                    actions = "actions are "+ n.operator ;
                    goal = true;
                    break;
                }
                
                Node add = n.moveRight();
                if(add!=null){
                    q.add(add);
                }
                add = n.moveUp();
                if(add!=null){
                    q.add(add);
                }
                add = n.moveLeft();
                if(add!=null){
                    q.add(add);
                }
                add = n.moveDown();
                if(add!=null){
                    q.add(add);
                }
                add = n.pickUp();
                if(add!=null){
                    q.add(add);
                }
                add=n.reitreive();
                if(add!=null){
                    q.add(add);
                }
                add=n.drop();
                if(add!=null){
                    q.add(add);
                }
            }
            
        }


        return actions +";"+ passengers +";"+total+";"+died;
    }

}
