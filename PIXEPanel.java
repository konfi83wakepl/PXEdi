import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;


public class PIXEPanel extends JPanel implements ActionListener {

    // Data fields
    static final int SCREEN_WIDTH = 1201; // Width of the screen
    static final int SCREEN_HEIGHT = 626; // Height of the screen
    static final int UNIT_SIZE = 25; // Size of each unit in the grid
    static final int DELAY = 22; // Delay for the timer
    static final int SCREEN_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; // Total number of units in the screen
    final int[] x = new int[SCREEN_UNITS]; // Array to store x-coordinates
    final int[] y = new int[SCREEN_UNITS]; // Array to store y-coordinates
    int mouseX, mouseY; // Coordinates of the mouse pointer
    boolean userClicked = false; // Flag to indicate if the user clicked
    boolean bucketToolEnabled = false; // Flag to indicate if the bucket tool is enabled
    boolean showLines = true; // Flag to indicate if grid lines should be shown
    ArrayList<Rectangle> paintedStuff = new ArrayList<Rectangle>(); // List to store painted rectangles
    Timer timer; // Timer for repainting the screen
    Color color = Color.BLACK; // Default color for painting

    /**
     * Constructor for PaintPanel.
     */
    PIXEPanel() {
        // Set panel dimensions and appearance
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT + 36));
        this.setBackground(Color.white);
        this.setFocusable(true);
        this.addMouseListener(new MyMouseAdapter());

        // Create button panel and add it to the bottom of the panel
        JPanel buttonPanel = createButtonPanel();
        this.setLayout(new BorderLayout());
        this.add(buttonPanel, BorderLayout.SOUTH);

        // Start the painting process
        startPaint();
    }

    /**
     * Create a panel containing buttons for various actions.
     * 
     * @return JPanel containing buttons.
     */
    public JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton colourButton = new JButton("Change Colour");
        JButton saveButton = new JButton("Save as Image");
        JButton bucketButton = new JButton("Bucket Tool");
        JButton showLinesButton = new JButton("Show Lines");

        // Add buttons to the panel
        buttonPanel.add(colourButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(bucketButton);
        buttonPanel.add(showLinesButton);

        // Add action listeners to the buttons
        colourButton.addActionListener(new ActionListener() {
            /**
             * Opens a color chooser dialog when the button is clicked and sets the selected
             * color.
             * 
             * @param e The ActionEvent associated with the button click.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(buttonPanel, "Choose Color", Color.RED);
                if (newColor != null) {
                    color = newColor;
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            /**
             * Saves the panel as an image when the button is clicked.
             * 
             * @param e The ActionEvent associated with the button click.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImage(SCREEN_WIDTH, SCREEN_HEIGHT);
            }
        });

        bucketButton.addActionListener(new ActionListener() {
            /**
             * Toggles the bucket tool on/off when the button is clicked and repaints the
             * panel.
             * 
             * @param e The ActionEvent associated with the button click.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                bucketToolEnabled = !bucketToolEnabled;
                repaint();
            }
        });

        showLinesButton.addActionListener(new ActionListener() {
            /**
             * Toggles the visibility of grid lines when the button is clicked.
             * 
             * @param e The ActionEvent associated with the button click.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                showLines = !showLines;
            }
        });

        return buttonPanel;
    }

    /**
     * Start the timer for repainting the panel.
     */
    public void startPaint() {
        timer = new Timer(DELAY, this);
        timer.start();
    }

    /**
     * Calls the draw method that draws the content of the screen.
     * 
     * @param g Graphics object to draw.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Draws lines on screen based on UNIT_SIZE, allows user to draw
     * colors of their choice inside the units.
     * 
     * @param g Graphics object to draw.
     */
    public void draw(Graphics g) {
        // Draw rectangles representing painted areas
        for (Rectangle rect : paintedStuff) {
            g.setColor(rect.getColor());
            g.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        }

        // Draw grid lines if enabled
        if (showLines) {
            g.setColor(Color.GRAY);
            for (int i = 0; i < SCREEN_WIDTH; i += UNIT_SIZE) {
                g.drawLine(i, 0, i, SCREEN_HEIGHT);
            }
            for (int j = 0; j < SCREEN_HEIGHT; j += UNIT_SIZE) {
                g.drawLine(0, j, SCREEN_WIDTH, j);
            }
        }
    }

    /**
     * Runs methods needed to make paint run, repaints the screen
     * to reflect what is happening.
     * 
     * @param e An ActionEvent to indicate something is happening.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    /**
     * Save the panel as an image.
     * 
     * @param width  Width of the image.
     * @param height Height of the image.
     */
    public void saveImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        paint(g2d);
        g2d.dispose();
        BufferedImage croppedImage = cropImage(image, 0, 40, image.getWidth(), image.getHeight() - 40);

        // Save the image to a file
        try {
            File output = new File("saved_image.png");
            ImageIO.write(croppedImage, "png", output);
            System.out.println("Image saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crop an image to specified dimensions.
     * 
     * @param originalImage The original image to be cropped.
     * @param x             The x-coordinate of the top-left corner of the crop
     *                      area.
     * @param y             The y-coordinate of the top-left corner of the crop
     *                      area.
     * @param width         The width of the crop area.
     * @param height        The height of the crop area.
     * @return The cropped BufferedImage.
     */
    public static BufferedImage cropImage(BufferedImage originalImage, int x, int y, int width, int height) {
        // Ensure crop area is within bounds
        if (x < 0 || y < 0 || width <= 0 || height <= 0 || x + width > originalImage.getWidth()
                || y + height > originalImage.getHeight()) {
            throw new IllegalArgumentException("Invalid crop dimensions");
        }

        // Create a new BufferedImage with the cropped dimensions
        BufferedImage croppedImage = new BufferedImage(width, height, originalImage.getType());

        // Copy the cropped portion of the original image to the new BufferedImage
        Graphics2D g2d = croppedImage.createGraphics();
        g2d.drawImage(originalImage.getSubimage(x, y, width, height), 0, 0, null);
        g2d.dispose();

        return croppedImage;
    }

    /**
     * Change the panel background color.
     * 
     * @param color The color to set as the background.
     */
    public void paintBackground(Color color) {
        super.setBackground(color);
    }

    // Custom mouse adapter for handling mouse events
    public class MyMouseAdapter extends MouseAdapter {

        /**
         * Handles the mouse click event, allowing the user to draw on the panel or
         * activate the bucket tool.
         * 
         * @param e The MouseEvent containing information about the mouse click event.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            // Calculate the closest unit
            int closestX = (mouseX / UNIT_SIZE) * UNIT_SIZE;
            int closestY = (mouseY / UNIT_SIZE) * UNIT_SIZE;
            if (!bucketToolEnabled) {
                // Add a painted rectangle to the list
                Rectangle rect = new Rectangle(closestX, closestY, UNIT_SIZE, UNIT_SIZE, color);
                paintedStuff.add(rect);
            } else {
                // Paint the entire background with the selected color
                paintBackground(color);
            }
            repaint();
        }

    }

}