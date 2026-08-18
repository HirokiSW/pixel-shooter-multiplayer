/**
    This class represents the game world's different interactable objects.
    Adds teleporters and togglers to the world, and facilitates teleportation mechanics.
    
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

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class WorldInteractables {
    private ArrayList<Player> users;
    private ArrayList<ObjectInteractable> interactables;
    private ArrayList<SpriteDrawing> backgrounds;
    private double[] teleportTimer;

    /**
     * Constructs the WorldInteractables object that manages user interaction with
     * togglers and teleporters on the map.
     *
     * @param users the list of players in the world
     * @param interactables the list of interactive objects
     * @param backgrounds the list of background sprites
     */
    public WorldInteractables(ArrayList<Player> users, ArrayList<ObjectInteractable> interactables, ArrayList<SpriteDrawing> backgrounds) {
        this.users = users;
        this.interactables = interactables;
        this.backgrounds = backgrounds;
        teleportTimer = new double[Const.NUMBER_OF_TELEPORTERS];
        addCharacterTogglers();
        addGunTogglers();
        addTeleporters();
        addGlowingTeleporterMarkings();
    }

    /**
     * Adds character toggler objects to the interactable list. 
     * These togglers allow users to change appearance of character.
     */
    private void addCharacterTogglers() {
        for (int i = 0; i < 5; i++) {
            interactables.add(new Toggler(
                Const.LOBBY_ID, 
                Const.MAP_START[Const.LOBBY_ID][Const.X] + 1992 + 192*i, 
                Const.MAP_START[Const.LOBBY_ID][Const.Y] + 1816, 
                Const.CHARACTER_WIDTH, Const.CHARACTER_HEIGHT, i, "character"
            ));
        }
    }

    /**
     * Adds gun toggler objects for primary and secondary weapons in Dungeon and PVP maps.
     * Allows users to change their primary or secondary gun.
     */
    private void addGunTogglers() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                int gunIndex;
                String tag;
                if (i == 0) {
                    gunIndex = Const.REVOLVER_ID + j;
                    tag = "secondary_gun";
                } else {
                    gunIndex = Const.REVOLVER_ID + 5*i + j;
                    tag = "primary_gun";
                }
                double[] gunStats = Const.GUN_STATS[gunIndex];
                double width = gunStats[Const.HANDLE_WIDTH] + gunStats[Const.BARREL_LENGTH];
                double height = gunStats[Const.HANDLE_HEIGHT] + gunStats[Const.BARREL_HEIGHT];
                interactables.add(new Toggler(
                    Const.DUNGEON_ID, 
                    336 + 576*i + (48 - width/2), 
                    140 + 144*j + (48 - height/2), 
                    width, height, gunIndex, tag
                ));
                interactables.add(new Toggler(
                    Const.PVP_ID, 336 + 576*i + (48 - width/2), 
                    140 + 144*j + (48 - height/2), 
                    width, height, gunIndex, tag
                ));
            }
        }
    }

    /**
     * Adds teleporter hitboxes to the world for transitioning between maps.
     */
    private void addTeleporters() {
        interactables.add(new Teleporter(Const.LOBBY_ID, 852, 1296, 288, 192, 0));
        interactables.add(new Teleporter(Const.LOBBY_ID, 1632, 1008, 288, 192, 1));
        interactables.add(new Teleporter(Const.DUNGEON_ID, 528, 384, 288, 192, 2));
        interactables.add(new Teleporter(Const.PVP_ID, 528, 384, 288, 192, 3));
    }

    /**
     * Adds glowing teleport markings to the background, which visually indicate teleport activation progress.
     */
    private void addGlowingTeleporterMarkings() {
        backgrounds.add(new SpriteDrawing() {
            @Override
            public void draw(Graphics2D g2d, SpriteManager sm) {
                g2d.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 
                    (float) (teleportTimer[0] / (Const.TELEPORT_DURATION[0] + 100))
                ));
                sm.drawSprite(
                    g2d, Const.MARKINGS, 
                    Const.MAP_START[Const.LOBBY_ID][Const.X] + 816, 
                    Const.MAP_START[Const.LOBBY_ID][Const.Y] + 1296, 
                    288, 192
                );

                g2d.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 
                    (float) (teleportTimer[1] / (Const.TELEPORT_DURATION[1] + 100))
                ));
                sm.drawSprite(
                    g2d, Const.MARKINGS, 
                    Const.MAP_START[Const.LOBBY_ID][Const.X] + 1632, 
                    Const.MAP_START[Const.LOBBY_ID][Const.Y] + 1008, 
                    288, 192
                );

                g2d.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 
                    (float) (teleportTimer[2] / (Const.TELEPORT_DURATION[2] + 100))
                ));
                sm.drawSprite(
                    g2d, Const.MARKINGS, 
                    Const.MAP_START[Const.DUNGEON_ID][Const.X] + 528, 
                    Const.MAP_START[Const.DUNGEON_ID][Const.Y] + 384, 
                    288, 192
                );

                g2d.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 
                    (float) (teleportTimer[3] / (Const.TELEPORT_DURATION[3] + 100))
                ));
                sm.drawSprite(
                    g2d, Const.MARKINGS, 
                    Const.MAP_START[Const.PVP_ID][Const.X] + 528, 
                    Const.MAP_START[Const.PVP_ID][Const.Y] + 384, 
                    288, 192
                );

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
        });
    }

    /**
     * Updates the state of all teleporters. 
     * Checks if all players are within a teleporter zone and teleports them after a delay.
     */
    public void updateTeleportationState() {
        for (int i = 0; i < teleportTimer.length; i++) {
            boolean willTeleport = true;
            for (Player user : users) {
                if (!user.isCurrentlyInTeleporter(i)) {
                    willTeleport = false;
                    break;
                }
            }
            if (willTeleport) teleportTimer[i] += Const.MILISECONDS_PER_FRAME;
            else teleportTimer[i] = 0;
            if (teleportTimer[i] > Const.TELEPORT_DURATION[i]) teleportPlayers(Const.TELEPORT_DESTINATION[i]);
        }
    }

    /**
     * Teleports all players to a given map ID destination. 
     * Adjusts their position and toggles PVP status.
     *
     * @param mapID the destination map ID to teleport the players to
     */
    private void teleportPlayers(int mapID) {
        for (Player user : users) {
            user.moveX(Const.TELEPORT_SPAWNPOINT[mapID][Const.X] - user.getImage(Const.X) + (user.getPlayerID() - 1)*50);
            user.moveY(Const.TELEPORT_SPAWNPOINT[mapID][Const.Y] - user.getImage(Const.Y));
            user.setPVP(mapID != Const.LOBBY_ID);
        }
    }
    
    /**
     * Class representing a teleporter zone in the world.
     * When all players are in a teleporter zone for long enough, they are transported to a different map.
     */
    private class Teleporter implements ObjectInteractable {
        private ShapeDimensions hitbox;
        private int id;
        private boolean[] isColliding;

        /**
         * Creates a Teleporter object at the specified position relative to the map.
         *
         * @param mapID the ID of the map where the teleporter is placed
         * @param xRelative the x-coordinate relative to the map
         * @param yRelative the y-coordinate relative to the map
         * @param width the width of the teleporter hitbox
         * @param height the height of the teleporter hitbox
         * @param id the ID representing which teleporter this is
         */
        private Teleporter(int mapID, double xRelative, double yRelative, double width, double height, int id) {
            double x = xRelative + Const.MAP_START[mapID][Const.X];
            double y = yRelative + Const.MAP_START[mapID][Const.Y];
            hitbox = new ShapeDimensions(x, y, width, height);
            isColliding = new boolean[4];
            this.id = id;
        }

        @Override
        public void detectCollision(Player o) {
            double left = getHitbox(Const.X);
            double right = left + getHitbox(Const.WIDTH);
            double top = getHitbox(Const.Y);
            double bottom = top + getHitbox(Const.HEIGHT);

            double otherLeft = o.getHitbox(Const.X);
            double otherRight = otherLeft + o.getHitbox(Const.WIDTH);
            double otherTop = o.getHitbox(Const.Y);
            double otherBottom = otherTop + o.getHitbox(Const.HEIGHT);

            boolean withinY = !(top >= otherBottom || bottom <= otherTop);
            boolean withinX = !(left >= otherRight || right <= otherLeft);
            
            if (withinY) {
                if (!isColliding[Const.LEFT]) isColliding[Const.LEFT] = (left < otherRight) && (right > otherRight);
                if (!isColliding[Const.RIGHT]) isColliding[Const.RIGHT] = (right > otherLeft) && (left < otherLeft);
            }
            if (withinX) {
                if (!isColliding[Const.UP]) isColliding[Const.UP] = (top < otherBottom) && (bottom > otherBottom);
                if (!isColliding[Const.DOWN]) isColliding[Const.DOWN] = (bottom > otherTop) && (top < otherTop);
            }

            if (isColliding[Const.LEFT] || isColliding[Const.RIGHT] || isColliding[Const.UP] || isColliding[Const.DOWN]) o.setInTeleporter(id, true);
            else o.setInTeleporter(id, false);
            resetCollisions();
        }

        /**
         * Resets the collision states.
         */
        private void resetCollisions() {
            for (int i = 0; i < isColliding.length; i++) isColliding[i] = false;
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

    /**
     * Class representing a toggler object.
     * When a player collides with the toggler, their character or gun changes.
     */
    private class Toggler implements ObjectInteractable {
        private ShapeDimensions hitbox;
        private int id;
        private String type;
        private boolean[] isColliding;

        /**
         * Creates a Toggler object at the specified position relative to the map.
         *
         * @param mapID the ID of the map where the toggler is placed
         * @param xRelative the x-coordinate relative to the map
         * @param yRelative the y-coordinate relative to the map
         * @param width the width of the toggler hitbox
         * @param height the height of the toggler hitbox
         * @param id the ID of the character or weapon
         * @param type the type of toggler
         */
        private Toggler(int mapID, double xRelative, double yRelative, double width, double height, int id, String type) {
            double x = xRelative + Const.MAP_START[mapID][Const.X];
            double y = yRelative + Const.MAP_START[mapID][Const.Y];
            hitbox = new ShapeDimensions(x, y, width, height);
            isColliding = new boolean[4];
            this.id = id;
            this.type = type;
        }

        @Override
        public void detectCollision(Player o) {
            double left = getHitbox(Const.X);
            double right = left + getHitbox(Const.WIDTH);
            double top = getHitbox(Const.Y);
            double bottom = top + getHitbox(Const.HEIGHT);

            double otherLeft = o.getHitbox(Const.X);
            double otherRight = otherLeft + o.getHitbox(Const.WIDTH);
            double otherTop = o.getHitbox(Const.Y);
            double otherBottom = otherTop + o.getHitbox(Const.HEIGHT);

            boolean withinY = !(top >= otherBottom || bottom <= otherTop);
            boolean withinX = !(left >= otherRight || right <= otherLeft);
            
            if (withinY) {
                if (!isColliding[Const.LEFT]) isColliding[Const.LEFT] = (left < otherRight) && (right > otherRight);
                if (!isColliding[Const.RIGHT]) isColliding[Const.RIGHT] = (right > otherLeft) && (left < otherLeft);
            }
            if (withinX) {
                if (!isColliding[Const.UP]) isColliding[Const.UP] = (top < otherBottom) && (bottom > otherBottom);
                if (!isColliding[Const.DOWN]) isColliding[Const.DOWN] = (bottom > otherTop) && (top < otherTop);
            }

            if (isColliding[Const.LEFT] || isColliding[Const.RIGHT] || isColliding[Const.UP] || isColliding[Const.DOWN]) {
                if (type.equals("character")) o.toggleCharacter(id);
                else if (type.equals("primary_gun")) o.getGun().toggleGun(0, id);
                else if (type.equals("secondary_gun")) o.getGun().toggleGun(1, id);
            }
            resetCollisions();
        }

        /**
         * Resets the collision states.
         */
        private void resetCollisions() {
            for (int i = 0; i < isColliding.length; i++) isColliding[i] = false;
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
