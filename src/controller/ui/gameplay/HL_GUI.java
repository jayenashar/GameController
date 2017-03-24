package controller.ui.gameplay;

import common.TotalScaleLayout;
import controller.action.ActionBoard;
import controller.ui.localization.Localization;
import data.Rules;
import data.communication.GameControlData;
import data.hl.HL;
import data.spl.SPL;
import data.states.AdvancedData;

import javax.swing.*;
import java.awt.*;


/**
 * @author Robert Kessler
 *         <p>
 *         This is the main GUI for the HL League - overwriting soem functionality and layouting choices.
 */
public class HL_GUI extends GUI {

    protected JButton[] freeKick;
    protected JButton[] penaltyKick;

    public HL_GUI(boolean fullscreen, GameControlData data) {
        super(fullscreen, data);
    }

    /** Sets up all the UI elements that are necessary such
     * that they can be arranged in the layout and actions can be bound to them
     */
    public void setupGuiElement(){
        freeKick = new Button[2];
        for (int i=0; i<2; i++) {
            freeKick[i] = new Button(Localization.getDefault().FREE_KICK_PREPARE);
        }

        penaltyKick = new Button[2];
        for (int i=0; i<2; i++) {
            penaltyKick[i] = new Button(Localization.getDefault().PENALTY_KICK_PREPARE);
        }
    }

    public void setupActionHandlers(){
        super.setupActionHandlers();

        freeKick[0].addActionListener(ActionBoard.freeKick[0]);
        freeKick[1].addActionListener(ActionBoard.freeKick[1]);

        penaltyKick[0].addActionListener(ActionBoard.penaltyKick[0]);
        penaltyKick[1].addActionListener(ActionBoard.penaltyKick[1]);

    }

