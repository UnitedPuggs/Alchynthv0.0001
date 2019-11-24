package floorgeneration;
/*
 * Lasciate ogne speranza, voi ch'intrate
 */
import java.util.ArrayList;
import java.util.Arrays;
public class NewFloor {
    private Tile[][] floorMap;
    private int boundary;
    private int spawn1, spawn2;
    public NewFloor(int height, int length) {
        floorMap = new Tile[height][length];
        buildBorder();
        do 
        roomGen(height * length);
        while(roomCount() < 5);
        Pair<Integer, Integer> spawnCoords = spawnPoint();
        spawn1 = (int)spawnCoords.first();
        spawn2 = (int)spawnCoords.second();
        createPathway();
        cleanup();
    }

    private void buildBorder() { //fills the floormap with wall tiles, and creates borderes around the edges
        for(int i = 0; i < floorMap.length; i++) {
            for(int j = 0; j < floorMap[i].length; j++) {
                if(i < 1 || i == floorMap.length - 1 || j == 0 || j == floorMap[0].length - 1) 
                    floorMap[i][j] = new Tile(Type.WALL_TILE, 'b', i, j);
                else 
                    floorMap[i][j] = new Tile(Type.WALL_TILE, 'a', i, j);
            }
        }
    }

    private void roomGen(int floorSize) { //spawns random room tiles throughout the floormap and assigns them a size
        char[] layouts;
        boundary = 4;
        if(floorSize <= 625) {
            layouts = new char[]{'a', 'b', 'c', 'd', 'e', 'f'};
            boundary = 4;
        } else if(floorSize <= 2500) {
            layouts = new char[]{'a', 'b', 'c', 'd', 'e', 'f',
                'g', 'h', 'i', 'j', 'k', 'l'};
            boundary = 5;
        } else {
            layouts = new char[]{'g', 'h', 'i', 'j', 'k', 'l',
                'm', 'n', 'o', 'p', 'q', 'r'};
            boundary = 6;
        }
        for(int i = 0; i < 250; i++) {
            int c = 0;
            while(c < 5) {
                int rng1 = (int)(Math.random() * floorMap.length);
                int rng2 = (int)(Math.random() * floorMap[0].length);
                if(validRoomSpace(rng1, rng2)) {
                    floorMap[rng1][rng2].changeTo(Type.ROOM_TILE, layouts[(int)(Math.random() * layouts.length)]);
                    buildRoom(rng1, rng2);
                    break;
                }
                c++;
            }
        }
        doorGen();
    }

    private int roomCount() { //keeps track of how many rooms have generated to make sure there are enough
        int c = 0;
        for(int i = 0; i < floorMap.length; i++) {
            for(int j = 0; j < floorMap[0].length; j++) {
                if(floorMap[i][j].tileType() == Type.ROOM_TILE)
                    c++;
            }
        }
        return c;
    }

    private boolean validRoomSpace(int y, int x) { //makes sure that rooms will not overlap
        int extraSpace = 4;
        if(y - (boundary + extraSpace) < 0 || y + (boundary + extraSpace) >= floorMap.length || x - (boundary + extraSpace) < 0 || x + (boundary + extraSpace) >= floorMap[0].length)
            return false;
        for(int i = y - (boundary + extraSpace); i <= y + (boundary + extraSpace); i++) {
            for(int j = x - (boundary + extraSpace); j <= x + (boundary + extraSpace); j++) {
                if(floorMap[i][j].tileType() != Type.WALL_TILE)
                    return false;
            }
        }
        return true;
    }

