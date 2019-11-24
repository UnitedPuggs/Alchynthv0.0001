package floorgeneration;

import java.io.*;
public class NewDungeon {
    private int floorNumber, height, length;
    private boolean valid;
    public void createFloor(int y, int x) {
        this.height = y;
        this.length = x;
        NewFloor floor = null;
        do {
            try {
                floor = new NewFloor(y, x);
                valid = true;
            } catch(Exception e) {
                valid = false;
            }
        } while (!valid);
        WriteFile newFloor = new WriteFile("worlds/floor"); //prints the generated floor to a text file that is read to generate the floor layout
        try {
            newFloor.writeToFile(floor.toString());
        } catch (IOException e) {
            System.out.print(e);
            e.printStackTrace();
        }  
        floorNumber++;
    }

    public int getFloor() {
        return floorNumber;
    }

    public int getHeight() {
        return height + 6;
    }

    public int getLength() {
        return length + 6;
    }
}