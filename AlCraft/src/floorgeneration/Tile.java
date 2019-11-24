
package floorgeneration;

public class Tile {
    private Type tileType;
    private char subType;
    private Pair location;
    
    public Tile(Type tileType, char subType, int y, int x) {
        this.tileType = tileType;
        this.subType = subType;
        this.location = new Pair<Integer, Integer>(y, x);
    }
    
    public Type tileType() {
        return tileType;
    }
    
    public char subType() {
        return subType;
    }
    
    public Pair getLocation() {
        return location;
    }
    
    public void changeTo(Type tileType, char subType) {
        this.tileType = tileType;
        this.subType = subType;
    }
    
    public void setSubType(char subType) {
        this.subType = subType;
    }
    
    public String toString() {
        return Integer.toString(tileType.ordinal() - Type.GROUND_TILE.ordinal()) + subType;
    }
}