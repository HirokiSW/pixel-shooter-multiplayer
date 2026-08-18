/**
    This class handles the player connections, receives input from clients.
    Continuously sends updates in game state back to all clients.

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

import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class GameServer {
    private ServerSocket ss;
    private int numPlayers;
    public static int maxPlayers;

    private ActionListener process;
    private Timer updating;

    private double[] randomVariables = new double[100];
    private ArrayList<Socket> sockets = new ArrayList<>();
    private ArrayList<ReadFromClient> rfcRunnables = new ArrayList<>();
    private ArrayList<WriteToClient> wtcRunnables = new ArrayList<>();

    private boolean[][] allPressedFlags;
    private double[][] allPlayerCoords;
    private double[][] allMouseCoords;
    private boolean[] allMousePressedFlags;
    private double[] allPlayerHealth;

    /**
     * Constructs the server, initializes random data for the game constants
     * and initializes arrays for player input and game state.
     */
    public GameServer() {
        System.out.println("-== GAME SERVER ==-");
        try {
            ss = new ServerSocket(52223);
        } catch (IOException ex) {
            System.out.println("! IOException from GameServer constructor");
        }
        System.out.println("> THE GAME SERVER HAS BEEN CONNECTED");
        for (int i = 0; i < 100; i++) {
            randomVariables[i] = Math.random();
        }
        allPressedFlags = new boolean[maxPlayers][7];
        allPlayerCoords = new double[maxPlayers][2];
        allMouseCoords = new double[maxPlayers][2];
        allMousePressedFlags = new boolean[maxPlayers];
        allPlayerHealth = new double[maxPlayers];
    }

    /**
     * Waits for incoming client connections until the maximum number of players is reached.
     * Sets up communication threads for each client after max players are reached.
     */
    public void waitForConnections() {
        try {
            System.out.println("> WAITING FOR CONNECTIONS...");
            while (numPlayers < maxPlayers) {
                Socket socket = ss.accept();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeInt(numPlayers);
                out.writeInt(maxPlayers);
                numPlayers++;
                System.out.println("> Player " + numPlayers + " has joined the server");
                ReadFromClient rfc = new ReadFromClient(numPlayers - 1, in);
                WriteToClient wtc = new WriteToClient(numPlayers - 1, out);

                sockets.add(socket);
                rfcRunnables.add(rfc);
                wtcRunnables.add(wtc);

                if (numPlayers == maxPlayers) {
                    for (WriteToClient wtcRunnable : wtcRunnables) {
                        wtcRunnable.sendStartMsg();
                    }
                    ArrayList<Thread> readThreads = new ArrayList<>();
                    for (ReadFromClient rfcRunnable : rfcRunnables) {
                        readThreads.add(new Thread(rfcRunnable));
                    }
                    for (Thread readThread : readThreads) {
                        readThread.start();
                    }
                    setUpTimer();
                    updating.start();
                }
            }
            System.out.println("> NO LONGER ACCEPTING CONNECTIONS");
        } catch (IOException ex) {
            System.out.println("IOException from waitForConnections method");
        }
    }

    /**
     * Sets up the loop that continuously sends updates to all clients.
     */
    public void setUpTimer() {
        process = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (WriteToClient wtcRunnable : wtcRunnables) {
                    wtcRunnable.write();
                }
            }
        };
        updating = new Timer(Const.MILISECONDS_PER_FRAME, process);
    }

    /**
     * This runnable class is responsible for reading player input from a client.
     */
    private class ReadFromClient implements Runnable {

        private DataInputStream dataIn;
        private int playerID;

        /**
         * Constructs a reader for a specific player ID.
         *
         * @param pID player ID.
         * @param in data input stream from player.
         */
        public ReadFromClient(int pID, DataInputStream in) {
            playerID = pID;
            dataIn = in;
            System.out.println("> RFC#" + (pID + 1) + " Runnable created");
        }

        /**
         * Continuosly accepts data and updates stored data containers to be sent to each client.
         */
        @Override
        public void run() {
            try {
                while (true) {
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
                }
            } catch (IOException ex) {
                System.out.println("IOException from RFC run()");
            }
        }
    }

    /**
     * This class is responsible for sending game state data to a specific client.
     */
    private class WriteToClient {

        private DataOutputStream dataOut;

        /**
         * Constructs a writer for a specific player.
         *
         * @param pID player ID.
         * @param out data output stream to the player.
         */
        public WriteToClient(int pID, DataOutputStream out) {
            dataOut = out;
            System.out.println("> WTC#" + (pID + 1) + " Runnable created");
        }

        /**
         * Sends current state data of all players to the client.
         */
        public void write() {
            try {
                for (int playerID = 0; playerID < maxPlayers; playerID++) {
                    for (int j = 0; j < allPressedFlags[0].length; j++) {
                        dataOut.writeInt(playerID);
                        dataOut.writeInt(Const.TYPE_BUTTON_PRESSED);
                        dataOut.writeInt(j);
                        dataOut.writeBoolean(allPressedFlags[playerID][j]);
                    }
                    for (int j = 0; j < allMouseCoords[0].length; j++) {
                        dataOut.writeInt(playerID);
                        dataOut.writeInt(Const.TYPE_MOUSE_COORDS);
                        dataOut.writeInt(j);
                        dataOut.writeDouble(allMouseCoords[playerID][j]);
                    }
                    dataOut.writeInt(playerID);
                    dataOut.writeInt(Const.TYPE_MOUSE_PRESSED);
                    dataOut.writeBoolean(allMousePressedFlags[playerID]);

                    for (int j = 0; j < allPlayerCoords[0].length; j++) {
                        dataOut.writeInt(playerID);
                        dataOut.writeInt(Const.TYPE_PLAYER_COORDS);
                        dataOut.writeInt(j);
                        dataOut.writeDouble(allPlayerCoords[playerID][j]);
                    }

                    dataOut.writeInt(playerID);
                    dataOut.writeInt(Const.TYPE_PLAYER_HEALTH);
                    dataOut.writeDouble(allPlayerHealth[playerID]);
                }
                dataOut.flush();
            } catch (IOException ex) {
                updating.stop();
                System.out.println("IOException from WTC run()");
            }
        }

        /**
         * Sends a notice along with an array of random values to the client once max players is reached.
         */
        public void sendStartMsg() {
            try {
                dataOut.writeUTF("Server has reached max players");
                for (int i = 0; i < 100; i++) {
                    dataOut.writeDouble(randomVariables[i]);
                }
            } catch (IOException ex) {
                System.out.println("IOException from sendStartMsg()");
            }
        }
    }

    /**
     * Main method. Accepts input for max amount of players (1 - 4 players).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of players (1-4): ");
        maxPlayers = Math.clamp(scanner.nextInt(), 1, 4);
        scanner.close();

        GameServer gs = new GameServer();
        gs.waitForConnections();
    }
}