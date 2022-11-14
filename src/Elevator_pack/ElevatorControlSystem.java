package Elevator_pack;

import java.util.*;

import enums_Elevator.ElevatorDirection;
import enums_Elevator.Direction;
import enums_Elevator.State;
import enums_Elevator.ElevatorStatus;

import Elevator_pack.Elevator;
import Elevator_pack.Request;

public class ElevatorControlSystem { // make this class public 
	private static final int MAX_ELEVATORS = 16;
    private Elevator[] elevators;
    private final int elevatorCount;
    private ArrayList<Request> pendingRequest = new ArrayList();
    private int activeRequests;
    //Constructor initialize with all elevators with 0 start floor
    public ElevatorControlSystem(int elevatorCount){
        this.elevatorCount = elevatorCount > MAX_ELEVATORS ? MAX_ELEVATORS : elevatorCount;
        this.elevators = new Elevator[elevatorCount];
        for(int elevator = 0; elevator < elevatorCount; elevator++){
            elevators[elevator] = new Elevator(0, elevator);
        }
    }
    
    public ArrayList<Request> getpendingRequest(){
        return pendingRequest;
    }
    public Elevator[] getElevators(){
        return elevators;
    }

    public int getActiveRequests(){
        return activeRequests;
    }
    
    public boolean updateStatus(ElevatorStatus elevatorStatus, int elevatorId) {
        if(elevatorId < 0 || elevatorId > elevatorCount-1){
            return false;
        }
        return elevators[elevatorId].updateStatus(elevatorStatus);
    }
    
    //Request Elevator to go 0 Floor after request completion
    public void goBasementRequest(Elevator elevator) {
    	elevator.setElevatorDirection(ElevatorDirection.ELEVATOR_DOWN);
    	elevator.addDownDestination(0);
    	elevator.setElevatorDirection(ElevatorDirection.ELEVATOR_NONE);
    }
    
    // Take Request and Assign it to closest elevator
    public boolean pickUpRequest(Request req) {
        int maxRequestPerElevator = activeRequests/elevatorCount+1;
        
        int minUp = Integer.MAX_VALUE;
        int minDown = Integer.MAX_VALUE;
        Elevator minUpElevator = null;
        Elevator minDownElevator = null;
        
        int fromFloor = req.getSourceFloor();
        int toFloor = req.getDestinationFloor();
        Direction directiontoGo = req.getDirection();
        
        System.out.println("Request Sent from Floor-> " + fromFloor + " to Floor-> "+toFloor);
        //Define Parameter whether any Elevator is accepting request or not
        boolean pickbool = false;
        //Check Maintain Status of All Elevator
        boolean allMaintain = false;
        for(Elevator elevator : elevators){
            
            // Don't schedule anything for given under maintenance.
            if(elevator.getElevatorStatus() == ElevatorStatus.ELEVATOR_MAINTENANCE ){
                continue;
            }
            //An elevator is ready to service
            allMaintain = true;
            
            //If Request is not assigned to Elevator
            if(elevator.getDirection() == ElevatorDirection.ELEVATOR_NONE) {
    			System.out.println("Request Accepted "+ fromFloor+" -> " + toFloor + " by Elevator " + elevator.getId());

    			if(directiontoGo == Direction.UP) {
    				System.out.println("Request for Up");
    				elevator.addUpDestination(fromFloor);
        			elevator.addUpDestination(toFloor);
        			elevator.setElevatorDirection(ElevatorDirection.ELEVATOR_UP);
    			}else{
    				System.out.println("Request for Down");
    				elevator.setElevatorDirection(ElevatorDirection.ELEVATOR_UP);
    				elevator.addUpDestination(fromFloor);
        			elevator.addDownDestination(toFloor);
    			}		
    			activeRequests++;
    	        return true;
    		}
            
            //If elevator is going UP and directiontoGo is also UP
            else if(directiontoGo==Direction.UP && elevator.getDirection() == ElevatorDirection.ELEVATOR_UP ){
            	//An Elevator going up can pick this request
            	if(minUp > fromFloor - elevator.getCurrentFloor()){
            		minUp = fromFloor - elevator.getCurrentFloor();
                    minUpElevator = elevator;
                    pickbool=pickbool||true;      
            	}else{
            		//Can't pick. We Skip this elevator
            			pickbool=pickbool||false;
            		}
            }
          //If elevator is going DOWN and directiontoGo is also DOWN
            else if(directiontoGo==Direction.DOWN && elevator.getDirection() == ElevatorDirection.ELEVATOR_DOWN){
            	if(minDown > elevator.getCurrentFloor() - fromFloor){
            		minDown = elevator.getCurrentFloor() - fromFloor;
                    minDownElevator = elevator;
                    pickbool=pickbool||true;
                }else{
            			pickbool=pickbool||false;
            		}
            	}
        }// End of Elevator Loop 
        //Either Elevator minDown or MinUP is Valid.If we found only 1 closest elevator in down direction. Assign the pickUp requests to it.
        if(minUpElevator != null){
        	minUpElevator.addUpDestination(fromFloor);
        	minUpElevator.addUpDestination(toFloor);
        	minUpElevator.setElevatorDirection(ElevatorDirection.ELEVATOR_UP);
        }
        if(minDownElevator != null){
            minDownElevator.addDownDestination(fromFloor);
            minDownElevator.addDownDestination(toFloor);
            minDownElevator.setElevatorDirection(ElevatorDirection.ELEVATOR_DOWN);
        }
        //We could not allocate the request to any elevator. All elevators must be under maintenance. Return false.
        if(allMaintain == false){
            return false;
        }
        //All Active Elevators coudn't get this request. Add to pending List
        if(minDownElevator == null && minUpElevator== null){
        	System.out.println("Pending Request "+req.getSourceFloor()+"-> "+req.getDestinationFloor());
        	pendingRequest.add(req);
        	return false;
        }
        return true;   
}
    public void step(){
        //Loop though every elevator and call move
        for(Elevator currElevator : elevators) {
        	//If at Basement Proceed with other elevators
        	if(currElevator.getDirection()==ElevatorDirection.ELEVATOR_BASEMENT){
        		continue;
        	}
            if(currElevator.moveAndCheckIfServed()){
                	activeRequests--;
            }
        }   
    }
}



