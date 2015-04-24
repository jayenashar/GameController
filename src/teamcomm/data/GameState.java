package teamcomm.data;

import data.GameControlData;
import data.Rules;
import data.SPLStandardMessage;
import data.TeamInfo;
import data.Teams;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.event.EventListenerList;
import teamcomm.PluginLoader;
import teamcomm.data.event.TeamEvent;
import teamcomm.data.event.TeamEventListener;

/**
 * Singleton class managing the known information about communicating robots.
 *
 * @author Felix Thielke
 */
public class GameState {

    /**
     * Index of the team playing on the left side of the field.
     */
    public static final int TEAM_LEFT = 0;
    /**
     * Index of the team playing on the right side of the field.
     */
    public static final int TEAM_RIGHT = 1;
    /**
     * Index of the virtual team containing illegally communicating robots.
     */
    public static final int TEAM_OTHER = 2;

    private static final int CHANGED_LEFT = 1;
    private static final int CHANGED_RIGHT = 2;
    private static final int CHANGED_OTHER = 4;

    private static final GameState instance = new GameState();

    private GameControlData lastGameControlData;

    private final int[] teamNumbers = new int[]{0, 0};
    private final Map<Integer, Integer> teamColors = new HashMap<Integer, Integer>();

    private boolean mirrored = false;

    private final Map<Integer, SortedSet<RobotState>> robots = new HashMap<Integer, SortedSet<RobotState>>();

    private static final Comparator<RobotState> playerNumberComparator = new Comparator<RobotState>() {
        @Override
        public int compare(RobotState o1, RobotState o2) {
            if (o1.getPlayerNumber() == null) {
                if (o2.getPlayerNumber() == null) {
                    return 0;
                }
                return -1;
            } else if (o2.getPlayerNumber() == null) {
                return 1;
            }
            return o1.getPlayerNumber() - o2.getPlayerNumber();
        }
    };

    private final HashMap<String, RobotState> robotsByAddress = new HashMap<String, RobotState>();

    private final EventListenerList listeners = new EventListenerList();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ScheduledFuture<?> taskHandle;

    /**
     * Returns the only instance of the RobotData class.
     *
     * @return instance
     */
    public static GameState getInstance() {
        return instance;
    }

