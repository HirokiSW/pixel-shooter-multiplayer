/**
    This class acts as data for the transformations and dimensions of an object's shape.
    The getter and setter methods of this class will be used to help draw sprites, and facilitate hitbox collision.
    
    @author Hiroki S. Watanabe (244844)
    @author Yuuki S. Watanabe (244845)
    @version 20 May 2025
    I have not discussed the Java language code in my program
    with anyone other than my instructor or the teaching assistants
    assigned to this course.
    I have not used Java language code obtained from another student,
    or any other unauthorized source, either modified or unmodified.
    If any Java language code or documentation used in my program
    was obtained from another source, such as a textbook or website,
    that has been clearly noted with a proper citation in the comments
    of my program.
 */

public class ShapeDimensions {
    private double xPos, yPos, width, height, rotation, pivotX, pivotY;

    /**
     * Constructs the dimensions of the shape with specified position and size.
     * The pivot is automatically initialized to the center of the shape.
     *
     * @param xPos the x-coordinate of the shape
     * @param yPos the y-coordinate of the shape
     * @param width the width of the shape
     * @param height the height of the shape
     */
    public ShapeDimensions(double xPos, double yPos, double width, double height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.rotation = 0;
        this.pivotX = xPos + width/2;
        this.pivotY = yPos + height/2;
    }

    /** @return the x-coordinate of the shape */
    public double getX() {
        return xPos;
    }

    /** @return the y-coordinate of the shape */
    public double getY() {
        return yPos;
    }

    /** @return the width of the shape */
    public double getWidth() {
        return width;
    }

    /** @return the height of the shape */
    public double getHeight() {
        return height;
    }

    /** @return the current rotation angle of the shape in radians */
    public double getRotation() {
        return rotation;
    }

    /** @return the x-coordinate of the shape's pivot point */
    public double getPivotX() {
        return pivotX;
    }

    /** @return the y-coordinate of the shape's pivot point */
    public double getPivotY() {
        return pivotY;
    }

    /**
     * Sets the x-coordinate of the shape.
     *
     * @param x the new x-position
     */
    public void setX(double x) {
        xPos = x;
    }

    /**
     * Sets the y-coordinate of the shape.
     *
     * @param y the new y-position
     */
    public void setY(double y) {
        yPos = y;
    }

    /**
     * Sets the width of the shape.
     *
     * @param w the new width
     */
    public void setWidth(double w) {
        width = w;
    }

    /**
     * Sets the height of the shape.
     *
     * @param h the new height
     */
    public void setHeight(double h) {
        height = h;
    }

    /**
     * Sets the rotation angle of the shape in radians.
     *
     * @param theta the new rotation angle
     */
    public void setRotation(double theta) {
        rotation = theta;
    }

    /**
     * Sets the pivot point of the shape.
     *
     * @param x the new x-coordinate of the pivot
     * @param y the new y-coordinate of the pivot
     */
    public void setPivot(double x, double y) {
        pivotX = x;
        pivotY = y;
    }
}
