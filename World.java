/**
    This class represents the game world including its backgrounds, props, and interactable objects.
    Also updates the state of the world including animations and interactions.
    
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

public class World {
    private WorldBackgrounds wb;
    private WorldProps wp;
    private WorldInteractables wi;
    
    /**
     * Constructs the World object alongside its different components.
     * @param users the list of all players in the game
     * @param blocks the list of hitboxes representing solid or impassable objects
     * @param interactables the list of interactive objects
     * @param background the list of background sprites
     * @param graphics the list of all graphical sprites
     */
    public World(ArrayList<Player> users, ArrayList<ObjectHitbox> blocks, ArrayList<ObjectInteractable> interactables, ArrayList<SpriteDrawing> background, ArrayList<SpriteGraphic> graphics) {
        wb = new WorldBackgrounds(blocks, background);
        wp = new WorldProps(blocks, graphics);
        wi = new WorldInteractables(users, interactables, background);
    }

    /**
     * Updates the game world's state, including its different animations and interactions.
     */
    public void update() {
        wb.updateSpriteFrames();
        wi.updateTeleportationState();
    }
}
