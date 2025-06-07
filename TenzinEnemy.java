import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class TenzinEnemy {

    private ArrayList<ImageIcon> idleImages;
    //have these be set to constants in the constructor (determine best location)
    private Rectangle bounds;
    private int x;
    private int y;
    private int timerCount;
    private int i;

    
    public TenzinEnemy() {
        x = 100;
        y = 100;
        i = 0;
        timerCount = 0;
        bounds = new Rectangle(x, y, 50, 50); // default size of enemy
        idleImages = new ArrayList<>();
        idleImages.add(new ImageIcon("./images/Enemies/TenzinEnemy/Idle/Idle0.png"));
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void update(){
        timerCount++;
        if (timerCount % 10 == 0) {
            // Update the image every 10 frames
            // For now, just keep it as the first image
            if (i < idleImages.size() - 1) {
                i++;
            } else {
                i = 0; // Loop back to the first image
            }
        }
    }

    public ImageIcon getImage() {
        return idleImages.get(0); // Return the first image for now
    }


    
}
