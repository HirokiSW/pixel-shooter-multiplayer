/**
    This class represents the game world's background sprites and animations.
    Also adds the hitboxes that serve as barriers and obstacles of the world.
    
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

import java.util.ArrayList;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;

public class WorldBackgrounds {
    private double charFrames, gunFrames, gunIncrements, fountainFrames;
    private ArrayList<ObjectHitbox> blocks;
    private ArrayList<SpriteDrawing> background;

    /**
     * Constructs the WorldBackgrounds object, initalizing all background and barrier data for each map.
     * 
     * @param blocks the list of hitboxes representing solid or impassable objects
     * @param background the list of background sprites
     */
    public WorldBackgrounds(ArrayList<ObjectHitbox> blocks, ArrayList<SpriteDrawing> background) {
        this.blocks = blocks;
        this.background = background;
        
        addLobbyBackgrounds();
        addLobbyBarriers();

        addDungeonBackgrounds();
        addDungeonBarriers();

        addPvpBackgrounds();
        addPvpBarriers();
    }

    /**
     * Adds background drawing logic for the Lobby map, including characters and shadows.
     */
    private void addLobbyBackgrounds() {
        background.add(new SpriteDrawing() {
            @Override
            public void draw(Graphics2D g2d, SpriteManager sd) {
                sd.drawSprite(
                    g2d, Const.LOBBY_BACK_BG, 
                    Const.MAP_START[Const.LOBBY_ID][Const.X], 
                    Const.MAP_START[Const.LOBBY_ID][Const.Y], 
                    4032, 3072
                ); 
                drawCharacterToggleSprites(g2d, sd);
                drawLobbyShadows(g2d, sd);
            }

            /**
             * Draws animated character toggle sprites in the Lobby.
             */
            private void drawCharacterToggleSprites(Graphics2D g2d, SpriteManager sd) {
                for (int i = 0; i < 5; i++) {
                    sd.drawAnimatedSprite(
                        g2d, Const.IDLE_ANIM[Const.KNIGHT_ID + i], 
                        Const.MAP_START[Const.LOBBY_ID][Const.X] + 1992 + 192*i, 
                        Const.MAP_START[Const.LOBBY_ID][Const.Y] + 1816, 
                        Const.CHARACTER_WIDTH, Const.CHARACTER_HEIGHT, 
                        (int) charFrames
                    );
                }
            }

            /**
             * Draws translucent shadows for the Lobby background.
             */
            private void drawLobbyShadows(Graphics2D g2d, SpriteManager sd) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.5f));
                sd.drawSprite(
                    g2d, Const.LOBBY_SHADOWS, 
                    Const.MAP_START[Const.LOBBY_ID][Const.X] + 240, 
                    Const.MAP_START[Const.LOBBY_ID][Const.Y] + 960, 
                    3744, 1680
                );
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
        });
    }

    /**
     * Adds background drawing logic for the Dungeon map, including fountains and guns.
     */
    private void addDungeonBackgrounds() {
        background.add(new SpriteDrawing() {
            @Override
            public void draw(Graphics2D g2d, SpriteManager sd) {
                sd.drawSprite(g2d, Const.DUNGEON_BG, Const.MAP_START[Const.DUNGEON_ID][Const.X], Const.MAP_START[Const.DUNGEON_ID][Const.Y], 1344, 2496); 
                drawGunTogglerSprites(g2d, sd, Const.DUNGEON_ID);
                drawFountainAnimations(g2d, sd);
            }

            /**
             * Draws animated fountain sprites in the Dungeon map.
             */
            private void drawFountainAnimations(Graphics2D g2d, SpriteManager sd) {
                for (int i = 0; i < 2; i++) {
                    sd.drawAnimatedSprite(g2d, Const.RED_FOUNTAIN + i, Const.MAP_START[Const.DUNGEON_ID][Const.X] + 384 + 528*i, Const.MAP_START[Const.DUNGEON_ID][Const.Y] + 1440, 48, 96, (int) fountainFrames);
                }
            }
        });
    }

    /**
     * Adds background drawing logic for the PVP map, including guns.
     */
    private void addPvpBackgrounds() {
        background.add(new SpriteDrawing() {
            @Override
            public void draw(Graphics2D g2d, SpriteManager sd) {
                sd.drawSprite(g2d, Const.PVP_BACK_BG, Const.MAP_START[Const.PVP_ID][Const.X], Const.MAP_START[Const.PVP_ID][Const.Y], 1344, 2304);
                drawGunTogglerSprites(g2d, sd, Const.PVP_ID);
            }
        });
    }
    
    /**
     * Draws gun toggler sprites on the specified map.
     *
     * @param g2d the Graphics2D object to draw on.
     * @param sd the object used to retrieve and manage the sprites that will be used.
     * @param mapID the ID of the map to draw gun sprites on
     */
    protected void drawGunTogglerSprites(Graphics2D g2d, SpriteManager sd, int mapID) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                sd.drawSprite(
                    g2d, Const.REVOLVER_ID + 5*i + j, 
                    Const.MAP_START[mapID][Const.X] + 336 + 576*i + (48 - (Const.GUN_STATS[Const.REVOLVER_ID + 5*i + j][Const.HANDLE_WIDTH] + Const.GUN_STATS[Const.REVOLVER_ID + 5*i + j][Const.BARREL_LENGTH])/2),
                    Const.MAP_START[mapID][Const.Y] + 140 + gunIncrements + 144*j + (48 - (Const.GUN_STATS[Const.REVOLVER_ID + 5*i + j][Const.HANDLE_HEIGHT] + Const.GUN_STATS[Const.REVOLVER_ID + 5*i + j][Const.BARREL_HEIGHT])/2), 
                    Const.GUN_STATS[Const.REVOLVER_ID + 5*i + j][Const.HANDLE_WIDTH] + Const.GUN_STATS[Const.REVOLVER_ID + 5*i + j][Const.BARREL_LENGTH],
                    Const.GUN_STATS[Const.REVOLVER_ID + 5*i + j][Const.HANDLE_HEIGHT] + Const.GUN_STATS[Const.REVOLVER_ID + 5*i + j][Const.BARREL_HEIGHT]
                );
            }
        }
    }

    /**
     * Updates animation frame counters for character sprites, fountains, and gun togglers.
     */
    public void updateSpriteFrames() {
        gunIncrements = (int) (6 * Math.sin(Math.PI * gunFrames));
        gunFrames += 0.025;
        charFrames =  (charFrames + Const.ANIM_SPEED[Const.IDLE_ANIM[Const.KNIGHT_ID]]) % Const.ANIM_FRAMES[Const.IDLE_ANIM[Const.KNIGHT_ID]];
        fountainFrames = (fountainFrames + Const.ANIM_SPEED[Const.RED_FOUNTAIN]) % Const.ANIM_FRAMES[Const.RED_FOUNTAIN];
    }

    /**
     * Adds all barrier hitboxes for the Lobby map.
     */
    private void addLobbyBarriers() {
        blocks.add(new Barrier(Const.LOBBY_ID, 67, 1761, 144, 1059));
        blocks.add(new Barrier(Const.LOBBY_ID, 150, 1150, 352, 784));
        blocks.add(new Barrier(Const.LOBBY_ID, 152, 863, 1311, 352));
        blocks.add(new Barrier(Const.LOBBY_ID, 480, 1824, 336, 191));
        blocks.add(new Barrier(Const.LOBBY_ID, 816, 1824, 24, 287));
        blocks.add(new Barrier(Const.LOBBY_ID, 984, 1824, 24, 287));
        blocks.add(new Barrier(Const.LOBBY_ID, 1004, 1824, 389, 195));
        blocks.add(new Barrier(Const.LOBBY_ID, 160, 2592, 3826, 352));
        blocks.add(new Barrier(Const.LOBBY_ID, 1392, 1824, 24, 517));
        blocks.add(new Barrier(Const.LOBBY_ID, 1045, 575, 1311, 352));
        blocks.add(new Barrier(Const.LOBBY_ID, 1441, 1212, 22, 273));
        blocks.add(new Barrier(Const.LOBBY_ID, 1452, 1294, 249, 191));
        blocks.add(new Barrier(Const.LOBBY_ID, 1681, 1295, 19, 288));
        blocks.add(new Barrier(Const.LOBBY_ID, 1847, 1295, 22, 288));
        blocks.add(new Barrier(Const.LOBBY_ID, 1849, 1295, 252, 191));
        blocks.add(new Barrier(Const.LOBBY_ID, 2090, 862, 1311, 352));
        blocks.add(new Barrier(Const.LOBBY_ID, 2809, 1103, 803, 352));
        blocks.add(new Barrier(Const.LOBBY_ID, 3098, 1373, 141, 658));
        blocks.add(new Barrier(Const.LOBBY_ID, 3206, 1392, 784, 352));
        blocks.add(new Barrier(Const.LOBBY_ID, 3098, 2024, 21, 317));
        blocks.add(new Barrier(Const.LOBBY_ID, 3867, 1391, 141, 658));
        blocks.add(new Barrier(Const.LOBBY_ID, 3962, 1819, 50, 1059));
        blocks.add(new Barrier(Const.LOBBY_ID, 3866, 2032, 22, 273)); 
        blocks.add(new Barrier(Const.LOBBY_ID, 3460, 2113, 19, 285)); 
        blocks.add(new Barrier(Const.LOBBY_ID, 3625, 2113, 19, 285));
        blocks.add(new Barrier(Const.LOBBY_ID, 3228, 2113, 249, 191));
        blocks.add(new Barrier(Const.LOBBY_ID, 3217, 2031, 22, 273));
        blocks.add(new Barrier(Const.LOBBY_ID, 2438, 2161, 660, 180));
        blocks.add(new Barrier(Const.LOBBY_ID, 2269, 2161, 169, 95)); 
        blocks.add(new Barrier(Const.LOBBY_ID, 1788, 2161, 169, 95)); 
        blocks.add(new Barrier(Const.LOBBY_ID, 1415, 2161, 374, 180));
        blocks.add(new Barrier(Const.LOBBY_ID, 1441, 1212, 22, 273));
        blocks.add(new Barrier(Const.LOBBY_ID, 1452, 1294, 249, 191));
        blocks.add(new Barrier(Const.LOBBY_ID, 2090, 1213, 22, 273));
        blocks.add(new Barrier(Const.LOBBY_ID, 1955, 2161, 314, 180));
        blocks.add(new Barrier(Const.LOBBY_ID, 3625, 2114, 252, 191));
    }

    /**
     * Adds all barrier hitboxes for the Dungeon map.
     */
    private void addDungeonBarriers() {
        blocks.add(new Barrier(Const.DUNGEON_ID, 123, -77, 1157, 174));
        blocks.add(new Barrier(Const.DUNGEON_ID, 122, 73, 167, 999));
        blocks.add(new Barrier(Const.DUNGEON_ID, 1056, 95, 275, 933));
        blocks.add(new Barrier(Const.DUNGEON_ID, -194, 850, 770, 630));
        blocks.add(new Barrier(Const.DUNGEON_ID, 768, 850, 782, 630));
        blocks.add(new Barrier(Const.DUNGEON_ID, -193, 1411, 241, 1265));
        blocks.add(new Barrier(Const.DUNGEON_ID, -146, 2436, 1600, 158));
        blocks.add(new Barrier(Const.DUNGEON_ID, 1296, 1411, 211, 1142));
    }

    /**
     * Adds all barrier hitboxes for the PVP map.
     */
    private void addPvpBarriers() {
        blocks.add(new Barrier(Const.PVP_ID, 123, -77, 1157, 174));
        blocks.add(new Barrier(Const.PVP_ID, 122, 73, 167, 999));
        blocks.add(new Barrier(Const.PVP_ID, 1056, 95, 275, 933));
        blocks.add(new Barrier(Const.PVP_ID, -194, 850, 770, 480));
        blocks.add(new Barrier(Const.PVP_ID, 768, 850, 782, 480));
        blocks.add(new Barrier(Const.PVP_ID, -193, 1261, 241, 1265));
        blocks.add(new Barrier(Const.PVP_ID, -146, 2244, 1600, 158));
        blocks.add(new Barrier(Const.PVP_ID, 1296, 1261, 211, 1142));
    }

    /**
     * This class represents a singular rectangular hitbox in the world.
     * Used for walls and obstacles in each map.
     */
    private class Barrier implements ObjectHitbox {
        private ShapeDimensions hitbox;

        /**
         * Constructs a Barrier instance with a position relative to the given map.
         * 
         * @param mapID the ID of the map
         * @param xRelative the x-offset relative to the map's origin
         * @param yRelative the y-offset relative to the map's origin
         * @param width the width of the barrier
         * @param height the height of the barrier
         */
        private Barrier(int mapID, double xRelative, double yRelative, double width, double height) {
            double x = xRelative + Const.MAP_START[mapID][Const.X];
            double y = yRelative + Const.MAP_START[mapID][Const.Y];
            hitbox = new ShapeDimensions(x, y, width, height);
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
    }
}
