/**
    This class is responsible for accepting keyboard and mouse inputs.
    The accepted inputs are then sent to WriteToServer as flags and data.

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

import java.awt.event.*;

public class PlayerController implements MouseListener, KeyListener, MouseMotionListener {
    private PlayerGun gun;
    private WriteToServer wts;
    
    /**
     * Constructs a PlayerController object for the given Player.
     * and gives it a WriteToServer object to communicate inputs to server.
     * 
     * @param p the player this controller is managing
     * @param wts the object used to send player input data to the server
     */
    public PlayerController(Player p, WriteToServer wts) {
        this.wts = wts;
        gun = p.getGun();
    }

    /**
     * Called when a key is pressed. 
     * Sends movement and action key statuses to the server.
     * 
     * @param e KeyEvent containing the key code
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int pressed = e.getKeyCode();
        if (pressed == KeyEvent.VK_W) wts.writeKeyStatus(Const.UP, true);
        if (pressed == KeyEvent.VK_A) wts.writeKeyStatus(Const.LEFT, true);
        if (pressed == KeyEvent.VK_S) wts.writeKeyStatus(Const.DOWN, true);
        if (pressed == KeyEvent.VK_D) wts.writeKeyStatus(Const.RIGHT, true);

        if (pressed == KeyEvent.VK_Q) wts.writeKeyStatus(Const.SWITCH, true);
        if (pressed == KeyEvent.VK_E) wts.writeKeyStatus(Const.DASH, true);
        if (pressed == KeyEvent.VK_R) wts.writeKeyStatus(Const.RELOAD, true);
    }

    /**
     * Called when a key is released. 
     * Sends movement and action key statuses to the server.
     * 
     * @param e KeyEvent containing the key code
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int released = e.getKeyCode();
        if (released == KeyEvent.VK_W) wts.writeKeyStatus(Const.UP, false);
        if (released == KeyEvent.VK_A) wts.writeKeyStatus(Const.LEFT, false);
        if (released == KeyEvent.VK_S) wts.writeKeyStatus(Const.DOWN, false);
        if (released == KeyEvent.VK_D) wts.writeKeyStatus(Const.RIGHT, false);

        if (released == KeyEvent.VK_Q) wts.writeKeyStatus(Const.SWITCH, false);
        if (released == KeyEvent.VK_E) wts.writeKeyStatus(Const.DASH, false);
        if (released == KeyEvent.VK_R) wts.writeKeyStatus(Const.RELOAD, false);
    }

    /**
     * Called when mouse is moved.
     * Sends mouse coordinates to the server. 
     * 
     * @param e MouseEvent containing the mouse coordinates
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        wts.writeMouseCoords(e.getX(), e.getY());
    }

    /**
     * Called when the mouse is dragged. 
     * Treated the same as mouseMoved.
     *
     * @param e MouseEvent containing the mouse coordinates
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    /**
     * Called when a mouse button is pressed. 
     * If the gun has bullets, initiates firing.
     *
     * @param e the MouseEvent containing the click status
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (gun.getGunStat(Const.BULLETS_LEFT) > 0) wts.writeMouseStatus(true);
    }

    /**
     * Called when a mouse button is released. 
     * Stops the player from firing.
     *
     * @param e the MouseEvent containing the click status
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        wts.writeMouseStatus(false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}

}