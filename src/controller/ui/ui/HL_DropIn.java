package controller.ui.ui;

import controller.action.ui.penalty.Penalty;
import controller.ui.ui.components.ClockComponent;
import controller.ui.ui.components.GameStateComponent;
import controller.ui.ui.components.PenaltyComponent;
import controller.ui.ui.components.RobotList;
import data.states.AdvancedData;
import data.values.Side;

import javax.swing.*;


public class HL_DropIn extends AbstractUI {

    public HL_DropIn(boolean fullscreen, AdvancedData data) {
        super(fullscreen, data);

        setupUI();

        this.setSize(600, 400);
        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS)
        );

        setVisible(true);
        System.out.println("Came to set visible");
    }

    private void setupUI() {
        GameStateComponent gsc = new GameStateComponent();
        addUIPart(gsc);

        ClockComponent cc = new ClockComponent();
        addUIPart(cc);



        PenaltyComponent pc = new PenaltyComponent();
        uiElements.add(pc);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

        RobotList rl_left = new RobotList(Side.LEFT, pc);
        uiElements.add(rl_left);
        p.add(rl_left);

        p.add(pc);

        RobotList rl_right = new RobotList(Side.RIGHT, pc);
        uiElements.add(rl_right);
        p.add(rl_right);

        pc.addNotifiers(rl_left);
        pc.addNotifiers(rl_right);

        this.add(p);

    }

    @Override
    public void dispose() {
        dispose();
    }
}
