package code;


public class Node {
    State state;
    String operator;
    int depth;
    int pathCost;

    public Node(State state,String oper,int dep,int cost){
        this.state = state;
        this.operator = oper;
        this.depth = dep;
        this.pathCost = cost;
    }

    public Node moveLeft (){
        Node a = deepClone(this);
        a.state.coastGuard_x--;
        if(a.state.coastGuard_x < 0 || a.state.coastGuard_x>a.state.m){
            return null;
        }
        a.operator+=",left";
        a.state.action();
        
        return a;
    }
    public Node moveUp (){
        Node a = deepClone(this);
        a.state.coastGuard_y--;
        if(a.state.coastGuard_y < 0 || a.state.coastGuard_x>a.state.n){
            return null;
        }
        a.operator+=",up";
        a.state.action();
        
        return a;
    }
    public Node moveDown (){
        Node a = deepClone(this);
        a.state.coastGuard_y++;
        if(a.state.coastGuard_y < 0 || a.state.coastGuard_x>a.state.n){
            return null;
        }
        a.operator+=",down";
        a.state.action();
        
        return a;
    }
    public Node moveRight (){
        Node a = deepClone(this);
        a.state.coastGuard_x +=1;
   
        if(a.state.coastGuard_x < 0 || a.state.coastGuard_x>a.state.m){
            System.out.println("Returned null");
            return null;
        }
        a.operator+=",right";
        a.depth++;
        a.state.action();
        return a;
    }
    public Node pickUp(){
        Node a = deepClone(this);
        if(a.state.onShip){
            a.state.guard.pickUp(state.currentShip);
            a.operator+=",pickup";
            a.state.action();
            return a;
        }
       
        return null;
    }
    public Node drop(){
        Node a = deepClone(this);
        if(a.state.onStation){
            a.state.passengersSaved += a.state.guard.numberOfPassengers;
            a.state.guard.numberOfPassengers =0;
            a.state.action();
            a.operator+=",drop";
            return a;
        }
        return null;
    }
    public Node reitreive(){
        Node a = deepClone(this);
        if(a.state.onShip && a.state.currentShip.isReitrivable){
            a.state.guard.reitreiveBox(state.currentShip);
            state.currentShip.isReitrieved = true;
            a.state.blackBoxesRetreived++;
             a.operator+=",reitreive";
            a.state.action();
            return a;
        }
        return null ;
    }
    public Node deepClone(Node n){
        State s = n.state.deepClone();
        return new Node(s,n.operator,n.depth,n.pathCost);
    }


    
    
    
}
