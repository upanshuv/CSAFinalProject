import java.awt.Rectangle;

public class LesserButton {
    
    private String name;
    // Initialized mantually in enemy constructor
    private Rectangle bounds;
    private boolean isPressed;

    public LesserButton(String name, int x, int y) {
        this.name = name;
        this.bounds = new Rectangle(x, y, 15 * name.length(), 50); // Assuming 15 pixels per character
        this.isPressed = false;
    }

    public String getName() {
        return name;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        this.isPressed = pressed;
    }
}
