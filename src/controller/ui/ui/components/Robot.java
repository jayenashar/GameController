package controller.ui.ui.components;

import common.TotalScaleLayout;
import controller.action.ActionBoard;
import controller.action.ui.CardIncrease;
import controller.net.RobotOnlineStatus;
import controller.net.RobotWatcher;
import controller.ui.localization.Localization;
import controller.ui.ui.customized.Button;
import data.Helper;
import data.Rules;
import data.hl.HL;
import data.spl.SPL;
import data.states.AdvancedData;
import data.values.Penalties;
import data.values.Side;

import javax.swing.*;
import java.awt.*;

/**
 * Created by rkessler on 2017-06-11.
 */
public class Robot extends AbstractComponent {

    private ImageIcon lanIcon;
    private Side side;
    private int id;
    private JButton robot;
    private JLabel robotLabel;

    private JButton yellowCard;
    private JButton redCard;


    protected ImageIcon lanOnline;
    protected ImageIcon lanHighLatency;
    protected ImageIcon lanOffline;
    protected ImageIcon lanUnknown;


    protected static final String ICONS_PATH = "config/icons/";
    protected static final String ONLINE = "wlan_status_green.png";
    protected static final String OFFLINE = "wlan_status_red.png";
    protected static final String HIGH_LATENCY = "wlan_status_yellow.png";
    protected static final String UNKNOWN_ONLINE_STATUS = "wlan_status_grey.png";
    public static final Color COLOR_HIGHLIGHT = Color.YELLOW;

    protected static final int UNPEN_HIGHLIGHT_SECONDS = 10;

    private JProgressBar robotTime;


    public Robot(Side side, int id) {
        this.side = side;
        this.id = id;
        lanOnline = new ImageIcon(ICONS_PATH + ONLINE);
        lanHighLatency = new ImageIcon(ICONS_PATH + HIGH_LATENCY);
        lanOffline = new ImageIcon(ICONS_PATH + OFFLINE);
        lanUnknown = new ImageIcon(ICONS_PATH + UNKNOWN_ONLINE_STATUS);

        setup();
    }

    public void setup() {
        robot = new JButton();
        robot.addActionListener(ActionBoard.robot[side.value()][this.id]);

        robotLabel = new JLabel();
        robotLabel.setHorizontalAlignment(JLabel.CENTER);
        robotLabel.setIcon(lanIcon);

        robotTime = new JProgressBar();
        robotTime.setMaximum(1000);
        robotTime.setVisible(false);

        robot.setLayout(new BoxLayout(robot, BoxLayout.Y_AXIS));
        robot.add(robotLabel);
        robot.add(robotTime);

        yellowCard = new Button("Yellow");
        yellowCard.setBackground(Color.YELLOW);
        yellowCard.addActionListener(new CardIncrease(side, this.id, Color.YELLOW));
        robot.add(yellowCard);

        redCard = new Button("Red");
        redCard.setBackground(Color.RED);
        redCard.addActionListener(new CardIncrease(side, this.id, Color.RED));
        robot.add(redCard);

        robotLabel.setVisible(true);
        robot.setVisible(true);
        this.setLayout(new TotalScaleLayout(this));
        ((TotalScaleLayout) this.getLayout()).add(0, 0, 1, 1, robot);
        this.setVisible(true);
    }

