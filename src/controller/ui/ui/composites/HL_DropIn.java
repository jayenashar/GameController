package controller.ui.ui.composites;

import common.TotalScaleLayout;
import controller.ui.ui.AbstractUI;
import controller.ui.ui.components.*;
import data.states.AdvancedData;
import data.values.Side;

import javax.swing.*;
import java.awt.*;


public class HL_DropIn extends AbstractUI {

    DropInPointCounter dropInPointCounter;
    ButtonGroup kickOffGroup;


    public HL_DropIn(boolean fullscreen, AdvancedData data) {
        super(fullscreen, data);
        dropInPointCounter = new DropInPointCounter();
        kickOffGroup = new ButtonGroup();

        setupUI();

        this.setSize(600, 400);

        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS)
        );

        setVisible(true);
    }

    private void setupUI() {

        // Create a root container that is added to the actual panel
        JPanel rootContainer = new JPanel();

        // Put a total scale layout on the container so we can add elements
        TotalScaleLayout layout = new TotalScaleLayout(rootContainer);
        rootContainer.setLayout(layout);

        // First we add the game states

        // Then we add the left panel (left team)
        JPanel left_team_panel = new JPanel();
        TotalScaleLayout left_panel_layout = new TotalScaleLayout(left_team_panel);
        left_team_panel.setLayout(left_panel_layout);
        setupLeftPanel(left_panel_layout);

        // We add the center panel including Clock, Penalties
        JPanel center_panel = new JPanel();
        TotalScaleLayout center_panel_layout = new TotalScaleLayout(center_panel);
        center_panel.setLayout(center_panel_layout);
        setupCenterPanel(center_panel_layout);

        // We add the right panel (right team)
        JPanel right_team_panel = new JPanel();
        TotalScaleLayout right_panel_layout = new TotalScaleLayout(right_team_panel);
        right_team_panel.setLayout(right_panel_layout);
        setupRightPanel(right_panel_layout);

        // Layouting those three
        layout.add(.01, .01, .28, .85, left_team_panel);
        layout.add(.3, .01, .4, .85, center_panel);
        layout.add(.71, .01, .28, .85, right_team_panel);

        // Adding the History component
        GameStateHistoryLogger gshl = new GameStateHistoryLogger();
        gshl.setBackground(new Color(56, 125, 255));
        uiElements.add(gshl);
        layout.add(.01, .88, .98, .10, gshl);

        // Add the root container
        this.add(rootContainer);
    }

    private void setupCenterPanel(TotalScaleLayout center_panel_layout) {

        SequenceGameStates sgs = new SequenceGameStates();
        sgs.setBackground(new Color(236, 255, 207));
        uiElements.add(sgs);
        center_panel_layout.add(0.01, 0.01, 0.98, 0.1, sgs);

        // Now we actually fill the center elements
        GameStateComponent gsc = new GameStateComponent();
        //gsc.setBackground(Color.yellow);
        uiElements.add(gsc);
        center_panel_layout.add(0.01, 0.11, 0.98, 0.1, gsc);
        gsc.setBackground(Color.YELLOW);

        ClockComponent cc = new ClockComponent();
        cc.setBackground(Color.red);
        uiElements.add(cc);
        center_panel_layout.add(0.01, 0.21, 0.98, 0.25, cc);

        PenaltyComponent pc = new PenaltyComponent();
        pc.setBackground(new Color(255, 51, 147));
        uiElements.add(pc);
        center_panel_layout.add(0.01, 0.46, 0.98, 0.45, pc);

    }

    private void setupLeftPanel(TotalScaleLayout left_panel_layout) {
        DropInTeamComponent rl_left = new DropInTeamComponent(Side.LEFT, dropInPointCounter, kickOffGroup);
        rl_left.setBackground(Color.green);
        TeamActions ta_left = new HLTeamActions (Side.LEFT);
        ta_left.setBackground(new Color(255,100, 2));
        uiElements.add(rl_left);
        uiElements.add(ta_left);

        left_panel_layout.add(0.0, 0.0, 1.0, 0.9, rl_left);
        left_panel_layout.add(0.0, 0.9, 1.0, 0.1, ta_left);
    }

    @Override
    public void dispose() {
        dispose();
    }

    private void setupRightPanel(TotalScaleLayout right_team_panel) {
        DropInTeamComponent rl_right = new DropInTeamComponent(Side.RIGHT, dropInPointCounter, kickOffGroup);
        rl_right.setBackground(new Color(56, 243, 255));

        TeamActions ta_right = new HLTeamActions (Side.RIGHT);
        ta_right.setBackground(new Color(193, 94, 255));

        uiElements.add(rl_right);
        uiElements.add(ta_right);

        right_team_panel.add(0.0, 0.0, 1.0, 0.9, rl_right);
        right_team_panel.add(0.0, 0.9, 1.0, 0.1, ta_right);
    }
}
