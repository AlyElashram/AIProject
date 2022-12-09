package code;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class CoastGuard {
    int capacity;
    int numberOfPassengers;
    int blackBoxes;

    public CoastGuard(){
        capacity = 0;
        numberOfPassengers = 0;
        blackBoxes = 0;
    }
    @Override 
    public String toString(){
        return this.numberOfPassengers+"";
    }
    public void setCapacity(int cap){
        this.capacity = cap;
    }
    public boolean equal(CoastGuard g){
        if(g.capacity == this.capacity && g.numberOfPassengers == this.numberOfPassengers && g.blackBoxes == this.blackBoxes){return true;}
        else{return false;}
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
       ArrayList<Station> stations = new ArrayList<Station>();
       ArrayList<Ship> ships = new ArrayList<Ship>();
       int cg_x ;
       int cg_y;
       boolean min_ship = false;
       boolean min_station = false;
       Random rand = new Random();
       width = rand.nextInt(5,16);
       height = rand.nextInt(5,16);
       for(int i =0 ; i<height;i++){
           for (int j=0;j<width;j++){
               int num = rand.nextInt(0,31);
               if(num==5){
                    ships.add(new Ship(rand.nextInt(0,101),i,j));
                   
                   min_ship = true;
               }
               if(num == 10){
                stations.add(new Station(i,j));
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

           ships.add(new Ship(rand.nextInt(0,101), i_ship, j_ship));
           stations.add(new Station(i_station, j_station));





       }

       cg_x = rand.nextInt(0,height);
       cg_y = rand.nextInt(0,width);
       //Check coast guard not where any station or ship is
       boolean onShip =true;
       boolean onStation = true; 
       while(onShip || onStation){
            for(int i=0;i<ships.size();i++){
                Ship current = ships.get(i);
                if(current.locationX == cg_x && current.locationY == cg_y){
                    break;
                }
                onShip = false;
            }
            for(int i=0;i<stations.size();i++){
                Station current = stations.get(i);
                if(current.locationX == cg_x && current.locationY == cg_y){
                    break;
                }
                onStation = false;
            }
           cg_x = rand.nextInt(0,height);
           cg_y = rand.nextInt(0,width);
       }





       String stringGrid ="";
       stringGrid += width+","+height+";";
       //Adding Coast Guard Capacity
       stringGrid+=rand.nextInt(30,101)+";";
       stringGrid +=cg_x+","+cg_y+";";

       for(int i=0;i<stations.size();i++){
        Station current = stations.get(i);
           stringGrid +=current.locationX + "," + current.locationY +",";
       }
       stringGrid = stringGrid.substring(0,stringGrid.length()-1);
       stringGrid+=";";
       
       for(int i=0;i<ships.size();i++){
        Ship current = ships.get(i);
        stringGrid +=current.locationX + "," + current.locationY +","+current.numberOfPassengers+",";
        }
        stringGrid = stringGrid.substring(0,stringGrid.length()-1);
       stringGrid+=";";


       return stringGrid;

   }

    public static String solve(String grid,String approach,boolean visualise){
        String solution="";
        if(approach.equals("BF")){
        solution = BFS(grid,visualise);
        }
        else if(approach.equals("DF")){
            solution = DFS(grid,visualise);
        }
        else if(approach.equals("ID")){
            solution = ID(grid,visualise);
        }
       else {
        solution = GreedyAndAstar(grid, visualise, approach);
       }

        
        return solution;
    }

    public static String ID(String grid ,boolean visualise){

        String actions = "";
        String died = "";
        String reitreived = "";

        int numberOfNodes=0;
        Queue<Node> q = new LinkedList<Node>();
        HashSet<String> nodes = new HashSet<String>();
        CoastGuard guard = new CoastGuard();
        State initialState = new State(grid,guard);
        int cg_x=initialState.coastGuard_x;
        int cg_y = initialState.coastGuard_y;
        Node initialNode = new Node(initialState, "", 0,0,"ID");
        nodes.add(initialNode.toString());
        q.add(initialNode);
        boolean goal = false;
        int depth =0;
        while(!goal)
        {
            for(int i=0;i<=depth;i++) {
                Node n = q.poll();
               if(n.depth > depth) break;
            
                n.state.checks();
                if (n.state.isGoal) {


                    died = n.state.deadPassengers + "";
                    actions = n.operator;
                    reitreived = n.state.blackBoxesRetreived + "";
                    if(visualise){
                        visualise(initialState,n,cg_x,cg_y);
                    }

                    return actions.substring(0, actions.length() - 1) + ";" + died + ";" + reitreived + ";" + nodes.size();

                }


                Node add = n.pickUp();

                if (!(add == null) && !nodes.contains(add.toString())) {
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length - 1];
                    if (!lastOperation.equals("pickup")) {
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }
                add = n.reitreive();
                if (!(add == null) && !nodes.contains(add.toString())) {
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length - 1];
                    if (!lastOperation.equals("retrieve") && add.state.onWreck) {
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }
                add = n.drop();
                if (!(add == null) && !nodes.contains(add.toString())) {
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length - 1];
                    if (!lastOperation.equals("drop")) {
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }


                add = n.moveRight();
                if (!(add == null) && !nodes.contains(add.toString())) {
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length - 1];
                    if (!lastOperation.equals("left")) {
                        q.add(add);
                        nodes.add(add.toString());
                    }

                }
                add = n.moveUp();
                if (!(add == null) && !nodes.contains(add.toString())) {

                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length - 1];
                    if (!lastOperation.equals("down")) {
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }
                add = n.moveLeft();
                if (!(add == null) && !nodes.contains(add.toString())) {
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length - 1];
                    if (!lastOperation.equals("right")) {
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }
                add = n.moveDown();
                if (!(add == null) && !nodes.contains(add.toString())) {
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length - 1];
                    if (!lastOperation.equals("up")) {
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }
            }
        depth++;
        }



        return actions.substring(0,actions.length()-1) +";"+ died +";"+reitreived+";"+numberOfNodes;

    }

    public static String DFS(String grid,boolean visualise){

        String actions = "";
        String died = "";
        String reitreived = "";

        int numberOfNodes=0;

        Stack<Node> q = new Stack<Node>();
        HashSet<String> nodes = new HashSet<String>();
        CoastGuard guard = new CoastGuard();
        State initialState = new State(grid,guard);
        int cg_x =initialState.coastGuard_x;
        int cg_y = initialState.coastGuard_y;
        Node initialNode = new Node(initialState, "", 0,0,"DFS");
        nodes.add(initialNode.toString());
        q.add(initialNode);
        boolean goal = false;
        while(!goal){
            Node n = q.pop();
            n.state.checks();
            if(n.state.isGoal){

                died=n.state.deadPassengers+"";
                actions =n.operator;
                reitreived = n.state.blackBoxesRetreived+"";
                if(visualise){
                    visualise(initialState,n,cg_x,cg_y);
                }

                return actions.substring(0,actions.length()-1) +";"+ died +";"+reitreived+";"+nodes.size();

            }


            Node add = n.pickUp();

            if(!(add==null)&&!nodes.contains(add.toString())){
                String[] operations = n.operator.split(",");
                String lastOperation = operations[operations.length-1];
                if(!lastOperation.equals("pickup") && !nodes.contains(add.toString())){
                    q.add(add);
                    nodes.add(add.toString());
                }
            }
            add=n.reitreive();
            if(!(add==null)&&!nodes.contains(add.toString())){
                String[] operations = n.operator.split(",");
                String lastOperation = operations[operations.length-1];
                if(!lastOperation.equals("retrieve")&& !nodes.contains(add.toString()) && add.state.onWreck){
                    q.add(add);
                    nodes.add(add.toString());
                }
            }
            add=n.drop();
            if(!(add==null)&& !nodes.contains(add.toString())){
                String[] operations = n.operator.split(",");
                String lastOperation = operations[operations.length-1];
                if(!lastOperation.equals("drop")&& !nodes.contains(add.toString())){
                    q.add(add);
                    nodes.add(add.toString());
                }
            }



            add = n.moveRight();
            if(!(add==null)&& !nodes.contains(add.toString())){
                String[] operations = n.operator.split(",");
                String lastOperation = operations[operations.length-1];
                if(!lastOperation.equals("left")){
                    q.add(add);
                    nodes.add(add.toString());
                }

            }
            add = n.moveUp();
            if(!(add==null)&& !nodes.contains(add.toString())){

                String[] operations = n.operator.split(",");
                String lastOperation = operations[operations.length-1];
                if(!lastOperation.equals("down")){
                    q.add(add);
                    nodes.add(add.toString());
                }
            }
            add = n.moveLeft();
            if(!(add==null)&& !nodes.contains(add.toString())){
                String[] operations = n.operator.split(",");
                String lastOperation = operations[operations.length-1];
                if(!lastOperation.equals("right")){
                    q.add(add);
                    nodes.add(add.toString());
                }
            }
            add = n.moveDown();
            if(!(add==null)&&!nodes.contains(add.toString())){
                String[] operations = n.operator.split(",");
                String lastOperation = operations[operations.length-1];
                if(!lastOperation.equals("up")){
                    q.add(add);
                    nodes.add(add.toString());
                }
            }

        }



        return actions.substring(0,actions.length()-1) +";"+ died +";"+reitreived+";"+numberOfNodes;
    }

    public static String BFS(String grid,boolean visualise){

        String actions = "";
        String died = "";
        String reitreived = "";

        int numberOfNodes=0;
            Queue<Node> q = new LinkedList<Node>();
            HashSet<String> nodes = new HashSet<String>();
            CoastGuard guard = new CoastGuard();
            State initialState = new State(grid,guard);
            int CG_x = initialState.coastGuard_x;
            int CG_y = initialState.coastGuard_y; 
            Node initialNode = new Node(initialState, "", 0,0,"BFS");
            nodes.add(initialNode.toString());
            q.add(initialNode);
            boolean goal = false;

            while(!goal){
                Node n = q.poll();

                n.state.checks();

                if(n.state.isGoal){


                    died=n.state.deadPassengers+"";
                    actions =n.operator;
                    reitreived = n.state.blackBoxesRetreived+"";
                    if(visualise){
                        visualise(initialState,n,CG_x,CG_y);
                    }
                    return actions.substring(0,actions.length()-1) +";"+ died +";"+reitreived+";"+nodes.size();

                }


                Node add = n.pickUp();

                if(!(add==null)&&!nodes.contains(add.toString())){
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length-1];
                    if(!lastOperation.equals("pickup")){
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }
                add=n.reitreive();
                if(!(add==null)&&!nodes.contains(add.toString())){
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length-1];
                    if(!lastOperation.equals("retrieve")&& add.state.onWreck){
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }
                add=n.drop();
                if(!(add==null)&& !nodes.contains(add.toString())){
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length-1];
                    if(!lastOperation.equals("drop")){
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }



                add = n.moveRight();
                if(!(add==null)&& !nodes.contains(add.toString())){
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length-1];
                    if(!lastOperation.equals("left")){
                        q.add(add);
                        nodes.add(add.toString());
                    }

                }
                add = n.moveUp();
                if(!(add==null)&& !nodes.contains(add.toString())){

                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length-1];
                    if(!lastOperation.equals("down")){
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }
                add = n.moveLeft();
                if(!(add==null)&& !nodes.contains(add.toString())){
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length-1];
                    if(!lastOperation.equals("right")){
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }
                add = n.moveDown();
                if(!(add==null)&&!nodes.contains(add.toString())){
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length-1];
                    if(!lastOperation.equals("up")){
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }

            }



        return actions.substring(0,actions.length()-1) +";"+ died +";"+reitreived+";"+numberOfNodes;
    }

    public static String GreedyAndAstar(String grid,boolean visualise,String type){
        int heuristicFunc = 0;
        if(type.equals("GR1")||type.equals("AS1")){
            heuristicFunc=1;
        }else{
            heuristicFunc =2;
        }
        String actions = "";
        String died = "";
        String reitreived = "";
        String numberOfNodesExpanded="";
        int numberOfNodes=0;
            Queue<Node> q = new PriorityQueue<Node>();
            HashSet<String> nodes = new HashSet<String>();
            CoastGuard guard = new CoastGuard();
            State initialState = new State(grid,guard);
            int cg_x = initialState.coastGuard_x;
            int cg_y = initialState.coastGuard_y;
            Node initialNode = new Node(initialState, "", 0,heuristicFunc,type);
            nodes.add(initialNode.toString());
            q.add(initialNode);
            boolean goal = false;

            while(!goal){
                Node n = q.poll();
                numberOfNodes++;
                n.state.checks();
                if(n.state.isGoal){
                    died=n.state.deadPassengers+"";
                    actions =n.operator;
                    reitreived = n.state.blackBoxesRetreived+"";
                    numberOfNodesExpanded = numberOfNodes+"";
                    if(visualise){
                        visualise(initialState,n,cg_x,cg_y);
                    }
                    return actions.substring(0,actions.length()-1) +";"+ died +";"+reitreived+";"+numberOfNodesExpanded;

                }


                Node add = n.pickUp();

                if(!(add==null)&&!nodes.contains(add.toString())){
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length-1];
                    if(!lastOperation.equals("pickup")){
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }
                add=n.reitreive();
                if(!(add==null)&&!nodes.contains(add.toString())){
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length-1];
                    if(!lastOperation.equals("retrieve")&& add.state.onWreck){
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }
                add=n.drop();
                if(!(add==null)&& !nodes.contains(add.toString())){
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length-1];
                    if(!lastOperation.equals("drop")){
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }



                add = n.moveRight();
                if(!(add==null)&& !nodes.contains(add.toString())){
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length-1];
                    if(!lastOperation.equals("left")){
                        q.add(add);
                        nodes.add(add.toString());
                    }

                }
                add = n.moveUp();
                if(!(add==null)&& !nodes.contains(add.toString())){

                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length-1];
                    if(!lastOperation.equals("down")){
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }
                add = n.moveLeft();
                if(!(add==null)&& !nodes.contains(add.toString())){
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length-1];
                    if(!lastOperation.equals("right")){
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }
                add = n.moveDown();
                if(!(add==null)&&!nodes.contains(add.toString())){
                    String[] operations = n.operator.split(",");
                    String lastOperation = operations[operations.length-1];
                    if(!lastOperation.equals("up")){
                        q.add(add);
                        nodes.add(add.toString());
                    }
                }

            }



        return actions.substring(0,actions.length()-1) +";"+ died +";"+reitreived+";"+numberOfNodes;

    }
   
    public CoastGuard deepClone(){
        CoastGuard g = new CoastGuard();
        g.numberOfPassengers = this.numberOfPassengers;
        g.blackBoxes = this.blackBoxes;
        g.capacity =this.capacity;
        return g;
    }

    //Take String grid and not initial state and then parse it on your owns
    public static void visualise(State initialState,Node goal,int cg_x,int cg_y){
        State state = goal.state;
        //Gets each single step into a string array and makes sure that an extra comma in the end is removed
        String[] stepsFollowed = goal.operator.substring(0,goal.operator.length()-1).split(",");
        ArrayList<Ship> ships = initialState.ships;
        ArrayList<Station> stations = initialState.stations;
        HashMap<String,String> locations = new HashMap<>();
        //Add ship locations to hashmap for easier repeated access in the printing loop
        for(int i=0;i<ships.size();i++){
            Ship current = ships.get(i);
            locations.put(current.locationX+""+current.locationY,"Ship");        }
        
        //Add station location to hasmap
        for(int i=0;i<stations.size();i++){
            Station current1 = stations.get(i);
            locations.put(current1.locationX+""+current1.locationY,"Station");
        }
        


        for(int k =0 ; k<stepsFollowed.length;k++){
            //Main Loop to print the grid on each action
           System.out.println(stepsFollowed[k]);
           if(stepsFollowed[k].equals("right")){
            cg_y++;
           }

           else if(stepsFollowed[k].equals("up")){
            cg_x--;
           }

           else if(stepsFollowed[k].equals("down")){
            cg_x++;
           }

           else if(stepsFollowed[k].equals("left")){
            cg_y--;
           }
            System.out.println();
            String cell = "";
        //Second loop to print the multiple Horizontal lines  of the grid
        for(int i=0;i<state.n;i++){
            System.out.println("-------------------------------------------------");
            //Third loop to print the single Horizontal line of the grid
            for (int j=0;j<state.m;j++){
                if(cg_x == i && cg_y ==j){
                    cell+="CG";
                }
                if(!(locations.get(i+""+j)==null)){
                    cell+=locations.get(i+""+j);
                }
                if(cell.isEmpty()){
                    cell+=" blank||";
                }
                else{
                    cell+="||";
                }
                
                System.out.print(cell);
                cell="";
            }
            System.out.println();
        }
        System.out.println();
       }
    }


}
