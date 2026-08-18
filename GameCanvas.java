/**
    This class is responsible for rendering and managing background, players, projectiles, and interactables.
    It also manages user input, updates game object states, and manages network communication.

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
**/

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.JComponent;

public class GameCanvas extends JComponent {
    private Player main;
    private ArrayList<Player> users;
    private ArrayList<PlayerInputReader> irs;
    
    private ArrayList<ObjectHitbox> blocks;
    private ArrayList<ObjectInteractable> interactables;
    private ArrayList<ObjectEntity> players;
    private ArrayList<ObjectProjectile> projectiles;

    private SpriteManager sm;
    private ArrayList<SpriteDrawing> background;
    private ArrayList<SpriteGraphic> graphics;
    private ArrayList<SpriteGraphic> projectileGraphics;

    private World world;
    private int playerID, maxPlayers;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;

    /**
     * Constructs the GameCanvas.
     * Sets up hitboxes, sprites, players, player controls, and the drawing canvas.
     *
     * @param playerID the ID of the player using this canvas.
     * @param in the DataInputStream to read server data.
     * @param out the DataOutputStream to send data to the server.
     * @param maxPlayers the total number of players in the game.
     */
    public GameCanvas(int playerID, DataInputStream in, DataOutputStream out, int maxPlayers) {
        this.playerID = playerID;
        this.maxPlayers = maxPlayers;
        setPreferredSize(new Dimension(Const.FRAME_WIDTH, Const.FRAME_HEIGHT));
        setUpHitboxes();
        setUpSprites();
        setUpPlayers();        
        wtsRunnable = new WriteToServer(out);
        setUpControls();
        rfsRunnable = new ReadFromServer(playerID, in, users, irs, maxPlayers);
        rfsRunnable.waitForStartMsg();
        setUpDrawingCanvas();
    }

    /**
     * Initializes all hitbox-related object lists.
     */
    private void setUpHitboxes() {
        interactables = new ArrayList<>();
        blocks = new ArrayList<>();
        players = new ArrayList<>();
        projectiles = new ArrayList<>();
    }

    /**
     * Initializes the sprite manager and sprite-related lists.
     */
    private void setUpSprites() {
        sm = new SpriteManager();
        background = new ArrayList<>();
        graphics = new ArrayList<>();
        projectileGraphics = new ArrayList<>();
    }

    /**
     * Initializes and sets up player objects and assigns main player.
     */
    private void setUpPlayers() {
        users = new ArrayList<>();
        for (int i = 0; i < maxPlayers; i++) {
            Player newUser = new Player(i, i, Const.ASSAULT_ID, Const.REVOLVER_ID, projectiles, projectileGraphics);
            users.add(newUser);
        }
        main = users.get(playerID);
    }

    /**
     * Configures player controls, input reading, and listeners for the main player.
     */
    private void setUpControls() {
        irs = new ArrayList<>();
        main.createController(wtsRunnable);
        for (Player user : users) {
            user.simulateController(rfsRunnable);
            irs.add(user.getInputReader());
        }
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(main.getController());
        addMouseListener(main.getController());
        addMouseMotionListener(main.getController());
    }

    /**
     * Sets up the drawing canvas with graphical layers and world object that contains all drawable objects.
     */
    private void setUpDrawingCanvas() {
        for (Player user : users) {
            players.add(user);
            graphics.add(user);
            graphics.add(user.getGun());
        }
        world = new World(users, blocks, interactables, background, graphics);
    }

    /**
     * Custom paint method to render the entire game view.
     * Also shifts perspective of the camera to the main player.
     * Cleans up projectiles and entities.
     * @param g the Graphics object to draw on.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        AffineTransform revert = g2d.getTransform();

        shiftPerspective(g2d);
        drawLayeredGraphics(g2d);
        cleanUpProjectilesAndEntities();
        
        g2d.setTransform(revert);
        main.getStats().draw(g2d, sm);
    }

    /**
     * Shifts the camera to follow the main player's perspective.
     * @param g2d the Graphics2D object to draw on.
     */
    private void shiftPerspective(Graphics2D g2d) {
        g2d.fillRect(0, 0, Const.FRAME_WIDTH, Const.FRAME_WIDTH);
        g2d.translate(main.getCameraPerspective(Const.X), main.getCameraPerspective(Const.Y));
    }

    /**
     * Draws all the graphical elements based on their layer value.
     * @param g2d the Graphics2D object to draw on.
     */
    private void drawLayeredGraphics(Graphics2D g2d) {
        for (SpriteDrawing bg : background) {
            bg.draw(g2d, sm);
        }
        ArrayList<SpriteGraphic> allGraphics = new ArrayList<>();
        allGraphics.addAll(graphics);
        allGraphics.addAll(projectileGraphics);
        allGraphics.sort(Comparator.comparingInt(p -> p.getLayer()));
        for (SpriteGraphic graphic : allGraphics) {
            graphic.draw(g2d, sm);
        }
    }

    /**
     * Removes finished projectiles and entities and their sprites from the game.
     */
    private void cleanUpProjectilesAndEntities() {
        for (int i = 0; i < projectiles.size(); i++) {
            if (projectiles.get(i).hasFinished()) {
                projectiles.remove(i);
                projectileGraphics.remove(i);
                i--;
            }
        }
    }

    /**
     * Updates all player and projectile positions, checks collisions, and writes the main player's info to server.
     */
    public void updatePositions() {
        checkAllCollisions();
        for (Player user : users) {
            user.updatePosition();
            user.getGun().update();
        }
        for (ObjectProjectile projectile : projectiles) {
            projectile.updatePosition();
        }
        world.update();
        writePlayerInformation();
    }

    /**
     * Sends current player input, position, and health to the server.
     */
    private void writePlayerInformation() {
        wtsRunnable.writeInputToServer();
        wtsRunnable.writePlayerCoords(main.getImage(Const.X), main.getImage(Const.Y));
        wtsRunnable.writePlayerHealth(main.getHP());
    }

    /**
     * Checks and handles collisions between players, blocks, interactables, and projectiles.
     */
    private void checkAllCollisions() {
        for (ObjectInteractable interactable : interactables) {
            for (Player user : users) {
                interactable.detectCollision(user);
            }
        }

        for (ObjectEntity player : players) {
            for (ObjectHitbox block : blocks) {
                player.detectCollision(block);
            }
        }
        for (ObjectProjectile projectile : projectiles) {
            for (ObjectHitbox block : blocks) {
                if (!projectile.hasCollided()) projectile.detectCollision(block);
            }
            for (ObjectEntity player : players) {
                if (
                    player.isDead() || 
                    !((Player) player).isInPVP() ||
                    projectile.getShooter().equals(player) || 
                    ((Player) player).isDodging() ||
                    projectile.hasCollided()
                ) continue;
                projectile.detectCollision(player);
            }
        }
    }
}
