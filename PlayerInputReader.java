/**
    This class is responsible for accepting keyboard and mouse input data from the ReadFromServer.
    The accepted inputs are used to manipulate player data for moving, shooting, and other actions.

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

public class PlayerInputReader {
    private Player user;
    private PlayerGun gun;
    
    /**
     * Constructs a PlayerInputReader object for the given Player.
     * 
     * @param p the player this input reader is manipulating
     */
    public PlayerInputReader(Player user) {
        this.user = user;
        gun = user.getGun();
    }

    /**
     * Applies key press and release states to control movement and actions.
     *
     * @param pressedFlag a boolean array representing the states
     */
    public void xKeyPressed(boolean[] pressedFlag) {
        if (pressedFlag[Const.UP]) user.moving(Const.UP);
        else user.stopMoving(Const.UP);
        if (pressedFlag[Const.LEFT]) user.moving(Const.LEFT);
        else user.stopMoving(Const.LEFT);
        if (pressedFlag[Const.DOWN]) user.moving(Const.DOWN);
        else user.stopMoving(Const.DOWN);
        if (pressedFlag[Const.RIGHT]) user.moving(Const.RIGHT);
        else user.stopMoving(Const.RIGHT);

        if (pressedFlag[Const.SWITCH]) gun.setSwitching(true); 
        if (pressedFlag[Const.DASH]) user.setDodging(true);
        if (pressedFlag[Const.RELOAD]) gun.setReloading(true);
    }

    /**
     * Updates the player's mouse coordinates for proper aiming.
     *
     * @param mouseCoords an array containing the mouse X and Y coordinates
     */
    public void xMouseMoved(double[] mouseCoords) {
        user.setMouse(mouseCoords[Const.X], mouseCoords[Const.Y]);
    }

    /**
     * Updates the shooting state of the player based on mouse press.
     *
     * @param mouseFlag true if mouse is pressed, else false
     */
    public void xMousePressed(boolean mouseFlag) {
        if (gun.getGunStat(Const.BULLETS_LEFT) > 0 && mouseFlag) gun.setShooting(true);
        else gun.setShooting(false);
    }

    /**
     * Corrects the player's coordinates based on server data.
     *
     * @param playerCoords an array containing the player's X and Y position
     */
    public void xSetCoordinates(double[] playerCoords) {
        if (playerCoords[Const.X] == 0 && playerCoords[Const.Y] == 0) return;
        user.adjustPosition(playerCoords[Const.X], playerCoords[Const.Y]);
    }

    /**
     * Corrects the player's health value based on server data.
     *
     * @param playerHealth the new health value to set
     */
    public void xSetHealth(double playerHealth) {
        user.setHealth(playerHealth);
    }
}
