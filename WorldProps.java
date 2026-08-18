/**
    This class represents the game world's props and decorative elements.
    When Prop object is instantiated, both sprite and hitbox is added to the world.
    
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
import java.util.ArrayList;

public class WorldProps {

    /**
     * Constructs the WorldProps object, initializing and placing of 
     * predefined Prop objects on their respective map positions.
     * @param blocks the list of hitboxes representing solid or impassable objects
     * @param graphics the list of all graphical sprites
     */
    public WorldProps(ArrayList<ObjectHitbox> blocks, ArrayList<SpriteGraphic> graphics) {
        ArrayList<Prop> allProps = new ArrayList<>();

        allProps.add(new Prop(Const.LOBBY_ID, Const.STATUE, 2373, 1247));
        allProps.add(new Prop(Const.LOBBY_ID, Const.GRAVE_MINI, 2208, 1416));
        allProps.add(new Prop(Const.LOBBY_ID, Const.GRAVE_CROSS, 2255, 1223));
        allProps.add(new Prop(Const.LOBBY_ID, Const.GRAVE_RIP, 2544, 1317));

        allProps.add(new Prop(Const.LOBBY_ID, Const.SCRIBE_SML, 574, 1342));
        allProps.add(new Prop(Const.LOBBY_ID, Const.SCRIBE_MED, 671, 1150));
        allProps.add(new Prop(Const.LOBBY_ID, Const.SCRIBE_LRG, 1197, 1198));

        allProps.add(new Prop(Const.LOBBY_ID, Const.COLUMN_LRG, 715, 1819));
        allProps.add(new Prop(Const.LOBBY_ID, Const.COLUMN_LRG, 1005, 1819));
        allProps.add(new Prop(Const.LOBBY_ID, Const.COLUMN_LRG, 1580, 1289));
        allProps.add(new Prop(Const.LOBBY_ID, Const.COLUMN_LRG, 1870, 1289));
        allProps.add(new Prop(Const.LOBBY_ID, Const.COLUMN_LRG, 3360, 2106));
        allProps.add(new Prop(Const.LOBBY_ID, Const.COLUMN_LRG, 3646, 2106));

        allProps.add(new Prop(Const.LOBBY_ID, Const.COLUMN_SML, 720, 1392));
        allProps.add(new Prop(Const.LOBBY_ID, Const.COLUMN_SML, 1104, 1392));
        allProps.add(new Prop(Const.LOBBY_ID, Const.COLUMN_SML, 1536, 1103));
        allProps.add(new Prop(Const.LOBBY_ID, Const.COLUMN_SML, 1920, 1103));
        allProps.add(new Prop(Const.LOBBY_ID, Const.COLUMN_SML, 3313, 1919));
        allProps.add(new Prop(Const.LOBBY_ID, Const.COLUMN_SML, 3697, 1919));

        allProps.add(new Prop(Const.LOBBY_ID, Const.BENCH, 1777, 2256));
        allProps.add(new Prop(Const.LOBBY_ID, Const.BENCH, 2257, 2256));

        allProps.add(new Prop(Const.LOBBY_ID, Const.TREE_SML, 335, 1703));
        allProps.add(new Prop(Const.LOBBY_ID, Const.TREE_SML, 1440, 1703));
        allProps.add(new Prop(Const.LOBBY_ID, Const.TREE_MED, 1054, 1681));
        allProps.add(new Prop(Const.LOBBY_ID, Const.TREE_MED, 2735, 1197));
        allProps.add(new Prop(Const.LOBBY_ID, Const.TREE_LRG, 3598, 2106));

        allProps.add(new Prop(Const.PVP_ID, Const.BOX, 190, 1434));
        allProps.add(new Prop(Const.PVP_ID, Const.BOX, 290, 1434));
        allProps.add(new Prop(Const.PVP_ID, Const.BOX, 390, 1434));
        allProps.add(new Prop(Const.PVP_ID, Const.BOX, 190, 1961));
        allProps.add(new Prop(Const.PVP_ID, Const.BOX, 290, 1961));
        allProps.add(new Prop(Const.PVP_ID, Const.BOX, 955, 1434));
        allProps.add(new Prop(Const.PVP_ID, Const.BOX, 1055, 1434));
        allProps.add(new Prop(Const.PVP_ID, Const.BOX, 855, 1961));
        allProps.add(new Prop(Const.PVP_ID, Const.BOX, 955, 1961));
        allProps.add(new Prop(Const.PVP_ID, Const.BOX, 1055, 1961));

        for (Prop p : allProps) {
            blocks.add(p);
            graphics.add(p);
        }
    }
    
    /**
     * Class represnting an individual prop in the world.
     * When Prop is initialized, sprite and hitbox dimensions is automatically assigned according to propID.
     */
    private class Prop implements ObjectHitbox, SpriteGraphic {
        private ShapeDimensions hitbox;
        private ShapeDimensions spriteBounds;
        private int spriteID, propID;
        private final static double[][] spriteDimensions = {
            {0, 0, 191, 143},
            {0, 0, 290, 387},
            {0, 0, 387, 484},
            {0, 0, 389, 486},
            {0, 0, 96, 144},
            {0, 0, 100, 300},
            {0, 0, 150, 250},
            {0, 0, 96, 96},
            {0, 0, 100, 150},
            {0, 0, 100, 150},
            {0, 0, 100, 150},
            {0, 0, 100, 200},
            {0, 0, 100, 250},
            {0, 0, 100, 150}
        };
        private final static double[][] hitboxDimensions = {
            {13, 11, 165, 57},
            {127, 355, 36, 31},
            {174, 432, 36, 31},
            {176, 434, 36, 31},
            {17, 80, 63, 47},
            {0, 200, 100, 83},
            {18, 163, 113, 76},
            {4, 37, 88, 58},
            {23, 95, 55, 52},
            {4, 100, 91, 48},
            {10, 93, 80, 47},
            {10, 139, 80, 47},
            {10, 191, 80, 47},
            {0, 50, 100, 100}
        };

        /**
         * Constructs a Prop object with a specified map ID, sprite ID,
         * and position relative to the map's origin.
         *
         * @param mapID the ID of the map the prop belongs to
         * @param spriteID the sprite ID used to fetch its graphic from the sprite manager
         * @param xRelative the x-position relative to the map origin
         * @param yRelative the y-position relative to the map origin
         */
        private Prop(int mapID, int spriteID, double xRelative, double yRelative) {
            this.spriteID = spriteID;
            propID = spriteID - Const.BENCH;
            double x = xRelative + Const.MAP_START[mapID][Const.X];
            double y = yRelative + Const.MAP_START[mapID][Const.Y];
            spriteBounds = new ShapeDimensions(x, y, spriteDimensions[propID][Const.WIDTH], spriteDimensions[propID][Const.HEIGHT]);
            hitbox = new ShapeDimensions(
                x + hitboxDimensions[propID][Const.X], 
                y + hitboxDimensions[propID][Const.Y], 
                hitboxDimensions[propID][Const.WIDTH], 
                hitboxDimensions[propID][Const.HEIGHT]
            );
        }

        @Override
        public void draw(Graphics2D g2d, SpriteManager sm) {
            sm.drawSprite(g2d, spriteID, spriteBounds);
        }

        @Override
        public double getImage(int dimensionID) {
            switch (dimensionID) {
                case Const.X: return spriteBounds.getX();
                case Const.Y: return spriteBounds.getY();
                case Const.WIDTH: return spriteBounds.getWidth();
                case Const.HEIGHT: return spriteBounds.getHeight();
                case Const.ROTATION: return spriteBounds.getRotation();
                case Const.PIVOT_X: return spriteBounds.getPivotX();
                case Const.PIVOT_Y: return spriteBounds.getPivotY();
                default: return 0;
            }
        }

        @Override
        public double getHitbox(int dimensionID) {
            switch (dimensionID) {
                case Const.X: return hitbox.getX();
                case Const.Y: return hitbox.getY();
                case Const.WIDTH: return hitbox.getWidth();
                case Const.HEIGHT: return hitbox.getHeight();
                default: return 0;
            }
        }

        @Override
        public int getLayer() {
            return (int) (getHitbox(Const.Y));
        }
    }
}