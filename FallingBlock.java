import java.awt.Rectangle;

public class FallingBlock {
    
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private Rectangle bounds;
    private boolean active;

    public FallingBlock(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        bounds = new Rectangle(x, y, width, height);
        active = false; 
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        return active;
    }

    public void update() {
        y += speed; // Move the block down by its speed
        bounds = new Rectangle(x, y, width, height); // Update bounds
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