    private void buildRoom(int spot1, int spot2) { //checks to see what size the room is and builds ground tiles around the room tile in an appropriate size
        switch(floorMap[spot1][spot2].subType()) {
            case 'a':
            for(int i = spot1 - 1; i <= spot1 + 2; i++) {
                for(int j = spot2 - 1; j <= spot2 + 2; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'b':
            for(int i = spot1 - 2; i <= spot1 + 2; i++) {
                for(int j = spot2 - 2; j <= spot2 + 2; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'c':
            for(int i = spot1 - 1; i <= spot1 + 2; i++) {
                for(int j = spot2 - 2; j <= spot2 + 2; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'd':
            for(int i = spot1 - 1; i <= spot1 + 2; i++) {
                for(int j = spot2 - 2; j <= spot2 + 3; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'e':
            for(int i = spot1 - 2; i <= spot1 + 2; i++) {
                for(int j = spot2 - 2; j <= spot2 + 3; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'f':
            for(int i = spot1 - 2; i <= spot1 + 2; i++) {
                for(int j = spot2 - 3; j <= spot2 + 3; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'g': 
            for(int i = spot1 - 2; i <= spot1 + 3; i++) {
                for(int j = spot2 - 2; j <= spot2 + 3; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'h':
            for(int i = spot1 - 3; i <= spot1 + 3; i++) {
                for(int j = spot2 - 3; j <= spot2 + 3; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'i':
            for(int i = spot1 - 2; i <= spot1 + 3; i++) {
                for(int j = spot2 - 3; j <= spot2 + 3; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'j':
            for(int i = spot1 - 2; i <= spot1 + 3; i++) {
                for(int j = spot2- 3; j <= spot2 + 4; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'k':
            for(int i = spot1 - 3; i <= spot1 + 3; i++) {
                for(int j = spot2 - 3; j <= spot2 + 4; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'l':
            for(int i = spot1 - 3; i <= spot1 + 3; i++) {
                for(int j = spot2 - 4; j <= spot2 + 4; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'm':
            for(int i = spot1 - 3; i <= spot1 + 4; i++) {
                for(int j = spot2 - 3; j <= spot2 + 4; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'n':
            for(int i = spot1 - 4; i <= spot1 + 4; i++) {
                for(int j = spot2 - 4; j <= spot2 + 4; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'o':
            for(int i = spot1 - 3; i <= spot1 + 4; i++) {
                for(int j = spot2 - 4; j <= spot2 + 4; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'p':
            for(int i = spot1 - 3; i <= spot1 + 4; i++) {
                for(int j = spot2 - 4; j <= spot2 + 5; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'q':
            for(int i = spot1 - 4; i <= spot1 + 4; i++) {
                for(int j = spot2 - 4; j <= spot2 + 5; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
            break;
            case 'r':
            for(int i = spot1 - 4; i <= spot1 + 4; i++) {
                for(int j = spot2 - 5; j <= spot2 + 5; j++) {
                    if(floorMap[i][j].tileType() != Type.ROOM_TILE)
                        floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
                }
            }
        }
    }
    
    private Pair spawnPoint() { //unused, ignore.
    	boolean valid = false;
    	int rng1, rng2;
    	do {
    		rng1 = (int)(Math.random() * floorMap.length);
    		rng2 = (int)(Math.random() * floorMap[0].length);
    		
    		if(floorMap[rng1][rng2].tileType() == Type.GROUND_TILE) {
    			valid = true;
    		}
    	} while(!valid);
    	return new Pair<Integer, Integer>(rng1, rng2);
    }

    private void doorGen() { //creates doorways for the paths to connect to
        for(int i = 0; i < floorMap.length; i++) {
            for(int j = 0; j < floorMap[0].length; j++) {
                if(floorMap[i][j].tileType() == Type.ROOM_TILE) {
                    floorMap[i][j].setSubType('a');
                }
            }
        }
        for(int i = 0; i < floorMap.length; i++) {
            for(int j = 0; j < floorMap[0].length; j++) {
                if(floorMap[i][j].tileType() == Type.ROOM_TILE) {
                    ArrayList<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));
                    while(floorMap[i][j].subType() != 'e') {
                        int rngDirection = (int)(Math.random() * directions.size());
                        Direction direction = directions.get(rngDirection);
                        directions.remove(rngDirection);
                        double rngAdditionalDoor = Math.random(); //1 room guaranteed, chance for more (up to 4)
                        switch(floorMap[i][j].subType()) {
                            case 'a':
                            if(findEdge(i, j, direction))
                                floorMap[i][j].setSubType('b');
                            break;
                            case 'b':
                            if(rngAdditionalDoor > 0.25) {
                                if(findEdge(i, j, direction))
                                    floorMap[i][j].setSubType('c');
                            } else
                                floorMap[i][j].setSubType('e');
                            break;
                            case 'c':
                            if(rngAdditionalDoor > 0.50) {
                                if(findEdge(i, j, direction))
                                    floorMap[i][j].setSubType('d');
                            } else
                                floorMap[i][j].setSubType('e');
                            break;
                            case 'd':
                            if(rngAdditionalDoor > 0.75) {
                                if(findEdge(i, j, direction))
                                    floorMap[i][j].setSubType('e');
                            } else
                                floorMap[i][j].setSubType('e');
                            break;
                        }
                        if(directions.isEmpty()) {
                            floorMap[i][j].setSubType('e');
                        }
                    }
                }
            }
        }
    }

    private boolean findEdge(int spot1, int spot2, Direction direction) { //checks to see where the door needs to be created
        Tile edge = null;
        int start = 0, end = 0;
        switch(direction) {
            case NORTH:
            for(int i = spot1; i >= spot1 - boundary; i--) {
                if(floorMap[i][spot2].tileType() == Type.WALL_TILE) {
                    edge = floorMap[i][spot2];
                    break;
                }
            }

            for(int i = 0; i <= boundary; i++) {
                if(newLocation(newLocation(edge.getLocation(), Direction.WEST, i).getLocation(), Direction.SOUTH, 1).tileType() == Type.WALL_TILE) { 
                    start = (int)edge.getLocation().second() - (i - 2);
                    break;
                }
            }
            for(int i = 0; i <= boundary; i++) {
                if(newLocation(newLocation(edge.getLocation(), Direction.EAST, i).getLocation(), Direction.SOUTH, 1).tileType() == Type.WALL_TILE) {
                    end = (int)edge.getLocation().second() + (i - 1);
                    break;
                }
            }
            edge = floorMap[(int)edge.getLocation().first()][(int)(Math.random() * (end - start) + start)];
            break;
            case EAST:
            for(int i = spot2; i <= spot2 + boundary; i++) {
                if(floorMap[spot1][i].tileType() == Type.WALL_TILE) {
                    edge = floorMap[spot1][i];
                    break;
                }
            }

            for(int i = 0; i <= boundary; i++) {
                if(newLocation(newLocation(edge.getLocation(), Direction.NORTH, i).getLocation(), Direction.WEST, 1).tileType() == Type.WALL_TILE) {
                    start = (int)edge.getLocation().first() - (i - 2);
                    break;
                }
            }
            for(int i = 0; i <= boundary; i++) {
                if(newLocation(newLocation(edge.getLocation(), Direction.SOUTH, i).getLocation(), Direction.WEST, 1).tileType() == Type.WALL_TILE) {
                    end = (int)edge.getLocation().first() + (i - 1);
                    break;
                }
            }
            edge = floorMap[(int)(Math.random() * (end - start) + start)][(int)edge.getLocation().second()];
            break;
            case SOUTH:
            for(int i = spot1; i <= spot1 + boundary; i++) {
                if(floorMap[i][spot2].tileType() == Type.WALL_TILE) {
                    edge = floorMap[i][spot2];
                    break;
                }
            }

            for(int i = 0; i <= boundary; i++) {
                if(newLocation(newLocation(edge.getLocation(), Direction.WEST, i).getLocation(), Direction.NORTH, 1).tileType() == Type.WALL_TILE) {
                    start = (int)edge.getLocation().second() - (i - 2);
                    break;
                }
            }
            for(int i = 0; i <= boundary; i++) {
                if(newLocation(newLocation(edge.getLocation(), Direction.EAST, i).getLocation(), Direction.NORTH, 1).tileType() == Type.WALL_TILE) {
                    end = (int)edge.getLocation().second() + (i - 1);
                    break;
                }
            }
            edge = floorMap[(int)edge.getLocation().first()][(int)(Math.random() * (end - start) + start)];
            break;
            case WEST:
            for(int i = spot2; i >= spot2 - boundary; i--) {
                if(floorMap[spot1][i].tileType() == Type.WALL_TILE) {
                    edge = floorMap[spot1][i];
                    break;
                }
            }

            for(int i = 0; i <= boundary; i++) {
                if(newLocation(newLocation(edge.getLocation(), Direction.NORTH, i).getLocation(), Direction.EAST, 1).tileType() == Type.WALL_TILE) {
                    start = (int)edge.getLocation().first() - (i - 2);
                    break;
                }
            }
            for(int i = 0; i <= boundary; i++) {
                if(newLocation(newLocation(edge.getLocation(), Direction.SOUTH, i).getLocation(), Direction.EAST, 1).tileType() == Type.WALL_TILE) {
                    end = (int)edge.getLocation().first() + (i - 1);
                    break;
                }
            }
            edge = floorMap[(int)(Math.random() * (end - start) + start)][(int)edge.getLocation().second()];
            break;
        }
        if(!borderValid((int)edge.getLocation().first(), (int)edge.getLocation().second(), direction))
            return false;
        edge.changeTo(Type.DEV_TILE, 'a');
        newLocation(edge.getLocation(), direction, 1).changeTo(Type.DEV_TILE, 'a');
        int connectorSpot1 = (int)newLocation(edge.getLocation(), direction, 2).getLocation().first(), 
        connectorSpot2 = (int)newLocation(edge.getLocation(), direction, 2).getLocation().second();
        floorMap[connectorSpot1][connectorSpot2]  = new Connector(connectorSpot1, connectorSpot2, floorMap[spot1][spot2], direction);
        return true;
    }

    private Tile newLocation(Pair refCoords, Direction direction, int spaces) { //used for referencing a location in the floormap relative to a starting location
        switch(direction) {
            case NORTH:
            return floorMap[(int)(refCoords.first()) - spaces][(int)(refCoords.second())];
            case EAST:
            return floorMap[(int)(refCoords.first())][(int)(refCoords.second()) + spaces];
            case SOUTH:
            return floorMap[(int)(refCoords.first()) + spaces][(int)(refCoords.second())];
            default:
            return floorMap[(int)(refCoords.first())][(int)(refCoords.second()) - spaces];
        }
    }

    private Tile newLocation(Pair refCoords, Direction direction1, Direction direction2, int spaces) { //same as before, for diagonal references
        if(direction1 == Direction.NORTH) {
            if(direction2 == Direction.EAST)
                return floorMap[(int)(refCoords.first()) - spaces][(int)(refCoords.second()) + 1];
            else
                return floorMap[(int)(refCoords.first()) - spaces][(int)(refCoords.second()) - 1];
        }
        else {
            if(direction2 == Direction.EAST)
                return floorMap[(int)(refCoords.first()) + spaces][(int)(refCoords.second()) + 1];
            else
                return floorMap[(int)(refCoords.first()) + spaces][(int)(refCoords.second()) - 1];
        }
    }

    private boolean borderValid(int spot1, int spot2, Direction direction) { //makes sure that a doorway and connected path do not interfere with other rooms or go past the border
        switch(direction) {
            case NORTH:
            if(spot1 - 3 < 0)
                return false;
            if(floorMap[spot1 - 1][spot2].tileType() != Type.WALL_TILE || floorMap[spot1 - 2][spot2].tileType() != Type.WALL_TILE || floorMap[spot1 - 3][spot2].tileType() != Type.WALL_TILE)
                return false;
            break;
            case EAST:
            if(spot2 + 3 >= floorMap[0].length)
                return false;
            if(floorMap[spot1][spot2 + 1].tileType() != Type.WALL_TILE || floorMap[spot1][spot2 + 2].tileType() != Type.WALL_TILE || floorMap[spot1][spot2 + 3].tileType() != Type.WALL_TILE)
                return false;
            break;
            case SOUTH:
            if(spot1 + 3 >= floorMap.length)
                return false;
            if(floorMap[spot1 + 1][spot2].tileType() != Type.WALL_TILE || floorMap[spot1 + 2][spot2].tileType() != Type.WALL_TILE || floorMap[spot1 + 3][spot2].tileType() != Type.WALL_TILE)
                return false;
            break;
            case WEST:
            if(spot2 - 3 < 0)
                return false;
            if(floorMap[spot1][spot2 - 1].tileType() != Type.WALL_TILE || floorMap[spot1][spot2 - 2].tileType() != Type.WALL_TILE || floorMap[spot1][spot2 - 3].tileType() != Type.WALL_TILE)
                return false;
            break;
        }
        return true;
    }

    private void createPathway() { //draws paths from the doorway of one room to the doorway of another
        for(int i = 0; i < floorMap.length; i++) {
            for(int j = 0; j < floorMap[0].length; j++) {
                if(floorMap[i][j].tileType() == Type.DEV_TILE && floorMap[i][j].subType() == 'b') {
                    Connector connector = (Connector)floorMap[i][j];
                    Connector nearestTile = findNearestConnector(connector);

                    Tile nextTile = connector;

                    Direction direction;
                    if(connector.getDirection() == Direction.EAST || connector.getDirection() == Direction.WEST) {
                        if((int)nextTile.getLocation().first() < (int)nearestTile.getLocation().first()) 
                            direction = Direction.SOUTH;
                        else 
                            direction = Direction.NORTH;
                        while((int)nextTile.getLocation().first() != (int)nearestTile.getLocation().first()) {
                            int k = 1,
                            nextSpot1 = (int)newLocation(nextTile.getLocation(), direction, k).getLocation().first(),
                            nextSpot2 = (int)newLocation(nextTile.getLocation(), direction, k).getLocation().second();
                            if(floorMap[nextSpot1][nextSpot2].tileType() != Type.DEV_TILE && !touching(floorMap[nextSpot1][nextSpot2], direction)) {
                                floorMap[nextSpot1][nextSpot2].changeTo(Type.DEV_TILE, 'a');
                                nextTile = floorMap[nextSpot1][nextSpot2];
                                k++;
                            } else
                                break;
                        }
                        if((int)nextTile.getLocation().second() < (int)nearestTile.getLocation().second()) 
                            direction = Direction.EAST;
                        else 
                            direction = Direction.WEST;
                        while((int)nextTile.getLocation().second() != (int)nearestTile.getLocation().second()) {
                            int k = 1,
                            nextSpot1 = (int)newLocation(nextTile.getLocation(), direction, k).getLocation().first(),
                            nextSpot2 = (int)newLocation(nextTile.getLocation(), direction, k).getLocation().second();
                            if(floorMap[nextSpot1][nextSpot2].tileType() != Type.DEV_TILE && !touching(floorMap[nextSpot1][nextSpot2], direction)) {
                                floorMap[nextSpot1][nextSpot2].changeTo(Type.DEV_TILE, 'a');
                                nextTile = floorMap[nextSpot1][nextSpot2];
                                k++;
                            } else
                                break;
                        }
                    } else {
                        if((int)nextTile.getLocation().second() < (int)nearestTile.getLocation().second()) 
                            direction = Direction.EAST;
                        else 
                            direction = Direction.WEST;
                        while((int)nextTile.getLocation().second() != (int)nearestTile.getLocation().second()) {
                            int k = 1,
                            nextSpot1 = (int)newLocation(nextTile.getLocation(), direction, k).getLocation().first(),
                            nextSpot2 = (int)newLocation(nextTile.getLocation(), direction, k).getLocation().second();
                            if(floorMap[nextSpot1][nextSpot2].tileType() != Type.DEV_TILE && !touching(floorMap[nextSpot1][nextSpot2],direction)) {
                                floorMap[nextSpot1][nextSpot2].changeTo(Type.DEV_TILE, 'a');
                                nextTile = floorMap[nextSpot1][nextSpot2];
                                k++;
                            } else
                                break;
                        }
                        if((int)nextTile.getLocation().first() < (int)nearestTile.getLocation().first()) 
                            direction = Direction.SOUTH;
                        else 
                            direction = Direction.NORTH;
                        while((int)nextTile.getLocation().first() != (int)nearestTile.getLocation().first()) {
                            int k = 1,
                            nextSpot1 = (int)newLocation(nextTile.getLocation(), direction, k).getLocation().first(),
                            nextSpot2 = (int)newLocation(nextTile.getLocation(), direction, k).getLocation().second();

                            if(floorMap[nextSpot1][nextSpot2].tileType() != Type.DEV_TILE && !touching(floorMap[nextSpot1][nextSpot2], direction)) {
                                floorMap[nextSpot1][nextSpot2].changeTo(Type.DEV_TILE, 'a');
                                nextTile = floorMap[nextSpot1][nextSpot2];
                                k++;
                            } else
                                break;
                        }
                    }
                }
            }
        }
    }

    private boolean touching(Tile check, Direction direction) { //the great wall of tile
        if((newLocation(check.getLocation(), Direction.NORTH, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.EAST, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.NORTH, Direction.EAST, 1).tileType() != Type.WALL_TILE) ||
        (newLocation(check.getLocation(), Direction.NORTH, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.WEST, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.NORTH, Direction.WEST, 1).tileType() != Type.WALL_TILE) ||
        (newLocation(check.getLocation(), Direction.EAST, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.NORTH, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.NORTH, Direction.EAST, 1).tileType() != Type.WALL_TILE) ||
        (newLocation(check.getLocation(), Direction.EAST, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.SOUTH, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.SOUTH, Direction.EAST, 1).tileType() != Type.WALL_TILE) ||
        (newLocation(check.getLocation(), Direction.SOUTH, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.EAST, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.SOUTH, Direction.EAST, 1).tileType() != Type.WALL_TILE) ||
        (newLocation(check.getLocation(), Direction.SOUTH, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.WEST, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.SOUTH, Direction.WEST, 1).tileType() != Type.WALL_TILE) ||
        (newLocation(check.getLocation(), Direction.WEST, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.NORTH, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.NORTH, Direction.WEST, 1).tileType() != Type.WALL_TILE) ||
        (newLocation(check.getLocation(), Direction.WEST, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.SOUTH, 1).tileType() != Type.WALL_TILE &&
            newLocation(check.getLocation(), Direction.SOUTH, Direction.WEST, 1).tileType() != Type.WALL_TILE))
            return true;
        return false;
    }

    private Connector findNearestConnector(Connector startTile) { //used for finding which rooms the paths should connect
        ArrayList<Pair<Double, Connector>> distances = new ArrayList<Pair<Double, Connector>>();
        for(int i = 0; i < floorMap.length; i++) {
            for(int j = 0; j < floorMap[0].length; j++) {
                if(floorMap[i][j].tileType() == Type.DEV_TILE && floorMap[i][j].subType() == 'b') {
                    Connector connector = (Connector)floorMap[i][j];
                    if(connector.connectedRoom() != startTile.connectedRoom())
                        distances.add(new Pair<Double, Connector>(Math.sqrt(Math.pow((int)startTile.getLocation().second() - j, 2) +  (Math.pow((int)startTile.getLocation().first() - i, 2))), connector));
                }
            }
        }
        int closest = 0;
        for(int i = 0; i < distances.size(); i++) {
            if(distances.get(i).first() < distances.get(closest).first())
                closest = i;
        }
        return distances.get(closest).second();
    }

    private void cleanup() { //converts all tiles to codes that are readable by the map drawing program
        for(int i = 0; i < floorMap.length; i++) {
            for(int j = 0; j < floorMap[0].length; j++) {
                if(floorMap[i][j].tileType() == Type.WALL_TILE && floorMap[i][j].subType() == 'b')
                    floorMap[i][j].setSubType('a');
                else if(floorMap[i][j].tileType() == Type.ROOM_TILE || (floorMap[i][j].tileType() == Type.DEV_TILE && (floorMap[i][j].subType() == 'a' || floorMap[i][j].subType() == 'b')))
                    floorMap[i][j].changeTo(Type.GROUND_TILE, 'a');
            }
        }
        removeDeadEnds();
    }

    private void removeDeadEnds() { //clears any dead ends left over after the pathing (temp)
        boolean done = true;
        for(int i = 0; i < floorMap.length; i++) {
            for(int j = 0; j < floorMap[0].length; j++) {
                if(floorMap[i][j].tileType() == Type.GROUND_TILE)
                    if(!touchingTwo(floorMap[i][j])) {
                        floorMap[i][j].changeTo(Type.WALL_TILE, 'a');
                        done = false;
                    }
            }
        }
        if(!done)
            removeDeadEnds();
    }

    private boolean touchingTwo(Tile refTile) { //used to ensure paths remain 1x1 tunnels
        int c = 0;
        if(newLocation(refTile.getLocation(), Direction.NORTH, 1).tileType() != Type.WALL_TILE)
            c++;
        if(newLocation(refTile.getLocation(), Direction.EAST, 1).tileType() != Type.WALL_TILE)
            c++;
        if(newLocation(refTile.getLocation(), Direction.SOUTH, 1).tileType() != Type.WALL_TILE)
            c++;
        if(newLocation(refTile.getLocation(), Direction.WEST, 1).tileType() != Type.WALL_TILE)
            c++;
        if(c < 2)
            return false;
        return true;
    }

    public String toString() { //adds 3 wall tiles on all sides and gives the final map of codes for the reader
        String output = "";
        for(int i = 0; i < 3; i++) {
            if(i > 0)
                output = output + "\n";
            for(int j = 0; j < floorMap[0].length + 6; j++) {
                output = output + "1a ";
            }
        }
        for(int i = 0; i < floorMap.length; i++) {
            output = output + "\n";
            output = output + "1a 1a 1a ";
            for(int j = 0; j < floorMap[0].length; j++) {
                output = output + floorMap[i][j] + " ";
            }
            output = output + "1a 1a 1a";
        }
        for(int i = 0; i < 3; i++) {
            output = output + "\n";
            for(int j = 0; j < floorMap[0].length + 6; j++) {
                output = output + "1a ";
            }
        }
        output = output + "\n" + spawn1 + "\n" + spawn2;
        return output;
    }
}