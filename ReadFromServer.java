/**
    The class that handles the reading of data from the server.
    Continuously listens for server messages and updates local player information using PlayerInputReader.
    
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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ReadFromServer implements Runnable {
    
    private DataInputStream dataIn;
    private ArrayList<Player> users;
    private ArrayList<PlayerInputReader> userInputs;
    private int mainID;

    private boolean[][] allPressedFlags;
    private double[][] allPlayerCoords;
    private double[][] allMouseCoords;
    private boolean[] allMousePressedFlags;
    private double[] allPlayerHealth;

    /**
     * Constructs a ReadFromServer object with different player data containers depending on maxPlayers,
     * As well as different PlayerInputReaders to relay collected information for updating.
     *
     * @param playerID the ID of the main player.
     * @param in the DataInputStream used to read messages from the server.
     * @param users the list of all players in the server.
     * @param userInputs the list of PlayerInputReader objects to update each player's state.
     * @param maxPlayers the number of players in the game.
     */
    public ReadFromServer(int playerID, DataInputStream in, ArrayList<Player> users, ArrayList<PlayerInputReader> userInputs, int maxPlayers) {
        mainID = playerID;
        dataIn = in;
        this.users = users;
        this.userInputs = userInputs;
        allPressedFlags = new boolean[maxPlayers][7];
        allPlayerCoords = new double[maxPlayers][2];
        allMouseCoords = new double[maxPlayers][2];
        allMousePressedFlags = new boolean[maxPlayers];
        allPlayerHealth = new double[maxPlayers];
        System.out.println("> RFS Runnable created");
    }

    /**
     * Continuously reads data from the server and updates input states for each player.
     * This includes keyboard input and mouse input, as well as coordinates, and health for data correction.
     */
    @Override
    public void run() {
        try {
            while (true) {
                int playerID = dataIn.readInt();
                int dataType = dataIn.readInt();
                switch (dataType) {
                    case Const.TYPE_BUTTON_PRESSED: {
                        int dataIndex = dataIn.readInt();
                        allPressedFlags[playerID][dataIndex] = dataIn.readBoolean();
                        break;
                    }
                    case Const.TYPE_MOUSE_COORDS: {
                        int dataIndex = dataIn.readInt();
                        allMouseCoords[playerID][dataIndex] = dataIn.readDouble();
                        break;
                    }
                    case Const.TYPE_MOUSE_PRESSED: {
                        allMousePressedFlags[playerID] = dataIn.readBoolean();
                        break;
                    }
                    case Const.TYPE_PLAYER_COORDS: {
                        int dataIndex = dataIn.readInt();
                        allPlayerCoords[playerID][dataIndex] = dataIn.readDouble();
                        break;
                    }
                    case Const.TYPE_PLAYER_HEALTH: {
                        allPlayerHealth[playerID] = dataIn.readDouble();
                        break;
                    }
                }

                for (int i = 0; i < users.size(); i++) {
                    userInputs.get(i).xKeyPressed(allPressedFlags[i]);
                    userInputs.get(i).xMouseMoved(allMouseCoords[i]);
                    userInputs.get(i).xMousePressed(allMousePressedFlags[i]);
                    // Only corrects coordinates and health for other players
                    if (mainID != i) {
                        userInputs.get(i).xSetCoordinates(allPlayerCoords[i]);
                        userInputs.get(i).xSetHealth(allPlayerHealth[i]);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("IOException from RFS run()");
        }
    }

    /**
     * Waits for the start message from the server before initiating the data reading loop.
     * Also reads an array of random variables from the server to generate a game seed.
     */
    public void waitForStartMsg() {
        try {
            String startMsg = dataIn.readUTF();
            System.out.println("> Message from server: " + startMsg);
            double[] randomVariables = new double[100];
            for (int i = 0; i < 100; i++) {
                randomVariables[i] = dataIn.readDouble();
            }
            Const.setRandomVariables(randomVariables);
            Thread readThread = new Thread(this);
            readThread.start();
        } catch (IOException ex) {
            System.out.println("IOException from waitForStartMsg()");
        }
    }
}
