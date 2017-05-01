package controller.ui.ui.components;

import common.TotalScaleLayout;
import controller.action.ActionBoard;
import controller.action.ActionType;
import controller.action.GCAction;
import controller.net.RobotOnlineStatus;
import controller.net.RobotWatcher;
import controller.ui.ui.customized.Button;
import data.Rules;
import data.communication.GameControlData;
import data.hl.HL;
import data.spl.SPL;
import data.states.AdvancedData;
import data.values.GameStates;
import data.values.Penalties;
import data.values.SecondaryGameStates;
import data.values.Side;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by rkessler on 2017-03-29.
 */
public class DropInTeamComponent extends AbstractComponent implements Refreshable {

    private Side side;

    protected JLabel name;
    protected JButton goalDec;
    protected JButton goalInc;
    protected JLabel goals;

    protected JRadioButton kickOff;
    protected ButtonGroup kickOffGroup;

    protected JLabel pushes;

    protected JPanel robots;
    protected JButton[] robot;

    private DropInPointCounter dropInPointCounter;

    protected JButton[] robotPointsInc;
    protected JButton[] robotPointsDec;
    protected JLabel[] robotPointsLabel;

    protected JLabel[] robotLabel;
    protected ImageIcon[] lanIcon;
    protected JProgressBar[] robotTime;

    protected ImageIcon lanOnline;
    protected ImageIcon lanHighLatency;
    protected ImageIcon lanOffline;
    protected ImageIcon lanUnknown;

    protected static final String KICKOFF_PENALTY_SHOOTOUT = "P.-taker";

    protected static final String ICONS_PATH = "config/icons/";
    protected static final String ONLINE = "wlan_status_green.png";
    protected static final String OFFLINE = "wlan_status_red.png";
    protected static final String HIGH_LATENCY = "wlan_status_yellow.png";
    protected static final String UNKNOWN_ONLINE_STATUS = "wlan_status_grey.png";

    public static final String KICKOFF = "Kickoff";
    private int teamSize;

