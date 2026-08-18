/**
    Interface for objects that are drawn at the canvas.
    Implementing classes should define what objects will be drawn using Graphics2D and SpriteManager.
    
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

import java.awt.Graphics2D;

public interface SpriteDrawing {

    /**
     * Draws the sprite using Graphics2D and SpriteManager.
     *
     * @param g2d the Graphics2D object to draw on
     * @param sm  the object used to retrieve and manage the sprites that will be used
     */
    void draw(Graphics2D g2d, SpriteManager sm);
}
