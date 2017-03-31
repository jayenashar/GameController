package controller.ui.ui.components;

import controller.action.ActionBoard;
import controller.net.RobotOnlineStatus;
import controller.net.RobotWatcher;
import controller.ui.ui.customized.Button;

import common.TotalScaleLayout;
import data.Rules;
import data.hl.HL;
import data.spl.SPL;
import data.states.AdvancedData;
import data.values.Penalties;
import data.values.Side;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Created by rkessler on 2017-03-29.
 */
public class RobotList extends AbstractComponent implements Notifiable {

    private Side side;

    protected JPanel robots;
    protected JButton[] robot;
    protected JLabel[] robotLabel;
    protected ImageIcon[] lanIcon;
    protected JProgressBar[] robotTime;

    protected ImageIcon lanOnline;
    protected ImageIcon lanHighLatency;
    protected ImageIcon lanOffline;
    protected ImageIcon lanUnknown;

    protected static final String ICONS_PATH = "config/icons/";
    protected static final String ONLINE = "wlan_status_green.png";
    protected static final String OFFLINE = "wlan_status_red.png";
    protected static final String HIGH_LATENCY = "wlan_status_yellow.png";
    protected static final String UNKNOWN_ONLINE_STATUS = "wlan_status_grey.png";

    private int teamSize;
    private SelectedPenalty selectedPenalty;


    public RobotList(Side side, SelectedPenalty selectedPenalty){
        this.side = side;

        lanOnline = new ImageIcon(ICONS_PATH+ONLINE);
        lanHighLatency = new ImageIcon(ICONS_PATH+HIGH_LATENCY);
        lanOffline = new ImageIcon(ICONS_PATH+OFFLINE);
        lanUnknown = new ImageIcon(ICONS_PATH+UNKNOWN_ONLINE_STATUS);

        teamSize = Rules.league.teamSize;

        if (Rules.league.isCoachAvailable){
            teamSize += 1;
        }

        defineLayout();
    }


    public void defineLayout(){
        int teamSize = Rules.league.teamSize;

        if (Rules.league.isCoachAvailable){
            teamSize += 1;
        }

        robots = new JPanel();
        robots.setLayout(new GridLayout(teamSize, 1, 0, 10));
        robots.setOpaque(false);

        robot = new JButton[teamSize];
        robotLabel = new JLabel[teamSize];
        lanIcon = new ImageIcon[teamSize];
        robotTime = new JProgressBar[teamSize];

        for (int j=0; j < teamSize; j++) {
            robot[j] = new Button();
            robotLabel[j] = new JLabel();
            robotLabel[j].setHorizontalAlignment(JLabel.CENTER);
            lanIcon[j] = lanUnknown;
            robotLabel[j].setIcon(lanIcon[j]);
            robotTime[j] = new JProgressBar();
            robotTime[j].setMaximum(1000);
            robotTime[j].setVisible(false);
            robot[j].setLayout(new BoxLayout(robot[j], BoxLayout.Y_AXIS));
            robot[j].add(robotLabel[j]);
            robot[j].add(robotTime[j]);
            robots.add(robot[j]);
        }

        robots.setVisible(true);
        robots.setSize(500, 600);

        this.add(robots);
        this.setSize(500, 600);
        this.setVisible(true);

    }

    protected static final int UNPEN_HIGHLIGHT_SECONDS = 10;

    protected static final String COACH = "Coach";
    public static final Color COLOR_HIGHLIGHT = Color.YELLOW;
    protected static final String EJECTED = "Ejected";

    protected String formatTime(int seconds) {
        int displaySeconds = Math.abs(seconds) % 60;
        int displayMinutes = Math.abs(seconds) / 60;
        return (seconds < 0 ? "-" : "") + String.format("%02d:%02d", displayMinutes, displaySeconds);
    }

    protected static final String STANDARD_FONT = "Helvetica";

