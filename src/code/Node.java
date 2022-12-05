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
        a.state.action();
        return a;
    }
    public Node moveUp (){
        Node a = deepClone(this);
        a.state.coastGuard_y++;
        a.state.action();
        return a;
    }
    public Node moveDown (){
        Node a = deepClone(this);
        a.state.coastGuard_y--;
        a.state.action();
        return a;
    }
    public Node moveRight (){
        Node a = deepClone(this);
        a.state.coastGuard_x++;
        a.state.action();
        return a;
    }
    public Node pickUp(){}
    public Node drop(){}
    public Node reitreive(){}
    public Node deepClone(Node n){
        return new Node(n.state,n.operator,n.depth,n.pathCost);
    }


    
    
    
}