    public void setupLayout() {
        //--layout--
        TotalScaleLayout layout = new TotalScaleLayout(this);
        setLayout(layout);

        JPanel[] teamActionPanel = new JPanel[2];
        teamActionPanel[0] = new JPanel();
        teamActionPanel[0].setLayout(new FlowLayout(FlowLayout.LEFT));
        teamActionPanel[1] = new JPanel();
        teamActionPanel[1].setLayout(new FlowLayout(FlowLayout.LEFT));

        layout.add(0, 0, .3, .04, name[0]);
        layout.add(.7, 0, .3, .04, name[1]);
        layout.add(.01, .05, .08, .07, goalInc[0]);
        layout.add(.91, .05, .08, .07, goalInc[1]);
        layout.add(.01, .13, .08, .06, goalDec[0]);
        layout.add(.91, .13, .08, .06, goalDec[1]);
        layout.add(.17, .05, .12, .04, kickOff[0]);
        layout.add(.71, .05, .12, .04, kickOff[1]);
        layout.add(.21, .09, .08, .07, goals[0]);
        layout.add(.71, .09, .08, .07, goals[1]);
        layout.add(.21, .16, .08, .04, pushes[0]);
        layout.add(.71, .16, .08, .04, pushes[1]);
        layout.add(.01, .21, .28, .55, robots[0]);
        layout.add(.71, .21, .28, .55, robots[1]);

        // Adding the panel that holds the buttons for team wide actions
        layout.add(.01, .77, .28, .09, teamActionPanel[0]);
        layout.add(.71, .77, .28, .09, teamActionPanel[1]);

        for (int i=0; i < 2; i++) {
            teamActionPanel[i].add(freeKick[i]);
            teamActionPanel[i].add(timeOut[i]);
            teamActionPanel[i].add(out[i]);
            teamActionPanel[i].add(penaltyKick[i]);
        }

        layout.add(.31, .0, .08, .11, clockReset);
        layout.add(.4, .012, .195, .10, clock);
        layout.add(.61, .0, .08, .11, clockPause);
        layout.add(.4, .11, .2, .07, clockSub);
        if (Rules.league.lostTime)

        {
            layout.add(.590, .0, .03, .11, incGameClock);
            layout.add(.4, .0, .195, .11, clockContainer);
        } else

        {
            layout.add(.4, .0, .2, .11, clockContainer);
        }
        if (!Rules.league.overtime)

        {
            if (Rules.league.isRefereeTimeoutAvailable && !Rules.league.dropInPlayerMode) {
                layout.add(.31, .19, .09, .06, firstHalf);
                layout.add(.407, .19, .09, .06, secondHalf);
                layout.add(.503, .19, .09, .06, penaltyShoot);
                layout.add(.60, .19, .09, .06, refereeTimeout);
            } else { // no referee timeout in dropInPlayerMode is not supported!
                layout.add(.31, .19, .12, .06, firstHalf);
                layout.add(.44, .19, .12, .06, secondHalf);
                layout.add(.57, .19, .12, .06, Rules.league.dropInPlayerMode ? refereeTimeout : penaltyShoot);
            }
        } else

        {
            if (Rules.league.isRefereeTimeoutAvailable) {
                layout.add(.31, .19, .06, .06, firstHalf);
                layout.add(.375, .19, .06, .06, secondHalf);
                layout.add(.439, .19, .06, .06, firstHalfOvertime);
                layout.add(.501, .19, .06, .06, secondHalfOvertime);
                layout.add(.565, .19, .06, .06, penaltyShoot);
                layout.add(.63, .19, .06, .06, refereeTimeout);
            } else {
                layout.add(.31, .19, .07, .06, firstHalf);
                layout.add(.3875, .19, .07, .06, secondHalf);
                layout.add(.465, .19, .07, .06, firstHalfOvertime);
                layout.add(.5425, .19, .07, .06, secondHalfOvertime);
                layout.add(.62, .19, .07, .06, penaltyShoot);
            }
        }
        layout.add(.31, .26, .07, .08, initial);
        layout.add(.3875, .26, .07, .08, ready);
        layout.add(.465, .26, .07, .08, set);
        layout.add(.5425, .26, .07, .08, play);
        layout.add(.62, .26, .07, .08, finish);

        layout.add(.31, .38, .185, .08, pen[0]);
        layout.add(.505, .38, .185, .08, pen[1]);
        layout.add(.31, .48, .185, .08, pen[2]);
        layout.add(.505, .48, .185, .08, pen[3]);
        layout.add(.31, .58, .185, .08, pen[4]);
        layout.add(.505, .58, .185, .08, pen[5]);
        layout.add(.31, .68, .185, .08, pen[6]);
        layout.add(.31, .78, .38, .08, dropBall);

        layout.add(.08, .88, .84, .11, log);
        layout.add(.925, .88, .07, .11, cancelUndo);
        layout.add(0, 0, .3, .87, side[0]);
        layout.add(.3, 0, .4, .87, mid);
        layout.add(.7, 0, .3, .87, side[1]);
        layout.add(0, .87, 1, .132, bottom);
    }

    @Override
    public void update(AdvancedData data)
    {
        updateClock(data);
        updateHalf(data);
        updateColor(data);
        updateState(data);
        updateGoal(data);
        updateKickoff(data);
        updatePushes(data);
        updateTimeOut(data);
        updateRefereeTimeout(data);
        updateOut(data);

        updateFreeKick(data);
        updatePenaltyKick(data);

        updatePenaltiesHL(data);
        updateDropBall(data);

        updateRobots(data);
        updateUndo(data);
        repaint();
    }

    private void updateFreeKick(AdvancedData data) {
        for(int i = 0; i < 2; i++){
            // Check whether the button can be pressed
            freeKick[i].setEnabled(ActionBoard.freeKick[i].isLegal(data));

            // Check if the label of the button needs to be switched
            if (data.freeKickActive[i]){
                freeKick[i].setText(Localization.getDefault().FREE_KICK_EXECUTE);
            } else {
                freeKick[i].setText(Localization.getDefault().FREE_KICK_PREPARE);
            }
        }
    }

    private void updatePenaltyKick(AdvancedData data) {
        for(int i = 0; i < 2; i++){
            // Check whether the button can be pressed
            penaltyKick[i].setEnabled(ActionBoard.penaltyKick[i].isLegal(data));

            // Check if the label of the button needs to be switched
            if (data.penaltyKickActive[i]){
                penaltyKick[i].setText(Localization.getDefault().PENALTY_KICK_EXECUTE);
            } else {
                penaltyKick[i].setText(Localization.getDefault().PENALTY_KICK_PREPARE);
            }
        }
    }
}