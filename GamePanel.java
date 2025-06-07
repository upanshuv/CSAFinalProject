import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {

    private BufferedImage background;
    private Hero hero;
    private int secondsCount = 0;
    private Door rightEnteranceToClassRoom;
    private int panelHeight;
    private int panelWidth;
    private int frameCount;
    private int fightAnimationIterator;
    private ArrayList<Room> rooms;
    private ArrayList<EnemyTrigger> enemyTriggers;
    private String[][] map;
    private boolean triggersPlaced;
    private boolean battleActive;
    private MainBattleButton fightButton;
    private MainBattleButton itemButton;
    private MainBattleButton actButton;
    private MainBattleButton mercyButton;
    private ArrayList<String> genericEnemies;
    private int randomEnemyInt; // will be used to select a random enemy from the genericEnemies list
    private int activeEnemyHealth;
    private boolean fightAnimationActive;
    private ArrayList<ImageIcon> fightAnimation;
    private Boolean turn; // true for player, false for enemy
    private boolean displayDamageAnimation;
    private int damageAnimationCounter;
    private TenzinEnemy tenzinEnemy; 
    private boolean centerSoul; // used to center the soul in the middle of the screen when enemy turn starts
    private int heroXBeforeBattle;
    private int heroYBeforeBattle; 
    private String enemyClassName;
    private ArrayList<String> attacks; 
    private String activeAttack;
    private ArrayList<FallingBlock> fallingBlocks; // used to store falling blocks for the "fallingBlocks" attack
    private Health playerHealth;
    private Health enemyHealth;
    private Mercy mercyBar;
    private ArrayList<FallingBlock> fallingBlocksAtDifferentSpeeds; // used to store falling blocks for the "fallingBlocksAtDifferentSpeeds" attack
    private int renderableBlocks;
    private Room activeRoom;
    //player will be invincible until this frame reached
    private int invincibilityFrame;
    private boolean tookDamageThisFrame;
    private int mercyLevel;

    public GamePanel() {

        mercyLevel = 0; // used to track mercy level, increased by ACTing, enemy mercyable when level >= 5
        tookDamageThisFrame = false;
        invincibilityFrame = 0; // player will be invincible for 60 frames after taking damage
        frameCount = 0;
        activeEnemyHealth = 0;
        fightAnimationIterator = 0;
        turn = true;
        heroXBeforeBattle = 0;
        heroYBeforeBattle = 0;
        renderableBlocks = 0;
        centerSoul = false; // used to center the soul in the middle of the screen when enemy turn starts
        
        activeRoom = null;

        //INIT ATTACKS //

        attacks = new ArrayList<String>();
        attacks.add("fallingBlocks");
        attacks.add("fallingBlocksAtDifferentSpeeds");
        activeAttack = "";

        fallingBlocks = new ArrayList<FallingBlock>();
        for (int i = 0; i < 10; i++) {
            int x = (70 * i) + (int)(Math.random() * 10);
            int y = 0; // start at the top
            int width = 30, height = 30, speed = 8;
            fallingBlocks.add(new FallingBlock(x, y, width, height, speed));                         
        }

        fallingBlocksAtDifferentSpeeds = new ArrayList<FallingBlock>();
        for (int i = 0; i < 10; i++) {
            int x = 100 * i;
            int y = 0; 
            int width = 30, height = 30;
            int speed = (int) (Math.random() * 10 + 5); // random speed between 1 and 5
            fallingBlocksAtDifferentSpeeds.add(new FallingBlock(x, y, width, height, speed));
        }

        //INIT ANIMATIONS //

        fightAnimation = new ArrayList<ImageIcon>();
        for (int i = 0; i < 5; i++) {
            String imagePath = "./images/animations/fightAnimations/fightAnimation" + i + ".png";
            URL imageURL = getClass().getResource(imagePath);
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                fightAnimation.add(icon);
            } else {
                System.err.println("Image not found: " + imagePath);
            }
        }

        displayDamageAnimation = false; // used to display damage animation
        damageAnimationCounter = 0; // used to control damage animation rendering
        fightAnimationActive = false; // used to control fight animation rendering

        //INIT ANIMATIONS END //

        // INIT DOORS AND ROOMS //

        map = new String[][]{ // only used for visualization
            {"Mathroom"  ,  "JacobRoom"},
            {"RedFloorRoom","TileRoom", "ClassRoom"}, 
                        {"UpanshuRoom",}
        };

        rooms = new ArrayList<Room>();
        rooms.add(new Room("./images/background/gray_tile_background.png", "TileRoom"));
        rooms.add(new Room("./images/background/ClassRoom.jpg", "ClassRoom"));
        rooms.add(new Room("./images/background/UpanshuRoom.jpg", "UpanshuRoom"));
        rooms.add(new Room("./images/background/JacobRoom.jpg", "JacobRoom"));
        rooms.add(new Room("./images/background/RedFloorRoom.png", "RedFloorRoom"));

        for (Room room : rooms) {
            ArrayList<Door> roomDoors = new ArrayList<Door>();
            if (room.getKey().equals("TileRoom")) {
                roomDoors.add(new Door("ClassRoom", "right"));
                roomDoors.add(new Door("UpanshuRoom", "down"));
                roomDoors.add(new Door("JacobRoom", "up"));
                roomDoors.add(new Door("RedFloorRoom", "left"));
                room.setActive(true);
            } else if (room.getKey().equals("ClassRoom")) {
                roomDoors.add(new Door("TileRoom", "left"));
            } else if (room.getKey().equals("UpanshuRoom")) {
                roomDoors.add(new Door("TileRoom", "up"));
            } else if (room.getKey().equals("JacobRoom")) {
                roomDoors.add(new Door("TileRoom", "down"));
            } else if (room.getKey().equals("RedFloorRoom")) {
                roomDoors.add(new Door("TileRoom", "right"));
            }
            room.addDoors(roomDoors);
        }


        // INIT DOORS AND ROOMS END //

        //INIT BUTTONS//

        //TODO implement button images
        fightButton = new MainBattleButton("./images/buttons/mainBattleButtons/fightUnselected.png", "./images/buttons/mainBattleButtons/fightSelected.png", "fight");
        fightButton.setSelected(true);
        itemButton = new MainBattleButton("./images/buttons/mainBattleButtons/itemUnselected.png", "./images/buttons/mainBattleButtons/itemSelected.png", "item");
        actButton = new MainBattleButton("./images/buttons/mainBattleButtons/actUnselected.png", "./images/buttons/mainBattleButtons/actSelected.png", "act");
        mercyButton = new MainBattleButton("./images/buttons/mainBattleButtons/mercyUnselected.png", "./images/buttons/mainBattleButtons/mercySelected.png", "mercy");
        //INIT BUTTONS END//

        // INIT ENEMY TRIGGERS //

        triggersPlaced = false;
        enemyTriggers = new ArrayList<EnemyTrigger>();
        for (int i = 0; i < 10; i++) {
            enemyTriggers.add(new EnemyTrigger(9999, 9999));
        }

        battleActive = false;
        fightAnimationActive = false;

        this.setLayout(null);
        hero = new Hero(100, 100);

        URL imageURL = getClass().getResource("./images/background/gray_tile_background.png");

        //INIT ENEMY TRIGGERS END //

        try {
            background = ImageIO.read(imageURL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        hero.setVisible(true);

        Timer gameLoop = new Timer(17, this);
        gameLoop.start();

        this.setFocusable(true);
        this.requestFocusInWindow();

        // INIT GENERIC ENEMIES // 

        tenzinEnemy = new TenzinEnemy();

        genericEnemies = new ArrayList<String>();
        genericEnemies.add("TenzinEnemy");
        //TODO add more enemies here

        // INIT DISPLAY //
        //textDihsplay = new Display(1);
        //textDihsplay.setBounds(0, 0, 600, 100);//mess with in old thing
        //this.add(textDihsplay);
        //textDihsplay.setVisible(false);
        // END INIT DISPLAY //

        // INIT BARS //
        playerHealth = new Health(0, 0);
        playerHealth.setHealth(10); // set initial health to 10
        //this.add(playerHealth);
        //playerHealth.setVisible(false);

        enemyHealth = new Health(320, 0);
        //this.add(enemyHealth);
        //enemyHealth.setVisible(false);

        mercyBar = new Mercy(0, 45);
        // END INIT BARS //

        // keylistener here //
        this.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (!battleActive || (battleActive && !turn)) {
                    switch (code) {
                        case KeyEvent.VK_UP:
    
                            hero.setDy(-5);
                            hero.setDx(0);
                            hero.setDirection(Direction.UP);
                            
                            
                            break;
                        case KeyEvent.VK_LEFT:
                        
                                hero.setDy(0);
                                hero.setDx(-5);
                                hero.setDirection(Direction.LEFT);
                            
                            break;
                        case KeyEvent.VK_DOWN:
                            
                                hero.setDy(5);
                                hero.setDx(0);
                                hero.setDirection(Direction.DOWN);
                            
                            break;
                        case KeyEvent.VK_RIGHT:
                            
                                hero.setDy(0);
                                hero.setDx(5);
                                hero.setDirection(Direction.RIGHT);
                            
                            break;
                    }
                }

                else if (battleActive && turn) {
                    switch (code) {
                        case KeyEvent.VK_Z:
                            if (fightButton.isSelected() && !fightAnimationActive && turn) {
                                doFIGHT();
                            } else if (actButton.isSelected()) {
                                doACT();
                                System.out.println("Act");
                            } else if (itemButton.isSelected()) {
                                System.out.println("Item");
                            } else if (mercyButton.isSelected()) {
                                doMercy();
                                System.out.println("Mercy");
                            }
                            break;
                        case KeyEvent.VK_UP:
                            hero.setDy(-5);
                            hero.setDx(0);
                            break;
                        case KeyEvent.VK_LEFT:
                            hero.setDx(-5);
                            hero.setDy(0);

                            if (battleActive && turn){
                                if (fightButton.isSelected()) {
                                    fightButton.setSelected(false);
                                    mercyButton.setSelected(true);
                                } else if (actButton.isSelected()) {
                                    actButton.setSelected(false);
                                    fightButton.setSelected(true);
                                } else if (itemButton.isSelected()) {
                                    itemButton.setSelected(false);
                                    actButton.setSelected(true);
                                } else if (mercyButton.isSelected()) {
                                    mercyButton.setSelected(false);
                                    itemButton.setSelected(true);
                                }
                                repaint();
                            }
                            break;

                        case KeyEvent.VK_DOWN:
                            hero.setDy(5);
                            hero.setDx(0);
                            break;
                        case KeyEvent.VK_RIGHT:

                            hero.setDx(5);
                            hero.setDy(0);
                            if (battleActive && turn){
                                if (fightButton.isSelected()) {
                                    fightButton.setSelected(false);
                                    actButton.setSelected(true);
                                } else if (actButton.isSelected()) {
                                    actButton.setSelected(false);
                                    itemButton.setSelected(true);
                                } else if (itemButton.isSelected()) {
                                    itemButton.setSelected(false);
                                    mercyButton.setSelected(true);
                                } else if (mercyButton.isSelected()) {
                                    mercyButton.setSelected(false);
                                    fightButton.setSelected(true);
                                }
                                break;
                            }
                        }
                       
                }
            }

            private void doACT() {
                mercyLevel++;
                actButton.setSelected(false);
                //TODO update mercy bar
                mercyBar.setMercy(mercyLevel);
                setEnemyTurn();
            }

            private void doMercy() {
                if (mercyLevel >= 5) {
                    System.out.println("You spared the enemy!");
                    //TODO implement mercy logic
                    battleActive = false;
                    turn = true;
                    hero.setSoulMode(false);
                    hero.setLocation(heroXBeforeBattle, heroYBeforeBattle); // reset hero position
                    setRoom(activeRoom);
                } else {
                    System.out.println("You need to increase your mercy level to spare the enemy.");
                    System.out.println("You Can increase your mercy level by ACTing on the enemy.");
                    mercyButton.setSelected(false);
                    setEnemyTurn();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();

                switch (code) {
                    case KeyEvent.VK_Z:
                        if (battleActive && turn){
                            if (fightButton.isSelected() && !fightAnimationActive && turn) {
                                doFIGHT();

                            } else if (actButton.isSelected()) {
                                System.out.println("Act");
                            } else if (itemButton.isSelected()) {
                                System.out.println("Item");
                            } else if (mercyButton.isSelected()) {
                                System.out.println("Mercy");
                            }
                        }
                        break;
                    case KeyEvent.VK_UP:
                        hero.setDy(0);
                        hero.setIdle();
                        break;
                    case KeyEvent.VK_LEFT:
                        if (!battleActive || !turn || (battleActive && true)) {
                            hero.setDx(0);
                            hero.setIdle();
                        }
                        
                        hero.setDx(0);
                        hero.setIdle();
                        break;
                    case KeyEvent.VK_DOWN:
                        hero.setDy(0);
                        hero.setIdle();
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (!battleActive || !turn) {
                            hero.setDx(0);
                            hero.setIdle();
                        }
                        break;
                }
            }
        });

    }

    public void doDamage(){        
        activeEnemyHealth --; // deal damage to the enemy
        enemyHealth.setHealth(activeEnemyHealth);
        System.out.println("enemy took damage");
    }

    public void setBackground(String imagePath) {
        URL imageURL = getClass().getResource(imagePath);

        try {
            background = ImageIO.read(imageURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doFIGHT() {
        fightAnimationActive = true;
        System.out.println("Active Enemy Health: " + activeEnemyHealth);
        
    }

    public void setRoom(Room room) {
        this.setBackground(room.getImagePath());
        //reset triggers
        for (EnemyTrigger enemy : enemyTriggers) {
            int x = (int) (Math.random() * (getWidth() - enemy.getBounds().getWidth()));
            int y = (int) (Math.random() * (getHeight() - enemy.getBounds().getHeight()));
            enemy.getBounds().setLocation(x, y);
            for (EnemyTrigger compare : enemyTriggers) { //prevent overlap
                if (enemy != compare && enemy.getBounds().intersects(compare.getBounds())
                        || enemy.getBounds().intersects(hero.getBounds())) {
                    x = (int) (Math.random() * (getWidth() - enemy.getBounds().getWidth()));
                    y = (int) (Math.random() * (getHeight() - enemy.getBounds().getHeight()));
                    enemy.getBounds().setLocation(x, y);
                }
            }
        }
        
        triggersPlaced = true;
        repaint();
    }

    public void activateBattleMode() {

        activeEnemyHealth = 10; // reset active enemy health
        enemyHealth.setHealth(activeEnemyHealth);
        //TODO implement buttons
        this.setBackground("./images/background/GenericBattle.png");

        fightButton.getBounds().setLocation((int)fightButton.getBounds().getX(), (int)fightButton.getBounds().getY());
        actButton.getBounds().setLocation((int)actButton.getBounds().getX(), (int)actButton.getBounds().getY());
        itemButton.getBounds().setLocation((int)itemButton.getBounds().getX(), (int)itemButton.getBounds().getY());
        mercyButton.getBounds().setLocation((int)mercyButton.getBounds().getX(), (int)mercyButton.getBounds().getY());

        fightButton.setSelected(true);
        actButton.setSelected(false);
        itemButton.setSelected(false);
        mercyButton.setSelected(false);
        

        battleActive = true;

        randomEnemyInt = (int) (Math.random() * genericEnemies.size()); //will be used to select a random enemy
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);

        // door rendering 
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getActive() && !battleActive) {
                ArrayList<Door> roomDoors = rooms.get(i).getDoors();
                for (Door door : roomDoors) {
                    int doorWidth = door.getBounds().width;
                    int doorHeight = door.getBounds().height;
                    g.setColor(java.awt.Color.BLUE);

                    if (door.getSideOfRoom().equals("right")) {
                        door.setPosition(this.getWidth() - doorWidth, this.getHeight() / 2 - doorHeight / 2);
                    } else if (door.getSideOfRoom().equals("left")) {
                        door.setPosition(0, this.getHeight() / 2 - doorHeight / 2);
                    } else if (door.getSideOfRoom().equals("up")) {
                        door.setPosition(this.getWidth() / 2 - doorWidth / 2, 0);
                    } else if (door.getSideOfRoom().equals("down")) {
                        door.setPosition(this.getWidth() / 2 - doorWidth / 2, this.getHeight() - doorHeight);
                    }
                    Rectangle bounds = door.getBounds();
                    g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

                    if (door.hasCollided(hero)) {
                        rooms.get(i).setActive(false);
                        for (Room room : rooms) {
                            if (room.getKey().equals(door.getKey())) {
                                System.out.println(door.getKey());
                                room.setActive(true);
                                String side = door.getSideOfRoom();
                                int width = (int)door.getBounds().getWidth();
                                int height = (int)door.getBounds().getHeight();
                                if (side.equals("right")) { //TODO verify collision logic
                                    hero.setLocation(0 + width, this.getHeight() / 2 - hero.getHeight() / 2);
                                } else if (side.equals("left")) {
                                    hero.setLocation(this.getWidth() - hero.getWidth() - width, this.getHeight() / 2 - hero.getHeight() / 2);
                                } else if (side.equals("up")) {
                                    hero.setLocation(this.getWidth() / 2 - hero.getWidth() / 2, this.getHeight() - hero.getHeight() - height);
                                } else if (side.equals("down")) {
                                    hero.setLocation(this.getWidth() / 2 - hero.getWidth() / 2, 0 + height);
                                }
                                setRoom(room);
                                activeRoom = room;
                                System.out.println("New Room: " + room.getKey());
                            }
                        }
                    }
                }
            }
        }

        if (!battleActive){
            g.drawImage(hero.getImageIcon().getImage(), hero.getX(), hero.getY(), this);
        }

        
        // enemy trigger rendering
        if (!battleActive && triggersPlaced){
            for (int i = 0; i < enemyTriggers.size(); i++) {
                EnemyTrigger enemy = enemyTriggers.get(i);
                g.setColor(java.awt.Color.RED);
                Rectangle bounds = enemy.getBounds();
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height); // This line of code provided by ChatGPT
                if (enemy.hasCollided(hero) && !battleActive) {
                    System.out.println("Enemy Triggered");
                    activateBattleMode();
                    enemyTriggers.remove(enemy);
                    i--;
                    repaint();
                    break;
                }
            }
        }
        // button rendering
        if (battleActive) {

            // FIGHT ANIMATION RENDERING//
            
            if (fightButton.isSelected()) {
                g.drawImage(fightButton.getSelectedImage().getImage(), (int)fightButton.getBounds().getX(), (int)fightButton.getBounds().getY(), this);
            } else {
                g.drawImage(fightButton.getNonSelectedImage().getImage(), (int)fightButton.getBounds().getX(), (int)fightButton.getBounds().getY(), this);
            }

            if (actButton.isSelected()) {
                g.drawImage(actButton.getSelectedImage().getImage(), (int)actButton.getBounds().getX(), (int)actButton.getBounds().getY(), this);
            } else {
                g.drawImage(actButton.getNonSelectedImage().getImage(), (int)actButton.getBounds().getX(), (int)actButton.getBounds().getY(), this);
            }

            if (itemButton.isSelected()) {
                g.drawImage(itemButton.getSelectedImage().getImage(), (int)itemButton.getBounds().getX(), (int)itemButton.getBounds().getY(), this);
            } else {
                g.drawImage(itemButton.getNonSelectedImage().getImage(), (int)itemButton.getBounds().getX(), (int)itemButton.getBounds().getY(), this);
            }

            if (mercyButton.isSelected()) {
                g.drawImage(mercyButton.getSelectedImage().getImage(), (int)mercyButton.getBounds().getX(), (int)mercyButton.getBounds().getY(), this);
            } else {
                g.drawImage(mercyButton.getNonSelectedImage().getImage(), (int)mercyButton.getBounds().getX(), (int)mercyButton.getBounds().getY(), this);
            }

            // soul rendering and attack rendering

            if (battleActive && !turn){
                g.drawImage(hero.getSoul(), hero.getX(), hero.getY(), this);
                switch (activeAttack) {
                    case "fallingBlocks":
                        //render falling blocks
                        
                        g.setColor(Color.BLUE);
                        for (FallingBlock block : fallingBlocks) {
                            if (!tookDamageThisFrame && frameCount > invincibilityFrame && block.getBounds().intersects(hero.getSoulHitbox())) {
                                // if the block intersects with the soul, deal damage to the hero
                                System.out.println("damage");
                                playerHealth.setHealth(playerHealth.getHealth() - 1); // deal damage to the player
                                invincibilityFrame = frameCount + 60; // player will be invincible for 60 frames
                                tookDamageThisFrame = true; // set tookDamageThisFrame to true to prevent multiple damage in one frame
                            }
                            g.fillRect((int)block.getBounds().getX(), (int)block.getBounds().getY(), (int)block.getBounds().getWidth(), (int)block.getBounds().getHeight());
                        }
                        tookDamageThisFrame = false; // reset tookDamageThisFrame for the next frame
                        
                        break;
                    case "fallingBlocksAtDifferentSpeeds":
                        
                        g.setColor(Color.RED);
                        for (FallingBlock block : fallingBlocksAtDifferentSpeeds) {
                            if (!tookDamageThisFrame && frameCount > invincibilityFrame && block.getBounds().intersects(hero.getSoulHitbox())) {
                                // if the block intersects with the soul, deal damage to the hero
                                System.out.println("damage");
                                doDamageToPlayer();
                            } 
                            g.fillRect((int)block.getBounds().getX(), (int)block.getBounds().getY(), (int)block.getBounds().getWidth(), (int)block.getBounds().getHeight());
                        }
                        tookDamageThisFrame = false; // reset tookDamageThisFrame for the next frame
                        break;
                }
            }

            

        }

        // enemy rendering

        if (battleActive){
            randomEnemyInt = (int) (Math.random() * genericEnemies.size());
            enemyClassName = genericEnemies.get(randomEnemyInt);
            switch (enemyClassName){
                case "TenzinEnemy":
                    g.drawImage(tenzinEnemy.getImage().getImage(), 200, 60, this);
                    break;

                //TODO implement other enemies
            }
        }
        //fight animation rendering 

        if(fightAnimationActive) {
            if (fightAnimationIterator < fightAnimation.size()) {
                g.drawImage(fightAnimation.get(fightAnimationIterator).getImage(), 200, 120, this);
                if (frameCount % 5 == 0){
                    fightAnimationIterator++;
                }
            } 
            else {
                doDamage();
                setEnemyTurn();
            }
        }

        //bars
        if (battleActive && (playerHealth != null && enemyHealth != null && mercyBar != null)) {
            g.drawImage(playerHealth.getBar(), playerHealth.getX(), playerHealth.getY(), this);
            g.drawImage(enemyHealth.getBar(), enemyHealth.getX(), enemyHealth.getY(), this);
            g.drawImage(mercyBar.getBar(), mercyBar.getX(), mercyBar.getY(), this);

        }

    }

    private void doDamageToPlayer() {
        playerHealth.setHealth(playerHealth.getHealth() - 1); // deal damage to the player
        System.out.println("Player took damage");
        invincibilityFrame = frameCount + 60; // player will be invincible for 60 frames
        tookDamageThisFrame = true; // set tookDamageThisFrame to true to prevent multiple damage in one frame
    }

    public void setEnemyTurn(){
        
        System.out.println("Active Enemy Health: " + activeEnemyHealth);
        fightAnimationActive = false;
        fightAnimationIterator = 0;
        damageAnimationCounter = frameCount + 60; // Set the damage animation to display for 3 seconds
        displayDamageAnimation = true; // Show damage animation
        turn = false; // enemy turn
        activeAttack = attacks.get((int)(Math.random() * attacks.size()));
        //prepare the attack to be rendered 

        switch (activeAttack){
            case "fallingBlocks":
                fallingBlocks.clear(); // clear previous blocks
                for (int i = 0; i < 10; i++) {
                    int x = (70 * i) + (int)(Math.random() * 10);
                    int y = 0; // start at the top
                    int width = 30, height = 30, speed = 8;
                    fallingBlocks.add(new FallingBlock(x, y, width, height, speed));                         
                }
                break;

            case "fallingBlocksAtDifferentSpeeds":
                fallingBlocksAtDifferentSpeeds.clear(); // clear previous blocks
                for (int i = 0; i < 10; i++) {
                    int x = 100 * i;
                    int y = 0; 
                    int width = 30, height = 30;
                    int speed = (int) (Math.random() * 10 + 5); // random speed between 1 and 5
                    fallingBlocksAtDifferentSpeeds.add(new FallingBlock(x, y, width, height, speed));
                }
                break;
        }
        
        
        System.out.println("Active Attack: " + activeAttack);
        fightButton.setSelected(false);
        if (battleActive){
            hero.setSoulMode(true);
            centerSoul = true; // center the soul in the middle of the screen
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (!battleActive) {
            hero.update();
        }
        if (battleActive) {
            
            if (!turn){
                if (centerSoul) {
                    heroXBeforeBattle = hero.getX();
                    heroYBeforeBattle = hero.getY();
                    hero.setPosition(250, 300);
                    System.out.println("centered");
                    centerSoul = false; // only center once
                }
                hero.update();
            }
            else if (turn && battleActive){
                hero.setDx(0);
                hero.setDy(0);
            }

            //battle ending/enemy defeated logic 
            if (activeEnemyHealth <= 0) {
                //battle ending logic
                System.out.println("Enemy Defeated");
                battleActive = false;
                turn = true;
                hero.setSoulMode(false);
                hero.setLocation(heroXBeforeBattle, heroYBeforeBattle); // reset hero position
                setRoom(activeRoom);
            }

            //attack logic here
            if (activeAttack.equals("fallingBlocks")) {
                for (int i = 0; i < fallingBlocks.size(); i++) {
                    FallingBlock block = fallingBlocks.get(i);
                    block.update();
                    if (block.getBounds().getY() > this.getHeight()) {
                        fallingBlocks.remove(i);
                        i--; // adjust index after removal
                    }
                    if (fallingBlocks.size() <= 0) {
                        turn = true; // player turn after all blocks are rendered
                        fightButton.setSelected(true);
                        activeAttack = ""; // reset active attack
                    }
                }
            }
            else if (activeAttack.equals("fallingBlocksAtDifferentSpeeds")) {
                for (int i = 0; i < fallingBlocksAtDifferentSpeeds.size(); i++) {
                    FallingBlock block = fallingBlocksAtDifferentSpeeds.get(i);
                    block.update();
                    if (block.getBounds().getY() > this.getHeight()) {
                        fallingBlocksAtDifferentSpeeds.remove(i);
                        i--; // adjust index after removal
                    }
                    if (fallingBlocksAtDifferentSpeeds.size() <= 0) {
                        turn = true; // player turn after all blocks are rendered
                        fightButton.setSelected(true);
                        activeAttack = ""; // reset active attack
                    }
                }
            }
        }
        frameCount++;
        repaint();

        // Remove all target logic here

        hero.getPanelHeight(this.getHeight());
        hero.getPanelWidth(this.getWidth());
    }
}