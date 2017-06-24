package controller.ui.ui.components;

import common.TotalScaleLayout;
import controller.action.ActionBoard;
import controller.ui.helper.Localization;
import controller.ui.ui.customized.Button;
import data.states.AdvancedData;
import data.values.Side;

import javax.swing.*;

/**
 * Created by rkessler on 2017-04-29.
 */
public class HLTeamActions extends TeamActions {

    private JButton indirectFreeKick;
    private JButton directFreeKick;
    private JButton penaltyKick;

    public HLTeamActions(Side side) {
        super(side);

        defineLayout();
    }

    public void defineLayout() {
        container = new JPanel();
        container.setVisible(true);

        TotalScaleLayout layout = new TotalScaleLayout(container);
        container.setLayout(layout);

        timeOut = new JToggleButton(TIMEOUT);
        out = new JButton(OUT);

        directFreeKick = new Button(Localization.getDefault().DIRECT_FREE_KICK_PREPARE);
        indirectFreeKick = new Button(Localization.getDefault().INDIRECT_FREE_KICK_PREPARE);

        penaltyKick = new Button(Localization.getDefault().PENALTY_KICK_PREPARE);

        directFreeKick.addActionListener(ActionBoard.directFreeKick[side.value()]);
        indirectFreeKick.addActionListener(ActionBoard.indirectFreeKick[side.value()]);
        penaltyKick.addActionListener(ActionBoard.penaltyKick[side.value()]);

        layout.add(0, 0, 0.25, 1, timeOut);
        layout.add(0.25, 0, 0.25, 1, out);
        layout.add(0.5, 0, 0.15, 1, directFreeKick);
        layout.add(0.65, 0, 0.10, 1, indirectFreeKick);
        layout.add(0.75, 0, 0.25, 1, penaltyKick);

        timeOut.setVisible(true);
        out.setVisible(true);

        timeOut.addActionListener(ActionBoard.timeOut[side.value()]);
        out.addActionListener(ActionBoard.out[side.value()]);

        this.setLayout(new TotalScaleLayout(this));
        ((TotalScaleLayout) this.getLayout()).add(0, 0, 1, 1, container);

        this.setVisible(true);
    }

    @Override
    public void update(AdvancedData data) {
        super.update(data);
        updateDirectFreeKick(data);
        updateIndirectFreeKick(data);
        updatePenaltyKick(data);
    }


    private void updateDirectFreeKick(AdvancedData data) {
        // Check whether the button can be pressed
        directFreeKick.setEnabled(ActionBoard.directFreeKick[side.value()].isLegal(data));

        // Check if the label of the button needs to be switched
        if (data.directFreeKickActive[side.value()]) {
            directFreeKick.setText(Localization.getDefault().DIRECT_FREE_KICK_EXECUTE);
        } else {
            directFreeKick.setText(Localization.getDefault().DIRECT_FREE_KICK_PREPARE);
        }
    }

    private void updateIndirectFreeKick(AdvancedData data) {
        // Check whether the button can be pressed
        indirectFreeKick.setEnabled(ActionBoard.indirectFreeKick[side.value()].isLegal(data));

        // Check if the label of the button needs to be switched
        if (data.indirectFreeKickActive[side.value()]) {
            indirectFreeKick.setText(Localization.getDefault().INDIRECT_FREE_KICK_EXECUTE);
        } else {
            indirectFreeKick.setText(Localization.getDefault().INDIRECT_FREE_KICK_PREPARE);
        }
    }

    private void updatePenaltyKick(AdvancedData data) {
        // Check whether the button can be pressed
        penaltyKick.setEnabled(ActionBoard.penaltyKick[side.value()].isLegal(data));

        // Check if the label of the button needs to be switched
        if (data.penaltyKickActive[side.value()]) {
            penaltyKick.setText(Localization.getDefault().PENALTY_KICK_EXECUTE);
        } else {
            penaltyKick.setText(Localization.getDefault().PENALTY_KICK_PREPARE);
        }
    }
}
