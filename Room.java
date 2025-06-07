import java.lang.invoke.StringConcatException;
import java.util.ArrayList;

public class Room {
    private String filePath; // path to room image
    private String key;
    private boolean currentlyActive = false; // is this room currently active
    private ArrayList<Door> doors; // doors in this room

    public Room(String filePath, String key) {
        this.filePath = filePath;
        //TODO just make an imageIcon and return that 
        this.key = key;
        currentlyActive = false;
        doors = new ArrayList<Door>();
    }

    public String getImagePath() {
        return filePath;
    }

    public String getKey() {
        return key;
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void addDoors(ArrayList<Door> doors){
        this.doors = doors;
    }

    public void setActive(boolean active) {
        currentlyActive = active;
    }

    public boolean getActive() {
        return currentlyActive;
    }
}