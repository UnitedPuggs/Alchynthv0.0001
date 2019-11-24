package floorgeneration;

public class Pair<First, Second> {
    private First first;
    private Second second;
    
    public Pair(First first, Second second) {
        this.first = first;
        this.second = second;
    }
    
    public First first() {
        return first;
    }
    
    public Second second() {
        return second;
    }
}