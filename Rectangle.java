import java.awt.Color;


public class Rectangle {
    private int x;          // x-coordinate of the top-left corner
    private int y;          // y-coordinate of the top-left corner
    private int width;      // width of the rectangle
    private int height;     // height of the rectangle
    private Color color;    // color of the rectangle

    /**
     * Constructs a new Rectangle with the specified parameters.
     * 
     * @param x The x-coordinate of the top-left corner.
     * @param y The y-coordinate of the top-left corner.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     * @param color The color of the rectangle.
     */
    public Rectangle(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    /**
     * Gets the x-coordinate of the top-left corner of the rectangle.
     * 
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the top-left corner of the rectangle.
     * 
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the width of the rectangle.
     * 
     * @return The width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the rectangle.
     * 
     * @return The height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the color of the rectangle.
     * 
     * @return The color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of the rectangle.
     * 
     * @param color The new color to set.
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
