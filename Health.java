import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Health {
    private Rectangle bounds;
    private int x;
    private int y;
    private int health;
    private ImageIcon image;

    public Health(int x, int y) {
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, 180, 45); // Adjust width and height as needed
        this.health = 10; // Default health value

        try {
            image = new ImageIcon("images/healthbar/healthbar-start.png"); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Image getImage(){
        return image.getImage(); // Return the current health bar image
    }

    public Rectangle getBounds() {
        bounds.setBounds(x,y,200,800);
        return bounds;
    }

    public void setHealth(int health) {
        this.health = health;
        updateIcon(); // Update the image whenever health changes
    }

    public int getHealth() {
        return health;
    }

    public void updateIcon() {
        // Determine the image file based on health
        String imagePath = "images/healthbar/healthbar-" + health + ".png";

        try {
            image = new ImageIcon(imagePath); // Load the new image
            System.out.println("Health bar updated to: " + imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Image getBar() {
        return image.getImage();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}