    private GameState() {
        taskHandle = scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int changed = 0;

                synchronized (robotsByAddress) {
                    final Iterator<RobotState> iter = robotsByAddress.values().iterator();
                    while (iter.hasNext()) {
                        final RobotState r = iter.next();
                        if (r.isInactive()) {
                            iter.remove();

                            final SortedSet<RobotState> team = robots.get(r.getTeamNumber());
                            team.remove(r);
                            if (r.getTeamNumber() == teamNumbers[TEAM_LEFT]) {
                                changed |= CHANGED_LEFT;
                            } else if (r.getTeamNumber() == teamNumbers[TEAM_RIGHT]) {
                                changed |= CHANGED_RIGHT;
                            } else {
                                changed |= CHANGED_OTHER;
                            }
                        }
                    }
                }

                sendEvents(changed);
            }
        }, 5, 1, TimeUnit.SECONDS);
    }

    public void shutdown() {
        taskHandle.cancel(false);
    }

    /**
     * Resets all information about robots and teams.
     */
    public void reset() {
        synchronized (teamNumbers) {
            teamNumbers[0] = 0;
            teamNumbers[1] = 0;
        }
        synchronized (robotsByAddress) {
            robots.clear();
            robotsByAddress.clear();
        }
        sendEvents(CHANGED_LEFT | CHANGED_RIGHT | CHANGED_OTHER);
    }

    /**
     * Updates info about the game with a message from the GameController.
     *
     * @param data data sent by the GameController
     */
    public void updateGameData(final GameControlData data) {
        int changed = 0;

        if (data == null) {
            if (lastGameControlData != null) {
                synchronized (teamNumbers) {
                    int s = 0;
                    for (final Entry<Integer, SortedSet<RobotState>> entry : robots.entrySet()) {
                        if (!entry.getValue().isEmpty()) {
                            teamNumbers[s++] = entry.getKey();
                            if (s == 2) {
                                break;
                            }
                        }
                    }
                }
                changed = CHANGED_LEFT | CHANGED_RIGHT | CHANGED_OTHER;
            }
        } else {
            if (lastGameControlData == null) {
                synchronized (teamNumbers) {
                    teamNumbers[TEAM_LEFT] = data.team[0].teamNumber;
                    teamNumbers[TEAM_RIGHT] = data.team[1].teamNumber;
                }
                changed = CHANGED_LEFT | CHANGED_RIGHT | CHANGED_OTHER;
            } else {
                synchronized (teamNumbers) {
                    if (data.team[0].teamNumber != teamNumbers[TEAM_LEFT]) {
                        teamNumbers[TEAM_LEFT] = data.team[0].teamNumber;
                        changed = CHANGED_LEFT | CHANGED_OTHER;
                    }
                    if (data.team[1].teamNumber != teamNumbers[TEAM_RIGHT]) {
                        teamNumbers[TEAM_RIGHT] = data.team[1].teamNumber;
                        changed = CHANGED_RIGHT | CHANGED_OTHER;
                    }
                }
            }

            teamColors.put((int) data.team[0].teamNumber, (int) data.team[0].teamColor);
            teamColors.put((int) data.team[1].teamNumber, (int) data.team[1].teamColor);
        }
        lastGameControlData = data;

        if (changed != 0) {
            // (re)load plugins
            PluginLoader.getInstance().update((int) data.team[0].teamNumber, (int) data.team[1].teamNumber);

            // handle dropin games
            if ((data.team[0].teamNumber == 98 || data.team[0].teamNumber == 99) && (data.team[1].teamNumber == 98 || data.team[1].teamNumber == 99)) {
                Rules.league = Rules.LEAGUES[1];
            } else {
                Rules.league = Rules.LEAGUES[0];
            }

            // send events
            sendEvents(changed);
        }
    }

    /**
     * Handles a message that was received from a robot.
     *
     * @param address IP address of the sender
     * @param teamNumber team number belonging to the port on which the message
     * was received
     * @param message received message
     */
    public void receiveMessage(final String address, final int teamNumber, final SPLStandardMessage message) {
        int changed = 0;

        // update the team info if no GameController info is available
        if (lastGameControlData == null && message != null) {
            synchronized (teamNumbers) {
                for (int i = 0; i < 2; i++) {
                    if (teamNumbers[i] == 0) {
                        teamNumbers[i] = teamNumber;

                        // (re)load plugins
                        PluginLoader.getInstance().update(teamNumber);

                        // handle dropin games
                        if ((teamNumbers[0] == 0 || teamNumbers[0] == 98 || teamNumbers[0] == 99) && (teamNumbers[1] == 0 || teamNumbers[1] == 98 || teamNumbers[1] == 99)) {
                            Rules.league = Rules.LEAGUES[1];
                        } else {
                            Rules.league = Rules.LEAGUES[0];
                        }

                        changed = i + 1 | CHANGED_OTHER;
                        break;
                    } else if (teamNumbers[i] == teamNumber) {
                        break;
                    }
                }
            }
        }

        // create the robot state if it does not yet exist
        RobotState r;
        synchronized (robotsByAddress) {
            r = robotsByAddress.get(address);
            if (r == null) {
                r = new RobotState(address, teamNumber);

                robotsByAddress.put(address, r);
            }

            SortedSet<RobotState> set = robots.get(teamNumber);
            if (set == null) {
                set = new TreeSet<RobotState>(playerNumberComparator);
                robots.put(teamNumber, set);
            }
            if (set.add(r)) {
                if (teamNumbers[TEAM_LEFT] == teamNumber) {
                    changed |= CHANGED_LEFT;
                } else if (teamNumbers[TEAM_RIGHT] == teamNumber) {
                    changed |= CHANGED_RIGHT;
                }
            }
        }

        // let the robot state handle the message
        r.registerMessage(message);

        // send events
        sendEvents(changed);
    }

    private void sendEvents(final int changed) {
        if ((changed & CHANGED_OTHER) != 0) {
            final Collection<RobotState> rs = new LinkedList<RobotState>();
            synchronized (robotsByAddress) {
                for (final Entry<Integer, SortedSet<RobotState>> entry : robots.entrySet()) {
                    if (entry.getKey() == teamNumbers[TEAM_LEFT]) {
                        if ((changed & CHANGED_LEFT) != 0) {
                            fireEvent(new TeamEvent(this, outputSide(TEAM_LEFT), teamNumbers[TEAM_LEFT], new LinkedList<RobotState>(entry.getValue())));
                        }
                    } else if (entry.getKey() == teamNumbers[TEAM_RIGHT]) {
                        if ((changed & CHANGED_RIGHT) != 0) {
                            fireEvent(new TeamEvent(this, outputSide(TEAM_RIGHT), teamNumbers[TEAM_RIGHT], new LinkedList<RobotState>(entry.getValue())));
                        }
                    } else {
                        rs.addAll(entry.getValue());
                    }
                }
            }
            fireEvent(new TeamEvent(this, TEAM_OTHER, 0, rs));
        } else if ((changed & CHANGED_LEFT) != 0) {
            final Collection<RobotState> rs;
            synchronized (robotsByAddress) {
                rs = robots.get(teamNumbers[TEAM_LEFT]);
            }
            fireEvent(new TeamEvent(this, outputSide(TEAM_LEFT), teamNumbers[TEAM_LEFT], rs == null ? new LinkedList<RobotState>() : new LinkedList<RobotState>(rs)));
        } else if ((changed & CHANGED_RIGHT) != 0) {
            final Collection<RobotState> rs;
            synchronized (robotsByAddress) {
                rs = robots.get(teamNumbers[TEAM_RIGHT]);
            }
            fireEvent(new TeamEvent(this, outputSide(TEAM_RIGHT), teamNumbers[TEAM_RIGHT], rs == null ? new LinkedList<RobotState>() : new LinkedList<RobotState>(rs)));
        }
    }

    private void fireEvent(final TeamEvent e) {
        for (final TeamEventListener listener : listeners.getListeners(TeamEventListener.class)) {
            listener.teamChanged(e);
        }
    }

    /**
     * Returns the team numbers of the currently playing teams.
     *
     * @return team numbers of the currently playing teams or null if no info
     * about teams is available
     */
    public int[] getTeamNumbers() {
        if (teamNumbers[0] == 0) {
            return null;
        } else {
            if (mirrored) {
                return new int[]{teamNumbers[1], teamNumbers[0]};
            } else {
                return teamNumbers.clone();
            }
        }
    }

    /**
     * Returns the team color of the given team. The team color is either sent
     * by the game controller or given by the GameController configuration.
     *
     * @param teamNumber number of the team
     * @return the team color
     * @see TeamInfo#teamColor
     */
    public int getTeamColor(final int teamNumber) {
        Integer color = teamColors.get(teamNumber);
        if (color == null) {
            String[] colorStrings = null;
            try {
                colorStrings = Teams.getColors(teamNumber);
            } catch (NullPointerException e) {
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            if (colorStrings == null || colorStrings.length < 1) {
                if (teamNumber == teamNumbers[TEAM_RIGHT]) {
                    return GameControlData.TEAM_RED;
                } else {
                    return GameControlData.TEAM_BLUE;
                }
            } else if (colorStrings[0].equals("blue")) {
                return GameControlData.TEAM_BLUE;
            } else if (colorStrings[0].equals("red")) {
                return GameControlData.TEAM_RED;
            } else if (colorStrings[0].equals("yellow")) {
                return GameControlData.TEAM_YELLOW;
            } else {
                return GameControlData.TEAM_BLACK;
            }
        }

        return color;
    }

    /**
     * Returns the team name of the given team.
     *
     * @param teamNumber number of the team
     * @return the team name
     */
    public String getTeamName(final Integer teamNumber) {
        final String[] teamNames = Teams.getNames(true);
        if (teamNumber != null) {
            if (teamNumber < teamNames.length) {
                return ("Team " + teamNames[teamNumber]);
            } else {
                return ("Unknown Team (" + teamNumber + ")");
            }
        } else {
            return "Unknown Team";
        }
    }

    /**
     * Returns whether the team sides are mirrored.
     *
     * @return boolean
     */
    public boolean isMirrored() {
        return mirrored;
    }

    /**
     * Sets whether the team sides are mirrored.
     *
     * @param mirrored boolean
     */
    public void setMirrored(final boolean mirrored) {
        this.mirrored = mirrored;
    }

    private int outputSide(final int side) {
        return mirrored ? (side == 0 ? 1 : (side == 1 ? 0 : side)) : side;
    }

    public void addListener(final TeamEventListener listener) {
        listeners.add(TeamEventListener.class, listener);
    }

}
