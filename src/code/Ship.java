package code;
 //TODO:Understand wether to mark black box as retreived and ship should be removed or not

import java.util.Random;
public class Ship {
    int numberOfPassengers;
    boolean isWreck;
    boolean isReitrivable;
    int blackBoxDamage;
   

    public Ship(){
        Random rand = new Random();
        this.isWreck = false;
        this.isReitrivable = true;
        this.blackBoxDamage=0;
        this.numberOfPassengers = rand.nextInt(1,101);
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
    boolean isReitrivable(){
        return isReitrivable;
    }
    
}
