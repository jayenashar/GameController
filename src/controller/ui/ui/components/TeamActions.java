package controller.ui.ui.components;

import common.TotalScaleLayout;
import controller.action.ActionBoard;
import controller.net.RobotOnlineStatus;
import controller.net.RobotWatcher;
import controller.ui.gameplay.GUI;
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

/**
 * Created by rkessler on 2017-03-29.
 */
public class TeamActions extends AbstractComponent {

    private static final int TIMEOUT_HIGHLIGHT_SECONDS = 10;

    protected static final String TIMEOUT = "Timeout";
    protected static final String OUT = "Out";
    protected static final String STUCK = "Global <br/> Game <br/> Stuck";

    protected Side side;

    protected JToggleButton timeOut;
    protected JButton stuck;
    protected JButton out;

    protected JPanel container;

    public TeamActions(Side side){
        this.side = side;

        defineLayout();
    }


    public void defineLayout(){
        container = new JPanel();
        container.setVisible(true);

        TotalScaleLayout layout = new TotalScaleLayout(container);
        container.setLayout(layout);

        timeOut = new JToggleButton(TIMEOUT);
        out = new JButton(OUT);
        stuck = new JButton(STUCK);

        layout.add(0, 0, 0.33, 1, timeOut);
        layout.add(0.33, 0, 0.33, 1, out);
        layout.add(0.66, 0, 0.34, 1, stuck);

        out.setVisible(false);
        stuck.setVisible(false);
        timeOut.setVisible(false);

        if (Rules.league instanceof SPL && !Rules.league.dropInPlayerMode) {
            timeOut.setVisible(true);
            stuck.setVisible(true);
        } else {
            if (Rules.league instanceof SPL) {
                stuck.setVisible(true);
            } else {
                timeOut.setVisible(true);
            }
        }
        out.setVisible(true);

        timeOut.addActionListener(ActionBoard.timeOut[side.value()]);
        out.addActionListener(ActionBoard.out[side.value()]);
        if (Rules.league instanceof SPL) {
            stuck.addActionListener(ActionBoard.stuck[side.value()]);
        }

        this.setLayout(new TotalScaleLayout(this));
        ((TotalScaleLayout) this.getLayout()).add(0, 0, 1, 1, container);

        this.setVisible(true);
    }

    @Override
    public void update(AdvancedData data) {
        updateTimeOut(data);
        updateOut(data);

        if (Rules.league instanceof SPL) {
            updateGlobalStuck(data);
        }
    }

    /**
     * Updates the time-out.
     *
     * @param data     The current data (model) the GUI should view.
     */
    protected void updateTimeOut(AdvancedData data)
    {
        if (!data.timeOutActive[side.value()]) {
            timeOut.setSelected(false);
            highlight(timeOut, false);
        } else {
            boolean shouldHighlight = (data.getRemainingSeconds(data.whenCurrentGameStateBegan, Rules.league.timeOutTime) < TIMEOUT_HIGHLIGHT_SECONDS)
                    && (timeOut.getBackground() != GUI.COLOR_HIGHLIGHT);
            timeOut.setSelected(!GUI.IS_OSX || !shouldHighlight);
            highlight(timeOut, shouldHighlight);
        }
        timeOut.setEnabled(ActionBoard.timeOut[side.value()].isLegal(data));

    }

    /**
     * Updates the global game stuck.
     *
     * @param data     The current data (model) the GUI should view.
     */
    protected void updateGlobalStuck(AdvancedData data)
    {
        if (data.gameState == GameStates.PLAYING
                && data.getRemainingSeconds(data.whenCurrentGameStateBegan, Rules.league.kickoffTime + Rules.league.minDurationBeforeStuck) > 0)
        {
            stuck.setEnabled(false);
            stuck.setText("<font color=#808080>"+STUCK);
        } else {
            stuck.setEnabled(ActionBoard.stuck[side.value()].isLegal(data));
            stuck.setText((ActionBoard.stuck[side.value()].isLegal(data) ? "<font color=#000000>" : "<font color=#808080>")+STUCK);
        }

    }



    /**
     * Updates the out.
     *
     * @param data     The current data (model) the GUI should view.
     */
    protected void updateOut(AdvancedData data)
    {
        out.setEnabled(ActionBoard.out[side.value()].isLegal(data));
    }
}
