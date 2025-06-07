
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Mercy {
    private Rectangle bounds;
    private int x;
    private int y;
    private int mercy;
    private ImageIcon image;

    public Mercy(int x, int y) {
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, 180, 45); // Adjust width and height as needed
        this.mercy = 5; // Default health value

        try {
            image = new ImageIcon("images/mercybar/mercybar-start.png"); 
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

    public void setMercy(int mercy) {
        this.mercy = mercy;
        updateIcon(); // Update the image whenever health changes
    }

    public int getmercy() {
        return mercy;
    }

    public void updateIcon() {
        // Determine the image file based on health
        
        String imagePath = "images/mercybar/mercybar-" + mercy + ".png";

        try {
            image = new ImageIcon(imagePath); // Load the new image
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

