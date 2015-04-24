package teamcomm;

import com.jogamp.opengl.GLProfile;
import java.net.SocketException;
import javax.swing.JOptionPane;
import teamcomm.data.GameState;
import teamcomm.gui.MainWindow;
import teamcomm.net.GameControlDataReceiver;
import teamcomm.net.SPLStandardMessageReceiver;

/**
 * @author Felix Thielke
 *
 * The team communication monitor starts in this class.
 */
public class Main {

    private static boolean shutdown = false;
    private static final Object shutdownMutex = new Object();

    /**
     * Startup method of the team communication monitor.
     *
     * @param args This is ignored.
     */
    public static void main(final String[] args) {
        GameControlDataReceiver gcDataReceiver = null;
        SPLStandardMessageReceiver receiver = null;

        // Initialize the JOGL profile for 3D drawing
        GLProfile.initSingleton();

        // Initialize listener for GameController messages
        try {
            gcDataReceiver = new GameControlDataReceiver();
        } catch (SocketException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error while setting up GameController listener.",
                    "SocketException",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }

        // Initialize listeners for robots
        receiver = SPLStandardMessageReceiver.getInstance();

        // Initialize robot view part of the GUI
        final MainWindow robotView = new MainWindow();

        // Start threads
        gcDataReceiver.start();
        receiver.start();
        robotView.start();

        // Wait for shutdown
        try {
            synchronized (shutdownMutex) {
                while (!shutdown) {
                    shutdownMutex.wait();
                }
            }
        } catch (InterruptedException ex) {
        }

        // Shutdown threads
        GameState.getInstance().shutdown();
        receiver.interrupt();
        gcDataReceiver.interrupt();
        robotView.terminate();

        try {
            gcDataReceiver.join(1000);
            receiver.join(100);
        } catch (InterruptedException ex) {
        }

        System.exit(0);
    }

    /**
     * Shuts down the program by notifying the main thread.
     */
    public static void shutdown() {
        synchronized (shutdownMutex) {
            shutdown = true;
            shutdownMutex.notifyAll();
        }
    }
}
