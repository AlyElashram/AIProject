package code;


public class Node implements Comparable<Node>{
    State state;
    String operator;
    int depth;
    int pathCost;
    int heuristicFunction;
    double heuristicCost;
    String type;

    public Node(State state,String oper,int dep,int heuristicFunction,String type){
        this.state = state;
        this.operator = oper;
        this.depth = dep;
        this.heuristicFunction = heuristicFunction;
        this.heuristicCost = state.heuristicCost;
        this.pathCost = state.deadPassengers;
        this.type = type; // A* or Greedy algorithm
    }

    @Override
    public String toString(){
        String s="";
        s+= this.state.toString();
        return s;
    }

    @Override
    public boolean equals(Object a){
        Node n = (Node) a ;
        if(n.state.equals(this.state) && this.operator.equals(n.operator) && this.depth == n.depth && n.pathCost == this.pathCost){
            return true;
        }else{
            return false;
        }
    }

    public Node moveLeft (){
        Node a = deepClone(this);
        a.state.checks();
        
        a.state.coastGuard_y--;
        if(a.state.coastGuard_y < 0 || a.state.coastGuard_y>a.state.m-1){
            return null;
        }
        a.operator+="left,";
        int pathCost = a.state.action();
        a.pathCost = pathCost; 
        a.state.calculateHeuristic(a.heuristicFunction);
        a.depth++;
        
        return a;
    }
    public Node moveUp (){
        Node a = deepClone(this);
        a.state.checks();
       
        a.state.coastGuard_x--;
        if(a.state.coastGuard_x < 0 || a.state.coastGuard_x>a.state.n-1){
            return null;
        }
        a.operator+="up,";
       int pathCost = a.state.action();
       a.pathCost = pathCost; 
       a.state.calculateHeuristic(a.heuristicFunction);
       
       a.depth++;
        
        return a;
    }
    public Node moveDown (){
        Node a = deepClone(this);
        a.state.checks();
      
        a.state.coastGuard_x++;
        if(a.state.coastGuard_x < 0 || a.state.coastGuard_x>a.state.n-1){
            return null;
        }
        a.operator+="down,";
        int pathCost = a.state.action();
        a.pathCost = pathCost;  
         a.state.calculateHeuristic(a.heuristicFunction);
        a.depth++;
        
        return a;
    }
    public Node moveRight (){
        Node a = deepClone(this);
        a.state.checks();
        
        a.state.coastGuard_y ++;
        if(a.state.coastGuard_y < 0 || a.state.coastGuard_y>a.state.m-1){
            return null;
        }
        a.operator+="right,";
        a.depth++;
        int pathCost = a.state.action();
        a.pathCost = pathCost; 
        a.state.calculateHeuristic(a.heuristicFunction);
        
        return a;
    }
    public Node pickUp(){
        Node a = deepClone(this);
        a.state.checks();
        if(a.state.onShip && !a.state.onWreck){
            if(a.state.guard.numberOfPassengers == a.state.guard.capacity){
                return null;
            }
            if(a.state.currentShip.numberOfPassengers ==0){
                return null;
            }
            a.state.guard.pickUp(a.state.currentShip);
            a.operator+="pickup,";
            int pathCost = a.state.action();
            a.pathCost = pathCost; 
            a.state.calculateHeuristic(a.heuristicFunction);
            a.depth++;
            return a;
        }
       
        return null;
    }
    public Node drop(){
        Node a = deepClone(this);
        a.state.checks();
        
        if(a.state.onStation && a.state.guard.numberOfPassengers!=0){
            a.state.passengersSaved += a.state.guard.numberOfPassengers;
            a.state.guard.numberOfPassengers =0;
            int pathCost = a.state.action();
            a.pathCost = pathCost; 
            a.state.calculateHeuristic(a.heuristicFunction);
            a.operator+="drop,";
            a.depth++;
            return a;
        }
        return null;
    }
    public Node reitreive(){
        Node a = deepClone(this);
        a.state.checks();
        
        if(a.state.onWreck && a.state.currentShip.isReitrivable){
            a.state.guard.reitreiveBox(a.state.currentShip);
            a.state.currentShip.isReitrieved = true;
            a.state.blackBoxesRetreived++;
             a.operator+="retrieve,";
             int pathCost = a.state.action();
             a.pathCost = pathCost; 
             a.state.calculateHeuristic(a.heuristicFunction);
            a.depth++;
            return a;
        }
        return null ;
    }
    public Node deepClone(Node n){
        State s = n.state.deepClone();
        return new Node(s,n.operator,n.depth,n.heuristicFunction,n.type);
    }

  

    @Override
    public int compareTo(Node b) {

        //If greedy compare only using the Heuristic cost
        
        if(this.type.contains("GR")){
        if(this.heuristicCost > b.heuristicCost){
            return -1;
        }else if(this.heuristicCost == b.heuristicCost){
            return 0;
        }
        else{
            return 1;
        }
        }
        //If A* add heuristic cost to path cost and compare using both
        else if(this.type.contains("AS")){
            if(this.heuristicCost+this.pathCost >= b.heuristicCost+b.pathCost){
                return 1;
            }
            else if(this.heuristicCost+this.pathCost == b.heuristicCost+b.pathCost){
                return 0;
            }
            else{
                return -1;
            }
        }
        //Else it's not greedy nor A star this function will not even be called just return 0
        else{
            return 0;
        }
        }


    
    
    
}
