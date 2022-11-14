package new_pro;
import java.util.*;  

import enums_ECS.ElevatorDirection;
import enums_ECS.Direction;

import new_pro.ElevatorControlSystem;
import new_pro.Request;
import new_pro.Elevator;


public class ElevatorSimulation {
    public static void main(String[] args) {
    	Scanner sc= new Scanner(System.in);  
    	//Define number of Elevators
    	System.out.print("Enter Number of Elevators- "); 
    	int numElevators=sc.nextInt();
        ElevatorControlSystem elevatorControlSystem = new ElevatorControlSystem(numElevators);
        ArrayList<Request> upComingRequest = new ArrayList();
        //Define Number of Requests
    	System.out.print("Enter Number of Requests- "); 
    	int numRequest=sc.nextInt();
        for(int i = 1; i <= numRequest; i++){
        	System.out.println("Request Number = "+i);
        	System.out.print("Enter Source Floor- "); 
        	int source= sc.nextInt();
        	System.out.print("Enter Destination Floor- ");  
        	int destination= sc.nextInt(); 
        	if(source<destination) {
        		Request temp= new Request(source,destination,Direction.UP);
        		elevatorControlSystem.pickUpRequest(temp);
        	}else{
        		Request temp= new Request(source,destination,Direction.DOWN);
        		elevatorControlSystem.pickUpRequest(temp);
        	}
        }
        System.out.println("*** Request Distribution ***");
        for(Elevator elevator : elevatorControlSystem.getElevators()){
            System.out.println("Elevator[" + elevator.getId() + "] - " + elevator.getTotalRequests());
        }
        System.out.println("------------------------");
        System.out.println("*** Stepping simulation ***");
        //Define Time Unit
        int t=0;
        //Start Control System
        while(elevatorControlSystem.getActiveRequests() > 0){
        	System.out.println("Time = "+t++);
        	elevatorControlSystem.step();
        	//Add New Request
        	System.out.println("Want to ADD Request press 1 else 0");
        	int request_accept= sc.nextInt();
        	if(request_accept==1) {
        		System.out.print("Enter Source Floor- "); 
            	int source= sc.nextInt();
            	System.out.print("Enter Destination Floor- ");  
            	int destination= sc.nextInt(); 
            	if(source<destination) {
            		Request temp= new Request(source,destination,Direction.UP);
            		elevatorControlSystem.pickUpRequest(temp);
            	}else{
            		Request temp= new Request(source,destination,Direction.DOWN);
            		elevatorControlSystem.pickUpRequest(temp);
            	}
        	}
            for(Elevator elevator : elevatorControlSystem.getElevators()){
            	//If elevator reached last Destination ie. 0 floor then accept pending request
            	if(elevator.getDirection()==ElevatorDirection.ELEVATOR_BASEMENT) {
	            	if(elevatorControlSystem.getpendingRequest().size()!=0) {
	            		Request req2=elevatorControlSystem.getpendingRequest().get(0);
	            		elevator.setElevatorDirection(ElevatorDirection.ELEVATOR_NONE);
	            		elevatorControlSystem.pickUpRequest(req2);
	            		elevatorControlSystem.getpendingRequest().remove(0);
	            	}
            	}          	
                System.out.println("Elevator[" + elevator.getId() + "] - Current Floor " + elevator.getCurrentFloor());
            }
        }
        System.out.println("------------------------");
        System.out.println("*** Final Output simulation ***");
        for(Elevator elevator : elevatorControlSystem.getElevators()) {
        	System.out.println("Elevator[" + elevator.getId() + "] - Current Floor " + elevator.getCurrentFloor());
        }
    }
}
