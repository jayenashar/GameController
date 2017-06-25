package controller.ui.ui.components;

import common.TotalScaleLayout;
import controller.action.ActionBoard;
import controller.action.ui.DirectFreeKick;
import controller.action.ui.IndirectFreeKick;
import controller.action.ui.PenaltyKick;
import controller.ui.localization.Localization;
import controller.ui.localization.LocalizationManager;
import controller.ui.ui.customized.Button;
import data.states.AdvancedData;
import data.values.SecondaryGameStates;
import data.values.Side;

import javax.swing.*;
import java.awt.*;

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

        directFreeKick = new Button(LocalizationManager.getLocalization().DIRECT_FREE_KICK_PREPARE);
        indirectFreeKick = new Button(LocalizationManager.getLocalization().INDIRECT_FREE_KICK_PREPARE);

        penaltyKick = new Button(LocalizationManager.getLocalization().PENALTY_KICK_PREPARE);

        directFreeKick.addActionListener(new DirectFreeKick(side.value()));
        indirectFreeKick.addActionListener(new IndirectFreeKick(side.value()));
        penaltyKick.addActionListener(new PenaltyKick(side.value()));

        layout.add(0, 0, 0.33, 0.5, timeOut);
        layout.add(0.33, 0, 0.33, 0.5, out);
        layout.add(0.66, 0, 0.34, 0.5, penaltyKick);

        layout.add(0, 0.5, 0.5, 0.5, directFreeKick);
        layout.add(0.5, 0.5, 0.5, 0.5, indirectFreeKick);

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

        // Check secondary game state as that informs us if a direct freekick is active

        if (data.secGameState == SecondaryGameStates.DIRECT_FREEKICK){
            byte[] bytes = data.secGameStateInfo.toByteArray();
            byte team = bytes[0];
            byte subMode = bytes[1];

            // Only change text and color if it is our team
            boolean isUs = team == data.team[side.value()].teamNumber;

            if (isUs){
                if (subMode == 0){
                    directFreeKick.setText(LocalizationManager.getLocalization().DIRECT_FREE_KICK_FREEZE);
                    directFreeKick.setBackground(new Color(193, 255, 208));
                } else {
                    directFreeKick.setText(LocalizationManager.getLocalization().DIRECT_FREE_KICK_EXECUTE);
                    directFreeKick.setBackground(new Color(193, 255, 208));
                }
            }
        } else {
            directFreeKick.setText(LocalizationManager.getLocalization().DIRECT_FREE_KICK_PREPARE);
            directFreeKick.setBackground(null);
        }
    }

    private void updateIndirectFreeKick(AdvancedData data) {
        // Check whether the button can be pressed
        indirectFreeKick.setEnabled(new IndirectFreeKick(side.value()).isLegal(data));

        // Check secondary game state as that informs us if a direct freekick is active

        if (data.secGameState == SecondaryGameStates.INDIRECT_FREEKICK){
            byte[] bytes = data.secGameStateInfo.toByteArray();
            byte team = bytes[0];
            byte subMode = bytes[1];

            // Only change text and color if it is our team
            boolean isUs = team == data.team[side.value()].teamNumber;

            if (isUs){
                if (subMode == 0){
                    indirectFreeKick.setText(LocalizationManager.getLocalization().INDIRECT_FREE_KICK_FREEZE);
                    indirectFreeKick.setBackground(new Color(193, 255, 208));
                } else {
                    indirectFreeKick.setText(LocalizationManager.getLocalization().INDIRECT_FREE_KICK_EXECUTE);
                    indirectFreeKick.setBackground(new Color(193, 255, 208));
                }
            }
        } else {
            indirectFreeKick.setText(LocalizationManager.getLocalization().INDIRECT_FREE_KICK_PREPARE);
            indirectFreeKick.setBackground(null);
        }
    }

    private void updatePenaltyKick(AdvancedData data) {
        // Check whether the button can be pressed
        penaltyKick.setEnabled(ActionBoard.penaltyKick[side.value()].isLegal(data));

        // Check if the label of the button needs to be switched
        if (data.penaltyKickActive[side.value()]) {
            penaltyKick.setText(LocalizationManager.getLocalization().PENALTY_KICK_EXECUTE);
        } else {
            penaltyKick.setText(LocalizationManager.getLocalization().PENALTY_KICK_PREPARE);
        }
    }
}
