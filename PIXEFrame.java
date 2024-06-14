import javax.swing.*;


public class PIXEFrame extends JFrame{
    
    /**
     * Constructs a new PaintFrame and initializes its components.
     */
    PIXEFrame(){
        PIXEPanel paintPanel = new PIXEPanel();
        this.add(paintPanel);
        this.setTitle("PIXEditor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

