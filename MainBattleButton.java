import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;


public class MainBattleButton {
    
    private String buttonID; //Determines button type / placement
    private ImageIcon nonSelectedIcon;
    private ImageIcon selectedIcon;
    private boolean isSelected;
    private int x; 
    private int y;
    private Rectangle bounds;
    
    public MainBattleButton(String nonSelectedImage, String selectedImage, String buttonID) {
        nonSelectedIcon = new ImageIcon(nonSelectedImage);
        selectedIcon = new ImageIcon(selectedImage);
        this.buttonID = buttonID;
        switch (buttonID) {
            //TODO assign proper values for specific buttons
            case "fight":
                this.x = 20; 
                this.y = 420; 
                this.isSelected = true; // default button
                break;
            case "act":
                this.x = 135; 
                this.y = 420; 
                this.isSelected = false;
                break;
            case "item":
                this.x = 248; 
                this.y = 420; 
                this.isSelected = false;
                break;
            case "mercy":
                this.x = 361; 
                this.y = 420; 
                this.isSelected = false;
                break;
        }
        this.isSelected = false;
        this.bounds = new Rectangle(x, y, 112, 45);
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public ImageIcon getNonSelectedImage() {
        return nonSelectedIcon;
    }

    public Rectangle getBounds() {
        return bounds;
    }   
    
    public ImageIcon getSelectedImage() {
        return selectedIcon;
    }

    public String getButtonID() {
        return buttonID;
    }
}
