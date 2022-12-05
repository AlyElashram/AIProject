package code;

public class Ship {
    int numberOfPassengers;
    boolean isWreck;
    boolean isReitrivable;
    boolean isReitrieved;
    int blackBoxDamage;
    int locationX;
    int locationY;
   

    public Ship(int numberOfPassengers,int locx,int locy){
        this.locationX = locx;
        this.locationY = locy;
        this.isWreck = false;
        this.isReitrivable = true;
        this.blackBoxDamage=0;
        this.numberOfPassengers = numberOfPassengers;
    }

    void reitreiveBox(){
        this.isReitrieved = true;
    }


    void action(){
        //
        if(this.blackBoxDamage == 20){
            this.isReitrivable = false;
        }
        else if(numberOfPassengers == 0){
            this.isWreck = true;
            this.blackBoxDamage ++;
        }else{
            this.numberOfPassengers --;
        }
    }
    int pickUpPassengers(int numberPickUp){
        if(numberPickUp >= this.numberOfPassengers){
            this.numberOfPassengers = 0;
            return numberPickUp;
        }
        
        this.numberOfPassengers -= numberOfPassengers;
        return numberPickUp;
    }

    
}
