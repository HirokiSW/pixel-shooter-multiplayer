/**
    This is the interface for all game entities that has health, can take damage and knockback and can die.
    These entities are also movable, as they extend ObjectMovable.
    
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

public interface ObjectEntity extends ObjectMovable {

    /** @return the max HP of the entity */
    double getMaxHP();

    /** @return the current HP of the entity */
    double getHP();

    /** @return true if the entity is knocked back, false otherwise */
    boolean isKnocked();

    /** @return true if the entity has 0 HP left, false otherwise */
    boolean isDead();

    /** Reduces entity's HP by a specified value
     *
     * @param damage the amount of damage to apply
     */
    void takeDamage(double damage);

    /**
     * Sets the entity's HP to a specified value
     *
     * @param health HP value to set
     */
    void setHealth(double health);

    /**
     * Applies knockback the entity given its magnitude and direction
     *
     * @param magnitude  the strength of knockback
     * @param trajectory the direction of knockback (in radians)
     */
    void takeKnockback(double magnitude, double trajectory);

    /**
     * Ends knockback on entity
     */
    void endKnockback();
}