    public DropInTeamComponent(Side side, DropInPointCounter dropInPointCounter){
        this.side = side;
        this.dropInPointCounter = dropInPointCounter;

        goalInc = new Button("+");
        goalDec = new Button("-");
        kickOff = new JRadioButton(KICKOFF);
        kickOff.setOpaque(false);
        kickOff.setHorizontalAlignment(JLabel.CENTER);

        // TODO - needs to be a button group across both sides ( i guess)
        kickOffGroup = new ButtonGroup();
        kickOffGroup.add(kickOff);

        goals = new JLabel("0");
        goals.setHorizontalAlignment(JLabel.CENTER);

        goalDec.addActionListener(ActionBoard.goalDec[side.value()]);
        goalInc.addActionListener(ActionBoard.goalInc[side.value()]);
        goalInc.addActionListener(new GCAction(ActionType.UI) {

            @Override
            public void perform(AdvancedData data) {
                dropInPointCounter.scoreGoal(data, side);
                updateDropInPointsLabels();
            }

            @Override
            public boolean isLegal(AdvancedData data) {
                return true;
            }
        });

        kickOff.addActionListener(ActionBoard.kickOff[side.value()]);

        pushes = new JLabel("0");
        pushes.setHorizontalAlignment(JLabel.CENTER);


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


    private void updateDropInPointsLabels(){
        for (int j=0; j < teamSize; j++) {
            int points = dropInPointCounter.getPoints(side, j);
            robotPointsLabel[j].setText(points + " Points");
        }
    }


    public void defineLayout(){
        int teamSize = Rules.league.teamSize;

        if (Rules.league.isCoachAvailable){
            teamSize += 1;
        }

        robots = new JPanel();
        TotalScaleLayout tsc = new TotalScaleLayout(robots);
        robots.setLayout(tsc);

        // First row
        tsc.add(0, 0, 0.33, 0.1, goals);
        tsc.add(0.33, 0, 0.33, 0.1, pushes);
        tsc.add(0.66, 0, 0.33, 0.1, kickOff);

        //
        tsc.add(0, 0.1, 0.5, 0.1, goalDec);
        tsc.add(0.5, 0.1, 0.5, 0.1, goalInc);

        robot = new JButton[teamSize];
        robotLabel = new JLabel[teamSize];
        lanIcon = new ImageIcon[teamSize];
        robotTime = new JProgressBar[teamSize];
        robotPointsLabel = new JLabel[teamSize];
        robotPointsInc = new JButton[teamSize];
        robotPointsDec = new JButton[teamSize];
        dropInPointCounter.initializeSide(this.side, teamSize, this);

        for (int j=0; j < teamSize; j++) {
            robot[j] = new Button();
            robotLabel[j] = new JLabel();
            robotLabel[j].setHorizontalAlignment(JLabel.CENTER);
            lanIcon[j] = lanUnknown;
            robotLabel[j].setIcon(lanIcon[j]);
            robotTime[j] = new JProgressBar();
            robotTime[j].setMaximum(1000);
            robotTime[j].setVisible(false);

            TotalScaleLayout layout = new TotalScaleLayout(robot[j]);
            robot[j].setLayout(layout);

            robotPointsLabel[j] = new JLabel();
            robotPointsInc[j] = new JButton("+");
            robotPointsDec[j] = new JButton("-");

            final int h = j;

            robotPointsInc[j].addActionListener(actionEvent -> {
                dropInPointCounter.deltaPoints(side, h, +1);
                updateDropInPointsLabels();
            });

            robotPointsDec[j].addActionListener(actionEvent -> {
                dropInPointCounter.deltaPoints(side, h, -1);
                updateDropInPointsLabels();
            });

            layout.add(0, 0, 0.2, 0.35, robotPointsInc[j]);
            layout.add(0, 0.35, 0.2, 0.35, robotPointsDec[j]);
            layout.add(0.02, 0.7, 0.18, 0.3, robotPointsLabel[j]);

            layout.add(0.275, 0.1, 0.7, 0.4, robotLabel[j]);
            layout.add(0.275, 0.5, 0.7, 0.4, robotTime[j]);


            tsc.add(0, 0.2+0.1*j, 1, 0.1, robot[j]);
            robot[j].addActionListener(ActionBoard.robot[side.value()][j]);
        }
        updateDropInPointsLabels();

        robots.setVisible(true);

        this.setLayout(new TotalScaleLayout(this));
        ((TotalScaleLayout) this.getLayout()).add(0, 0, 1, 1, robots);

        this.setVisible(true);

    }

    protected static final String PUSHES = "Pushes";
    protected static final String SHOT = "Shot";
    protected static final String SHOTS = "Shots";

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

    protected void updatePushes(AdvancedData data)
    {
            if (data.secGameState != SecondaryGameStates.PENALTYSHOOT && data.previousSecGameState != SecondaryGameStates.PENALTYSHOOT) {
                if (Rules.league.pushesToEjection == null || Rules.league.pushesToEjection.length == 0) {
                    pushes.setText("");
                } else {
                    pushes.setText(PUSHES+": "+data.pushes[side.value()]);
                }
            } else {
                pushes.setText((side.value() == 0 && (data.gameState == GameStates.SET
                        || data.gameState == GameStates.PLAYING) ? SHOT : SHOTS)+": "+data.team[side.value()].penaltyShot);
            }

    }

    public void updateKickOff(AdvancedData data)
    {
        if (data.kickOffTeam == GameControlData.DROPBALL) {
            kickOff.setSelected(true);
        }
        else if (data.team[side.value()].teamNumber == data.kickOffTeam){
            kickOff.setSelected(true);
        }
        else {
            kickOff.setSelected(false);
        }

        kickOff.setEnabled(ActionBoard.kickOff[side.value()].isLegal(data));
        if (data.secGameState != SecondaryGameStates.PENALTYSHOOT
                && data.previousSecGameState != SecondaryGameStates.PENALTYSHOOT) {
            kickOff.setText(KICKOFF);
        } else {
            kickOff.setText(KICKOFF_PENALTY_SHOOTOUT);
        }

    }

    @SuppressWarnings("Duplicates")
    @Override
    public void update(AdvancedData data) {

        updateKickOff(data);
        updatePushes(data);

        goals.setText("Goals: " + data.team[side.value()].score);
        goalInc.setEnabled(ActionBoard.goalInc[side.value()].isLegal(data));
        goalDec.setVisible(ActionBoard.goalDec[side.value()].isLegal(data));


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
    public void refresh() {
        updateDropInPointsLabels();
    }
}
