/**
    Interface for objects that can interact with the player on collision.
    Implementing classes should define collision logic with the player.
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

public interface ObjectInteractable extends ObjectHitbox {

    /**
     * Facilitates collision between player and its hitbox, as well as its interactions.
     * 
     * @param o player object to be checked for collision.
     */
    void detectCollision(Player o);
}
