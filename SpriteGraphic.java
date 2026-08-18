/**
    Interface for objects that are drawn in the canvas.
    Implementing classes should be able to return the image's different dimensions and transformations, as well as its layer value.
    The graphic is drawable, as it extends SpriteDrawing.
    
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

public interface SpriteGraphic extends SpriteDrawing {

    /**
     * Returns the transformations of the image in the specified dimension.
     *
     * @param dimensionID specified dimension ID as stated in Const
     * @return the specified dimension of the image.
     */
    double getImage(int dimensionID);

    /**
     * Returns the calculated layer of the image, dictating the order its drawn.
     *
     * @return layer value of the image.
     */
    int getLayer();
}
