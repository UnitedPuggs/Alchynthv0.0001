package floorgeneration;

import floorgeneration.Direction;
import floorgeneration.Tile;
import floorgeneration.Type;

public class Connector extends Tile { //This is the tile that the pathing detects to connect the rooms together
    private Tile connectedRoom;
    private boolean isConnected = false;
    private Direction direction;
    
    public Connector(int y, int x, Tile connectedRoom, Direction direction) {
        super(Type.DEV_TILE, 'b', y, x);
        this.connectedRoom = connectedRoom;
        this.direction = direction;
    }
    
    public Tile connectedRoom() {
        return connectedRoom;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public void connected() {
        isConnected = true;
    }
    
    public boolean isConnected() {
        return isConnected;
    }
}