    @SuppressWarnings("Duplicates")
    @Override
    public void update(AdvancedData data) {
        Font titleFont = new Font(STANDARD_FONT, Font.PLAIN, (int)(20));


        for (int j=0; j<teamSize; j++) {
            robotLabel[j].setFont(titleFont);
        }

        RobotOnlineStatus[][] onlineStatus = RobotWatcher.updateRobotOnlineStatus();
            int i = side.value();

            for (int j=0; j< teamSize; j++) {
                if (ActionBoard.robot[i][j].isCoach(data)) {
                    if (data.team[i].coach.penalty == Penalties.SPL_COACH_MOTION) {
                        robot[j].setEnabled(false);
                        robotLabel[j].setText(EJECTED);
                    } else {
                        robotLabel[j].setText(data.team[i].teamColor + " " + COACH);
                    }
                } else {
                    if (data.team[i].player[j].penalty != Penalties.NONE) {
                        if (!data.ejected[i][j]) {
                            int seconds = data.getRemainingPenaltyTime(i, j);
                            boolean pickup = ((Rules.league instanceof SPL &&
                                    data.team[i].player[j].penalty == Penalties.SPL_REQUEST_FOR_PICKUP)
                                    || (Rules.league instanceof HL &&
                                    (data.team[i].player[j].penalty == Penalties.HL_PICKUP_OR_INCAPABLE
                                            || data.team[i].player[j].penalty == Penalties.HL_SERVICE))
                            );
                            boolean illegalMotion = Rules.league instanceof SPL
                                    && data.team[i].player[j].penalty == Penalties.SPL_ILLEGAL_MOTION_IN_SET;
                            if (seconds == 0) {
                                if (pickup) {
                                    robotLabel[j].setText(data.team[i].teamColor + " " + (j + 1) + " (" + Penalties.SPL_REQUEST_FOR_PICKUP.toString() + ")");
                                    highlight(robot[j], true);
                                } else if (illegalMotion) {
                                    robotLabel[j].setText(data.team[i].teamColor + " " + (j + 1) + " (" + Penalties.SPL_ILLEGAL_MOTION_IN_SET.toString() + ")");
                                    highlight(robot[j], true);
                                } else if (data.team[i].player[j].penalty == Penalties.SUBSTITUTE) {
                                    robotLabel[j].setText(data.team[i].teamColor + " " + (j + 1) + " (" + Penalties.SUBSTITUTE.toString() + ")");
                                    highlight(robot[j], false);
                                } else if (!(Rules.league instanceof SPL) ||
                                        !(data.team[i].player[j].penalty == Penalties.SPL_COACH_MOTION)) {
                                    robotLabel[j].setText(data.team[i].teamColor + " " + (j + 1) + ": " + formatTime(seconds));
                                    highlight(robot[j], seconds <= UNPEN_HIGHLIGHT_SECONDS && robot[j].getBackground() != COLOR_HIGHLIGHT);
                                }
                            } else {
                                robotLabel[j].setText(data.team[i].teamColor + " " + (j + 1) + ": " + formatTime(seconds) + (pickup ? " (P)" : ""));
                                highlight(robot[j], seconds <= UNPEN_HIGHLIGHT_SECONDS && robot[j].getBackground() != COLOR_HIGHLIGHT);
                            }
                            int penTime = (seconds + data.getSecondsSince(data.whenPenalized[i][j]));
                            if (seconds != 0) {
                                robotTime[j].setValue(1000 * seconds / penTime);
                            }
                            robotTime[j].setVisible(seconds != 0);
                        } else {
                            robotLabel[j].setText(EJECTED);
                            robotTime[j].setVisible(false);
                            highlight(robot[j], false);
                        }
                    } else {
                        robotLabel[j].setText(data.team[i].teamColor + " " + (j + 1));
                        robotTime[j].setVisible(false);
                        highlight(robot[j], false);
                    }
                }

                robot[j].setEnabled(ActionBoard.robot[i][j].isLegal(data));

                ImageIcon currentLanIcon;
                if (onlineStatus[i][j] == RobotOnlineStatus.ONLINE) {
                    currentLanIcon = lanOnline;
                } else if (onlineStatus[i][j] == RobotOnlineStatus.HIGH_LATENCY) {
                    currentLanIcon = lanHighLatency;
                } else if (onlineStatus[i][j] == RobotOnlineStatus.OFFLINE) {
                    currentLanIcon = lanOffline;
                } else {
                    currentLanIcon = lanUnknown;
                }
                robotLabel[j].setIcon(currentLanIcon);
            }
    }

    @Override
    public void notifyObservers() {
        System.out.println("Was notified! Need to highlight buttons!");
    }
}
