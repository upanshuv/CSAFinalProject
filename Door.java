import java.awt.Rectangle;

public class Door{

    private Rectangle bounds;
    private String key;
    private String sideOfRoom;

    public Door(String key, String sideOfRoom) {
        this.key = key;
        this.sideOfRoom = sideOfRoom;
        if (sideOfRoom.equals("right") || sideOfRoom.equals("left")) {
            this.bounds = new Rectangle(0, 0, 5, 400); // default size of door
        } else if (sideOfRoom.equals("up") || sideOfRoom.equals("down")) {
            this.bounds = new Rectangle(0, 0, 400, 5); // default size of door
        } else {
            throw new IllegalArgumentException("Invalid side of room: " + sideOfRoom);
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public String getSideOfRoom() {
        return sideOfRoom;
    }
    
    public String getKey() {
        return key;
    }

    public void setPosition(int x, int y) {
        bounds.setLocation(x, y); //this line of code provided by ChatGPT
    }

    public boolean hasCollided(GameObject other){
        try {
            if (this.getBounds().intersects(other.getBounds())){
                return true;
            }
            else{
                return false;
            }
                
        } catch (NullPointerException e) {
            return false;
        }
    }
    
    
}

