package Elevator_pack;

import java.util.Comparator;
import java.util.TreeSet;

import enums_Elevator.ElevatorStatus;
import enums_Elevator.ElevatorDirection;

public class Elevator {
	private final Integer id;
    private Integer currentFloor;
    //TreeSet with save the sorted order of Requests.
    private TreeSet<Integer> upDestinationFloors;
    private TreeSet<Integer> downDestinationFloors;
    private ElevatorStatus elevatorStatus;
    ElevatorDirection direction;

    public Elevator(Integer currentFloor, Integer id) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.upDestinationFloors = new TreeSet<Integer>();
        this.downDestinationFloors = new TreeSet<Integer>(new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        this.elevatorStatus = ElevatorStatus.ELEVATOR_FUNCTIONAL;
        direction = ElevatorDirection.ELEVATOR_NONE;
        System.out.println("Elevator "+this.id+" start with "+this.currentFloor+" in "+this.direction);
    }
    public ElevatorDirection getDirection(){
        return this.direction;
    }    
    public ElevatorStatus getElevatorStatus(){
        return this.elevatorStatus;
    }
    public int getId(){
        return this.id;
    }
    public int getCurrentFloor(){
        return this.currentFloor;
    }
    public int getTotalRequests(){
        return upDestinationFloors.size() + downDestinationFloors.size();
    }
    public void setElevatorDirection(ElevatorDirection directiontoGo) {
    	this.direction= directiontoGo;
    }
    public boolean updateStatus(ElevatorStatus elevatorStatus){
        //Cannot update status for an elevator which is actively serving requests
        if(getTotalRequests() > 0){
            return false;
        }
        this.elevatorStatus = elevatorStatus;
        return true;
    }
    public int getNextDestionationFloor(){
        if(direction == ElevatorDirection.ELEVATOR_DOWN){
            return this.downDestinationFloors.first();
        }else if(direction == ElevatorDirection.ELEVATOR_UP){
            return this.upDestinationFloors.first();
        }else{
            return 0;
        }
    }
    public void addUpDestination(Integer destination) {
    	upDestinationFloors.add(destination);
    }
    public void addDownDestination(Integer destination) {
    	downDestinationFloors.add(destination);
    }
    private void popUpDestionation() {
        upDestinationFloors.remove(upDestinationFloors.first());
        //If All the up destination Floors are empty.Assign NONE Direction
        if (upDestinationFloors.size() == 0){
            direction = ElevatorDirection.ELEVATOR_NONE;
            System.out.println("Elevator "+id+" UP tree set becomes Empty.");
        } 
    }
    private void popDownDestionation() {
        downDestinationFloors.remove(downDestinationFloors.first());
        if(downDestinationFloors.size() == 0){
        	//If All the Down destination Floors are empty.Assign NONE Direction
        	System.out.println("Elevator "+id+" DOWN tree set becomes Empty.");
            direction = ElevatorDirection.ELEVATOR_NONE;
        }
    }
    
    public boolean moveAndCheckIfServed() {
        if(direction == ElevatorDirection.ELEVATOR_UP){
        	//If Elevator going UP and reaches desired (Source/Destination) floor. 
            if(upDestinationFloors.first() == currentFloor){
            	System.out.println("Elevator "+id+" reached UP desired floor.");
                popUpDestionation();
                //If UP request are fulfilled then assign Down Direction
                if (upDestinationFloors.size() == 0 && downDestinationFloors.size()!=0) {
                    direction = ElevatorDirection.ELEVATOR_DOWN;
                    System.out.println("Elevator "+id+" going UP will now serve DOWN request");
                }
            	}else{
            		currentFloor++;
            }
        }if(direction == ElevatorDirection.ELEVATOR_DOWN){
        	//If Elevator going DOWN and reaches desired (Source/Destination) floor. 
            if(downDestinationFloors.first() == currentFloor){
                popDownDestionation();
                //If DOWN request are fulfilled then assign UP Direction
                if (upDestinationFloors.size() != 0 && downDestinationFloors.size()==0) {
                    direction = ElevatorDirection.ELEVATOR_UP;
                    System.out.println("Elevator "+id+" going DOWN will now serve UP request");
                }
            }else{
                currentFloor--;  
            }
        }if(direction == ElevatorDirection.ELEVATOR_NONE){
            //Do Nothing. Elevator is not moving.
        	//Move Elevator to Ground If request are fulfilled
        	if(currentFloor !=0) {
        		direction = ElevatorDirection.ELEVATOR_DOWN;
        		downDestinationFloors.add(0);
        		return false;
        	}
        		System.out.println("Request fulfilled reached 0th floor by Elevator "+id);
            	direction=ElevatorDirection.ELEVATOR_BASEMENT;
            	return true;

        }
        return false;
    }    
}
