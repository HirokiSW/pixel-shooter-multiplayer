/**
    The main window for the game. Handles initial connection to the server,
    the initialization of the game canvas, and the sets up the animation loop.
    
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

import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class GameFrame extends JFrame {

    private GameCanvas gc;
    private ActionListener process;
    private Timer updating;
    public int maxPlayers;

    private Socket playerSocket;
    private int playerID;

    /** 
     * Default constructor for the GameFrame. 
     */
    public GameFrame() {}

    /**
     * Initializes the game window, sets up the user interface and starts the animation loop.
     */
    public void setUpGUI() {
        this.setTitle("Pixel Shooter Demo - Bugged Edition");
        this.add(gc);
        this.pack();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        setUpAnimationTimer();
    }

    /**
     * Accepts a String input from terminal for the server IP.
     * Establishes a connection to the game server and prepares input/output streams.
     * It then initializes GameCanvas with the assigned player ID and total number of players.
     */
    public void connectToServer() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter server IP address: ");
            playerSocket = new Socket(scanner.nextLine(), 52223);
            scanner.close();
            DataInputStream in = new DataInputStream(playerSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(playerSocket.getOutputStream());

            playerID = in.readInt();     // Receive assigned player ID
            maxPlayers = in.readInt();   // Receive total player count

            System.out.println("> Assigned as player " + (playerID + 1));

            gc = new GameCanvas(playerID, in, out, maxPlayers);
        } catch (IOException ex) {
            System.out.println("! IOException from connectToServer()");
        }
    }

    /**
     * Sets up the main animation loop for the updating and rendering.
     */
    private void setUpAnimationTimer() {
        process = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gc.updatePositions();
                gc.repaint();
            }
        };
        updating = new Timer(Const.MILISECONDS_PER_FRAME, process);
        updating.start();
    }
}