/**
    Interface for objects that act as projectiles in the game.
    Implementing classes should be able to know its shooter, and know when it has collided or finished.
    The projectiles are also movable, as they extend ObjectMovable.
    
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

public interface ObjectProjectile extends ObjectMovable {

    /**
     * Returns the entity that produced the projectile.
     *
     * @return entity object of shooter.
     */
    ObjectEntity getShooter();

    /**
     * Returns whether or not projectile has collided.
     *
     * @return true if projectile has collided with another object.
     */
    boolean hasCollided();

    /**
     * Returns whether or not projectile has finished and ready for clean-up.
     *
     * @return true if projectile has finished its animation.
     */
    boolean hasFinished();
}