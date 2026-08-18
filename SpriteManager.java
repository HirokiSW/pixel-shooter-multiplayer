/**
    The class responsible for caching and organization of different BufferedImage objects containing sprite assets.
    Contains different methods of drawing depending on context, allowing for rotation and flipping, in both static and animated sprites.
    
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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class SpriteManager {
    private String[] spriteNames;
    private BufferedImage[] spriteList;
    private int numberOfSprites;
    private String[] animationNames;
    private ArrayList<BufferedImage[]> animationList;
    private int numberOfAnimations;

    /**
     * Constructs the SpriteManager and loads all the sprites and animation frames, organized in IDs and names specified by Const.
     */
    public SpriteManager() {
        numberOfSprites = Const.NUMBER_OF_SPRITES;
        numberOfAnimations = Const.NUMBER_OF_ANIMATIONS;

        spriteList = new BufferedImage[numberOfSprites];
        spriteNames = new String[numberOfSprites];

        animationList = new ArrayList<>();
        animationNames = new String[numberOfAnimations];

        setUpArrays();
        loadSpritesAndFrames();
    }

    /**
     * Initializes the sprite and animation name arrays based on Const values.
     */
    private void setUpArrays() {
        for (int spriteID = 0; spriteID < numberOfSprites; spriteID++) {
            spriteNames[spriteID] = Const.SPRITE_NAMES[spriteID];
        }
        for (int animationID = 0; animationID < numberOfAnimations; animationID++) {
            animationList.add(new BufferedImage[Const.ANIM_FRAMES[animationID]]);
            animationNames[animationID] = Const.ANIM_NAMES[animationID];
        }
    }

    /**
     * Loads all sprite and animation image files in the specified arrays.
     */
    private void loadSpritesAndFrames() {
        for (int spriteID = 0; spriteID < numberOfSprites; spriteID++) {
            try {
                spriteList[spriteID] = ImageIO.read(new File(spriteNames[spriteID] + ".png"));
            } catch (IOException e) {
                System.out.println("IO Exception from SM loadSpritesAndFrames");
            }
        }
        for (int animationID = 0; animationID < numberOfAnimations; animationID++) {
            for (int frameIndex = 0; frameIndex < animationList.get(animationID).length; frameIndex++) {
                try {
                    animationList.get(animationID)[frameIndex] = ImageIO.read(new File(animationNames[animationID] + frameIndex + ".png"));
                } catch (IOException e) {
                    System.out.println("IO Exception from SM loadSpritesAndFrames");
                }
            }
        }
    }

    /**
     * Draws a static sprite on the screen using the specified shape.
     *
     * @param g2d the Graphics2D object to draw on
     * @param spriteID the ID of the sprite to be drawn
     * @param shape the object holding the position, size, and rotation details of the sprite
     */
    public void drawSprite(Graphics2D g2d, int spriteID, ShapeDimensions shape) {
        g2d.drawImage(spriteList[spriteID], (int) shape.getX(), (int) shape.getY(), (int) shape.getWidth(), (int) shape.getHeight(), null);
    }

    /**
     * Draws a static sprite on the screen using the specified position and dimensions.
     *
     * @param g2d the Graphics2D object to draw on
     * @param spriteID the ID of the sprite to be drawn
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param width the width to draw
     * @param height the height to draw
     */
    public void drawSprite(Graphics2D g2d, int spriteID, double x, double y, double width, double height) {
        g2d.drawImage(spriteList[spriteID], (int) x, (int) y, (int) width, (int) height, null);
    }

    /**
     * Draws a rotatable gun sprite that can be flipped vertically.
     *
     * @param g2d the Graphics2D object to draw on
     * @param spriteID the ID of the gun sprite
     * @param shape the object holding the position, size, and rotation details of the sprite
     */
    public void drawGunSprite(Graphics2D g2d, int spriteID, ShapeDimensions shape) {
        AffineTransform revert = g2d.getTransform();
        double angleDeg = Math.toDegrees(shape.getRotation()) % 360;
        boolean shouldFlipVertically = (angleDeg > 90 && angleDeg < 270) || (angleDeg < -90 && angleDeg > -270);
        g2d.rotate(shape.getRotation(), shape.getPivotX(), shape.getPivotY());
        if (shouldFlipVertically) {
            g2d.translate(shape.getPivotX(), shape.getPivotY());
            g2d.scale(1, -1);
            g2d.translate(-shape.getPivotX(), -shape.getPivotY());
        }
        g2d.drawImage(spriteList[spriteID], (int) shape.getX(), (int) shape.getY(), (int) shape.getWidth(), (int) shape.getHeight(), null);
        g2d.setTransform(revert);
    }

    /**
     * Draws a frame of an animated sprite using the specified shape.
     *
     * @param g2d the Graphics2D object to draw on
     * @param animID the ID of the animation
     * @param shape the object holding the position, size, and rotation details of the sprite
     * @param currFrame the index of the current animation frame
     */
    public void drawAnimatedSprite(Graphics2D g2d, int animID, ShapeDimensions shape, int currFrame) {
        g2d.drawImage(animationList.get(animID)[currFrame], (int) shape.getX(), (int) shape.getY(), (int) shape.getWidth(), (int) shape.getHeight(), null);
    }

    /**
     * Draws a frame of an animated sprite using specified position and dimensions.
     *
     * @param g2d the Graphics2D object to draw on
     * @param animID the ID of the animation
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param width the width to draw
     * @param height the height to draw
     * @param currFrame the index of the current animation frame
     */
    public void drawAnimatedSprite(Graphics2D g2d, int animID, double x, double y, double width, double height, int currFrame) {
        g2d.drawImage(animationList.get(animID)[currFrame], (int) x, (int) y, (int) width, (int) height, null);
    }

    /**
     * Draws a frame of an animated entity, flipping it horizontally based on its rotation.
     *
     * @param g2d the Graphics2D object to draw on
     * @param animID the ID of the animation
     * @param shape the object holding the position, size, and rotation details of the sprite
     * @param currFrame the index of the current animation frame
     */
    public void drawAnimatedEntity(Graphics2D g2d, int animID, ShapeDimensions shape, int currFrame) {
        AffineTransform revert = g2d.getTransform();
        double angleDeg = Math.toDegrees(shape.getRotation()) % 360;
        boolean shouldFlipHorizontally = (angleDeg > 90 && angleDeg < 270) || (angleDeg < -90 && angleDeg > -270);
        if (shouldFlipHorizontally) {
            g2d.translate(shape.getPivotX(), shape.getPivotY());
            g2d.scale(-1, 1);
            g2d.translate(-shape.getPivotX(), -shape.getPivotY());
        }
        g2d.drawImage(animationList.get(animID)[currFrame], (int) shape.getX(), (int) shape.getY(), (int) shape.getWidth(), (int) shape.getHeight(), null);
        g2d.setTransform(revert);
    }

    /**
     * Draws a frame of a rotatable animated projectile.
     *
     * @param g2d the Graphics2D object to draw on
     * @param animID the ID of the animation
     * @param shape the object holding the position, size, and rotation details of the sprite
     * @param currFrame the index of the current animation frame
     */
    public void drawAnimatedProjectile(Graphics2D g2d, int animID, ShapeDimensions shape, int currFrame) {
        AffineTransform revert = g2d.getTransform();
        g2d.rotate(shape.getRotation(), shape.getPivotX(), shape.getPivotY());
        g2d.drawImage(animationList.get(animID)[currFrame], (int) shape.getX(), (int) shape.getY(), (int) shape.getWidth(), (int) shape.getHeight(), null);
        g2d.setTransform(revert);
    }
    
    /**
     * Draws a frame of an animated effect using specified position and dimensions.
     *
     * @param g2d the Graphics2D object to draw on
     * @param animID the ID of the animation
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param width the width of the effect
     * @param height the height of the effect
     * @param rotation the rotation angle in radians
     * @param pivotX the x-coordinate of the rotation pivot
     * @param pivotY the y-coordinate of the rotation pivot
     * @param currFrame the index of the current animation frame
     */
    public void drawAnimatedEffect(Graphics2D g2d, int animID, double x, double y, double width, double height, double rotation, double pivotX, double pivotY, int currFrame) {
        AffineTransform revert = g2d.getTransform();
        g2d.rotate(rotation, pivotX, pivotY);
        g2d.drawImage(animationList.get(animID)[currFrame], (int) x, (int) y, (int) width, (int) height, null);
        g2d.setTransform(revert);
    }
}
