package controller.ui.ui.components;

import common.TotalScaleLayout;
import controller.ui.helper.FontHelper;
import data.communication.TeamInfo;
import data.states.AdvancedData;
import data.states.GamePreparationData;
import data.values.Side;

import javax.swing.*;


/**
 * Created by rkessler on 2017-07-21.
 */
public class GameMetaInfo extends AbstractComponent {

    private JPanel container;

    private JLabel left_team;
    private JLabel right_team;

    private String[] team_name;

    private JLabel score_label;


    public GameMetaInfo(AdvancedData initialData, GamePreparationData gamePrepData) {
        TeamInfo ti = initialData.getTeam(Side.LEFT);
        left_team = new JLabel(ti.getName());
        left_team.setFont(FontHelper.boldHeadlineFont());
        left_team.setHorizontalAlignment(SwingConstants.CENTER);
        left_team.setVerticalAlignment(SwingConstants.CENTER);
        left_team.setBackground(ti.teamColor.colorValue());
        left_team.setOpaque(true);

        TeamInfo ti2 = initialData.getTeam(Side.RIGHT);
        right_team = new JLabel(ti2.getName());
        right_team.setFont(FontHelper.boldHeadlineFont());
        right_team.setHorizontalAlignment(SwingConstants.CENTER);
        right_team.setVerticalAlignment(SwingConstants.CENTER);
        right_team.setBackground(ti2.teamColor.colorValue());
        right_team.setOpaque(true);

        score_label = new JLabel("0 - 0");
        score_label.setFont(FontHelper.boldHeadlineFont());
        score_label.setHorizontalAlignment(SwingConstants.CENTER);
        score_label.setVerticalAlignment(SwingConstants.CENTER);

        defineLayout();
    }


    protected void defineLayout() {
        container = new JPanel();
        TotalScaleLayout layout = new TotalScaleLayout(container);

        container.setLayout(layout);
        layout.add(0, 0, 0.28, 1, left_team);
        layout.add(0.28, 0, 0.44, 1, score_label);
        layout.add(0.72, 0, 0.28, 1, right_team);

        //container.setSize(100, 100);
        container.setVisible(true);


        this.setLayout(new TotalScaleLayout(this));
        ((TotalScaleLayout) this.getLayout()).add(0, 0, 1, 1, container);
        this.setVisible(true);
    }

    public void update(AdvancedData data) {
        String left = String.valueOf(data.team[Side.LEFT.value()].score);
        String right = String.valueOf(data.team[Side.RIGHT.value()].score);
        score_label.setText(left + " - " + right);


        left_team.setText(data.team[Side.LEFT.value()].getName());
        left_team.setBackground(data.team[Side.LEFT.value()].teamColor.colorValue());

        right_team.setText(data.team[Side.RIGHT.value()].getName());
        right_team.setBackground(data.team[Side.RIGHT.value()].teamColor.colorValue());
    }
}
