import java.awt.Rectangle;

public class EnemyTrigger {

    Rectangle bounds;
    int x;
    int y;

    public EnemyTrigger(int x, int y) {
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, 50, 50); 
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean hasCollided(GameObject other) {
        try {
            if (this.getBounds().intersects(other.getBounds())) {
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }
}
