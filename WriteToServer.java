/**
    The class that handles the storing, handling, and writing of data to the server.
    Continuously sends input and correctional data to the server.
    
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

import java.io.DataOutputStream;
import java.io.IOException;

public class WriteToServer {
    private DataOutputStream dataOut;
    private boolean[] pressedFlags = new boolean[7];
    private double[] playerCoords = new double[2];
    private double[] mouseCoords = new double[2];
    private boolean mousePressedFlag;
    private double playerHealth;

    /**
     * Constructs a WriteToServerObject for writing input and correctional data.
     *
     * @param out the DataOutputStream used to write data to the server.
     */
    public WriteToServer(DataOutputStream out) {
        dataOut = out;
        System.out.println("> WTS Runnable created");
        playerHealth = Const.CHARACTER_HP;
    }

    /**
     * Updates the flags for a specific key press or release.
     *
     * @param keyCode the index representing the specific key.
     * @param pressed true if key is pressed, false if released.
     */
    public void writeKeyStatus(int keyCode, boolean pressed) {
        pressedFlags[keyCode] = pressed;
    }

    /**
     * Updates the coordinates of the player's position for correction.
     *
     * @param playerX the player's x-coordinate.
     * @param playerY the player's y-coordinate.
     */
    public void writePlayerCoords(double playerX, double playerY) {
        playerCoords[Const.X] = playerX;
        playerCoords[Const.Y] = playerY;
    }

    /**
     * Updates the coordinates of the mouse position.
     *
     * @param mouseX the x-coordinate of the mouse.
     * @param mouseY the y-coordinate of the mouse.
     */
    public void writeMouseCoords(double mouseX, double mouseY) {
        mouseCoords[Const.X] = mouseX;
        mouseCoords[Const.Y] = mouseY;
    }

    /**
     * Updates the flags for mouse press.
     *
     * @param pressed true if mouse is pressed, false if released.
     */
    public void writeMouseStatus(boolean pressed) {
        mousePressedFlag = pressed;
    }

    /**
     * Updates the value of the player's current health for correction.
     *
     * @param hp the player's current health value.
     */
    public void writePlayerHealth(double hp) {
        playerHealth = hp;
    }

    /**
     * Sends the current input state and correctional data of the player to the server.
     */
    public void writeInputToServer() {
        try {
            for (int j = 0; j < pressedFlags.length; j++) {
                dataOut.writeInt(Const.TYPE_BUTTON_PRESSED);
                dataOut.writeInt(j);
                dataOut.writeBoolean(pressedFlags[j]);
            }
            for (int j = 0; j < mouseCoords.length; j++) {
                dataOut.writeInt(Const.TYPE_MOUSE_COORDS);
                dataOut.writeInt(j);
                dataOut.writeDouble(mouseCoords[j]);
            }
            dataOut.writeInt(Const.TYPE_MOUSE_PRESSED);
            dataOut.writeBoolean(mousePressedFlag);
            for (int j = 0; j < playerCoords.length; j++) {
                dataOut.writeInt(Const.TYPE_PLAYER_COORDS);
                dataOut.writeInt(j);
                dataOut.writeDouble(playerCoords[j]);
            }
            dataOut.writeInt(Const.TYPE_PLAYER_HEALTH);
            dataOut.writeDouble(playerHealth);
            dataOut.flush();
        } catch (IOException ex) {
            System.out.println("IOException from WTS writeInputToServer()");
        }
    }
}
