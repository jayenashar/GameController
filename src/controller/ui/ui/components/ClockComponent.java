package controller.ui.ui.components;

import common.TotalScaleLayout;
import controller.action.ActionBoard;
import controller.ui.gameplay.GUI;
import controller.ui.ui.customized.ImageButton;
import controller.ui.ui.customized.ImagePanel;
import data.Rules;
import data.states.AdvancedData;
import data.values.GameStates;

import javax.swing.*;
import java.awt.*;


/**
 * Created by rkessler on 2017-03-28.
 */
public class ClockComponent extends AbstractComponent {

    private JLabel clock;
    private JLabel clockSub;
    private ImageButton incGameClock;
    private ImageButton clockPause;
    private ImageButton clockReset;
    private ImagePanel clockContainer;


    private static final String CLOCK_RESET = "reset.png";
    private static final String CLOCK_PAUSE = "pause.png";
    private static final String CLOCK_PLAY = "play.png";
    private static final String CLOCK_PLUS = "plus.png";

    private static final String BACKGROUND_CLOCK_SMALL = "time_ground_small.png";
    private static final String BACKGROUND_CLOCK = "time_ground.png";

    private static final String ICONS_PATH = "config/icons/";

    protected static final int KICKOFF_BLOCKED_HIGHLIGHT_SECONDS = 3;

    public static final Color COLOR_HIGHLIGHT = Color.YELLOW;

    private ImageIcon clockImgReset;
    private ImageIcon clockImgPlay;
    private ImageIcon clockImgPause;
    private ImageIcon clockImgPlus;

    public ClockComponent() {
        clockImgReset = new ImageIcon(ICONS_PATH + CLOCK_RESET);
        clockImgPlay = new ImageIcon(ICONS_PATH + CLOCK_PLAY);
        clockImgPlus = new ImageIcon(ICONS_PATH + CLOCK_PLUS);
        clockImgPause = new ImageIcon(ICONS_PATH + CLOCK_PAUSE);



        defineLayout();
    }

    public void defineLayout() {
        //--mid--
        //  time
        clockReset = new ImageButton(clockImgReset.getImage());
        clockReset.setOpaque(false);
        clockReset.setBorder(null);
        if (Rules.league.lostTime) {
            clockContainer = new ImagePanel(new ImageIcon(ICONS_PATH + BACKGROUND_CLOCK_SMALL).getImage());
        } else {
            clockContainer = new ImagePanel(new ImageIcon(ICONS_PATH + BACKGROUND_CLOCK).getImage());
        }
        clockContainer.setOpaque(false);
        clock = new JLabel("10:00");
        clock.setForeground(Color.WHITE);
        clock.setHorizontalAlignment(JLabel.CENTER);
        clockPause = new ImageButton(clockImgReset.getImage());
        clockPause.setOpaque(false);
        clockPause.setBorder(null);
        clockSub = new JLabel("0:00");
        clockSub.setHorizontalAlignment(JLabel.CENTER);
        incGameClock = new ImageButton(clockImgPlus.getImage());
        incGameClock.setOpaque(false);
        incGameClock.setBorder(null);

        clock.setVisible(true);
        clockPause.setVisible(true);
        clockSub.setVisible(true);

        clockContainer.add(clock);
        clockContainer.add(clockSub);
        clockContainer.add(clockPause);
        clockContainer.add(clockSub);
        clockContainer.add(clockReset);
        clockContainer.add(incGameClock);
        clockContainer.setLayout(new BoxLayout(clockContainer, BoxLayout.Y_AXIS));

        clockReset.addActionListener(ActionBoard.clockReset);
        clockPause.addActionListener(ActionBoard.clockPause);
        if (Rules.league.lostTime) {
            incGameClock.addActionListener(ActionBoard.incGameClock);
        }

        this.add(clockContainer);
        this.setVisible(true);
    }


    protected static final int TIME_FONT_SIZE = 50;
    protected static final int TIME_SUB_FONT_SIZE = 40;

    private double lastSize = 0;
    protected static final String STANDARD_FONT = "Helvetica";

    @SuppressWarnings("Duplicates")
    @Override
    public void update(AdvancedData data) {
        Font timeFont = new Font(STANDARD_FONT, Font.PLAIN, (int) (TIME_FONT_SIZE * (1)));
        Font timeSubFont = new Font(STANDARD_FONT, Font.PLAIN, (int) (TIME_SUB_FONT_SIZE * (1)));

        clock.setFont(timeFont);
        clockSub.setFont(timeSubFont);

        clock.setText(formatTime(data.getRemainingGameTime(true)));
        Integer secondaryTime = data.getSecondaryTime(KICKOFF_BLOCKED_HIGHLIGHT_SECONDS - 1);
        if (secondaryTime != null) {
            if (data.gameState == GameStates.PLAYING) {
                clockSub.setText(formatTime(Math.max(0, secondaryTime)));
                clockSub.setForeground(secondaryTime <= 0
                        && clockSub.getForeground() != COLOR_HIGHLIGHT ? COLOR_HIGHLIGHT : Color.BLACK);
            } else {
                clockSub.setText(formatTime(secondaryTime));
                clockSub.setForeground(Color.BLACK);
            }
        } else {
            clockSub.setText("");
            clockSub.setForeground(Color.BLACK);
        }

        ImageIcon tmp;
        if (ActionBoard.clock.isClockRunning(data)) {
            tmp = clockImgPause;
        } else {
            tmp = clockImgPlay;
        }
        clockPause.setImage(tmp.getImage());
        clockReset.setVisible(ActionBoard.clockReset.isLegal(data));
        clockPause.setVisible(ActionBoard.clockPause.isLegal(data));
        if (Rules.league.lostTime) {
            incGameClock.setEnabled(ActionBoard.incGameClock.isLegal(data));
        }
    }

    private String formatTime(int seconds) {
        int displaySeconds = Math.abs(seconds) % 60;
        int displayMinutes = Math.abs(seconds) / 60;
        return (seconds < 0 ? "-" : "") + String.format("%02d:%02d", displayMinutes, displaySeconds);
    }
}
