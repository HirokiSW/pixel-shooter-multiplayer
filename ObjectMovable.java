/**
    Interface for objects that is able to move as well as check collision with consideration for speed.
    Implementing classes should be able to continuously update position, check collision, be able to move, and have a values for speed.
    Extends ObjectHitbox for collision detection.
    
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

public interface ObjectMovable extends ObjectHitbox {

    /**
     * Updates the position of the object.
     */
    void updatePosition();

    /**
     * Facilitates collision between other object's hitbox and its own
     *
     * @param o the other object to check collision against
     */
    void detectCollision(ObjectHitbox o);

    /**
     * Moves the object horizontally at the specified speed.
     *
     * @param speed the horizontal speed value
     */
    void moveX(double speed);

    /**
     * Moves the object vertically at the specified speed.
     *
     * @param speed the vertical speed value
     */
    void moveY(double speed);

    /**
     * Returns the speed of the object along the specified axis.
     *
     * @param dimensionID the ID of the dimension
     * @return the speed in the given direction
     */
    double getSpeed(int dimensionID);

}