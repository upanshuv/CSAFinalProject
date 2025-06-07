import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Display extends JPanel {

    private JTextArea textArea;
    private ArrayList<ArrayList<String>> coolTexts;
    private ArrayList<String> texts;
    private ArrayList<String> list;
    private int currentTextIndex;
    private String fullText;
    private StringBuilder displayedText;
    private int textIndex;
    private Timer typingTimer;
    private int tsCode;

    public Display(int tsCode) {
        coolTexts = new ArrayList<>();
        texts = new ArrayList<>();
        list = new ArrayList<>();
        currentTextIndex = 0;
        displayedText = new StringBuilder();
        textIndex = 0;
        //tsCode must be initialized through method first

        // test
        texts.add("She found an old, dusty journal hidden beneath the floorboards.");
        texts.add("Each page revealed secrets of a life she never knew her grandmother's adventures as a spy.");
        texts.add("As rain tapped the windows, she read on, heart pounding with every word.");
        texts.add("By dawn, she knew exactly what she had to do next.");


        list.add("One day, a curious child wandered into the clock shop and asked why the world was so unfair.");
        list.add("The clockmaker smiled and said, 'Because sometimes, the truth is too heavy to bear.'");
        list.add("The child pondered this, then asked, 'But what if the truth could change everything?'");
        list.add("The clockmaker paused, then replied, 'Then we must be brave enough to face it.'");

        coolTexts.add(texts);
        coolTexts.add(list);

        fullText = coolTexts.get(tsCode).get(0);

        /* panel setup
        this.setLayout(null);
        this.setPreferredSize(new Dimension(600, 600));
        */

        // this
        this.setBackground(Color.BLACK);
        this.setLayout(null);
        this.setBounds(10, 500, 580, 80);

        // text area
        textArea = new JTextArea();
        textArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        textArea.setForeground(Color.BLACK);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        Color lightGreen = new Color(178, 255, 102); // Green color
        textArea.setBackground(lightGreen);
        textArea.setBounds(5, 5, 570, 70);

        // Add the text area to the text box
        this.add(textArea);

        // Set up the typing timer
        typingTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textIndex < fullText.length()) {
                    displayedText.append(fullText.charAt(textIndex));
                    textArea.setText(displayedText.toString());
                    textIndex++;
                } else {
                    typingTimer.stop();
                }
            }
        });
    }

    //start typing the current text
    public void startTyping() {
        displayedText.setLength(0); // Clear the displayed text
        textIndex = 0; // Reset the text index
        typingTimer.start();
    }

    //switch to the next text
    public void nextText() {
        if (currentTextIndex < coolTexts.get(tsCode).size() - 1) {
            currentTextIndex++;
            fullText = coolTexts.get(tsCode).get(currentTextIndex);
            startTyping();
        }
    }

    //reset to the first text
    public void resetText() {
        currentTextIndex = 0;
        if (tsCode == 0)
        {
            fullText = texts.get(currentTextIndex);
        }
        else if (tsCode == 1)
        {
            fullText = list.get(currentTextIndex);
        }
        else
        {
            fullText = "Error: Invalid tsCode";
        }
        startTyping();
    }

    //add a new text
    public void addText(String newText, int tsCode) {
        coolTexts.get(tsCode).add(newText);
    }

    //check if typing is in progress
    public boolean isTyping() {
        return typingTimer.isRunning();
    }

    public int currentIndex(){
        return currentTextIndex;
    }

    //immediately display the current text
    public void displayCurrent() {
        typingTimer.stop(); 
        displayedText.setLength(0);
        displayedText.append(fullText);
        textArea.setText(displayedText.toString());
    }

    public int currentArrLength() {
        return coolTexts.get(tsCode).size();
    }

    public void setTsCode(int code) {
        tsCode = code;
        fullText = coolTexts.get(tsCode).get(0);
    }
}