    @Override
    public void update(AdvancedData data) {
        int sideValue = this.side.value();
        int j = this.id;


        yellowCard.setText("Y: " + data.team[side.value()].player[j].yellowCardCount);
        redCard.setText("R: " + data.team[side.value()].player[j].redCardCount);

        //** Online state **/
        RobotOnlineStatus[][] onlineStatus = RobotWatcher.updateRobotOnlineStatus();
        ImageIcon currentLanIcon;
        if (onlineStatus[sideValue][j] == RobotOnlineStatus.ONLINE) {
            currentLanIcon = lanOnline;
        } else if (onlineStatus[sideValue][j] == RobotOnlineStatus.HIGH_LATENCY) {
            currentLanIcon = lanHighLatency;
        } else if (onlineStatus[sideValue][j] == RobotOnlineStatus.OFFLINE) {
            currentLanIcon = lanOffline;
        } else {
            currentLanIcon = lanUnknown;
        }
        robotLabel.setIcon(currentLanIcon);


        if (ActionBoard.robot[sideValue][j].isCoach(data)) {
            if (data.team[sideValue].coach.penalty == Penalties.SPL_COACH_MOTION) {
                robot.setEnabled(false);
                robotLabel.setText(Localization.getDefault().EJECTED);
            } else {
                robotLabel.setText(data.team[sideValue].teamColor + " " + Localization.getDefault().COACH);
            }
        } else {
            if (data.team[sideValue].player[j].penalty != Penalties.NONE) {
                if (!data.ejected[sideValue][j]) {
                    int seconds = data.getRemainingPenaltyTime(sideValue, j);
                    boolean pickup = ((Rules.league instanceof SPL &&
                            data.team[sideValue].player[j].penalty == Penalties.SPL_REQUEST_FOR_PICKUP)
                            || (Rules.league instanceof HL &&
                            (data.team[sideValue].player[j].penalty == Penalties.HL_PICKUP_OR_INCAPABLE
                                    || data.team[sideValue].player[j].penalty == Penalties.HL_SERVICE))
                    );
                    boolean illegalMotion = Rules.league instanceof SPL
                            && data.team[sideValue].player[j].penalty == Penalties.SPL_ILLEGAL_MOTION_IN_SET;
                    if (seconds == 0) {
                        if (pickup) {
                            robotLabel.setText(data.team[sideValue].teamColor + " " + (j + 1) + " (" + Penalties.SPL_REQUEST_FOR_PICKUP.toString() + ")");
                            highlight(robot, true);
                        } else if (illegalMotion) {
                            robotLabel.setText(data.team[sideValue].teamColor + " " + (j + 1) + " (" + Penalties.SPL_ILLEGAL_MOTION_IN_SET.toString() + ")");
                            highlight(robot, true);
                        } else if (data.team[sideValue].player[j].penalty == Penalties.SUBSTITUTE) {
                            robotLabel.setText(data.team[sideValue].teamColor + " " + (j + 1) + " (" + Penalties.SUBSTITUTE.toString() + ")");
                            highlight(robot, false);
                        } else if (!(Rules.league instanceof SPL) ||
                                !(data.team[sideValue].player[j].penalty == Penalties.SPL_COACH_MOTION)) {
                            robotLabel.setText(data.team[sideValue].teamColor + " " + (j + 1) + ": " + Helper.formatTime(seconds));
                            highlight(robot, seconds <= UNPEN_HIGHLIGHT_SECONDS && robot.getBackground() != COLOR_HIGHLIGHT);
                        }
                    } else {
                        robotLabel.setText(data.team[sideValue].teamColor + " " + (j + 1) + ": " + Helper.formatTime(seconds) + (pickup ? " (P)" : ""));
                        highlight(robot, seconds <= UNPEN_HIGHLIGHT_SECONDS && robot.getBackground() != COLOR_HIGHLIGHT);
                    }
                    int penTime = (seconds + data.getSecondsSince(data.whenPenalized[sideValue][j]));
                    if (seconds != 0) {
                        robotTime.setValue(1000 * seconds / penTime);
                    }
                    robotTime.setVisible(seconds != 0);
                } else {
                    robotLabel.setText(Localization.getDefault().EJECTED);
                    robotTime.setVisible(false);
                    highlight(robot, false);
                }
            } else {
                robotLabel.setText(data.team[sideValue].teamColor + " " + (j + 1));
                robotTime.setVisible(false);
                highlight(robot, false);
            }
        }
    }


}
