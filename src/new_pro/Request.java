package new_pro;
import enums_ECS.Direction;
public class Request {
	private int  sourceFloor;
	private int  destinationFloor;
	private Direction directiontoGo;
	public Request(int sourceFloor, int destinationFloor, Direction directiontoGo ) {
		this.sourceFloor = sourceFloor;
		this.destinationFloor = destinationFloor;
		this.directiontoGo = directiontoGo;
	}
	public int getSourceFloor() {
		return sourceFloor;
	}
	public void setSourceFloor(int sourceFloor) {
		this.sourceFloor = sourceFloor;
	}
	public int getDestinationFloor() {
		return destinationFloor;
	}
	public void setDestinationFloor(int destinationFloor) {
		this.destinationFloor = destinationFloor;
	}
	public Direction getDirection() {
		return directiontoGo;
	}
	public void setDirection(Direction directiontoGo) {
		this.directiontoGo = directiontoGo;
	}
}
