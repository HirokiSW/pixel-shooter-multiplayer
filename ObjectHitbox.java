/**
    Interface for objects that have a hitbox used in collision detection.
    Implementing classes should define how hitbox dimensions are accessed.
    
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

public interface ObjectHitbox {

    /**
     * Returns the transformations of the hitbox in the specified dimension.
     *
     * @param dimensionID the ID of the dimension
     * @return the specified dimension of the hitbox
     */
    double getHitbox(int dimensionID);
}