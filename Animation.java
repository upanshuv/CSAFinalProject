import javax.swing.JFrame;


public class Animation {

    /**
     * main method which instantiates our JFrame and
     * our gamePanel.   
     */

    public static void main(String[] args) {

        //Scanner myScanner = new Scanner(System.in);
        //System.out.println("Enter Password to Play: ");
        //String password = myScanner.nextLine();
        //if (!password.equals("upanshu")) {
        //    System.out.println("Incorrect Password. Exiting...");
        //    System.out.println(0/0);
        //    return;
        //}
        
    

        JFrame frame = new JFrame("Undercoooked"); 
        frame.setBounds(50, 50, 500, 500);

        // makes it impossible to resize the frame
        frame.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        

        frame.add(gamePanel);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
