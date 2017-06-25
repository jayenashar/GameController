package controller.ui.ui.components;

import common.TotalScaleLayout;
import controller.action.ActionBoard;
import controller.ui.gameplay.GUI;
import controller.ui.ui.customized.ToggleButton;
import data.states.AdvancedData;
import data.values.GameStates;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rkessler on 2017-03-27.
 */
public class GameStateComponent extends AbstractComponent {

    protected JToggleButton initial;
    protected JToggleButton ready;
    protected JToggleButton set;
    protected JToggleButton play;
    protected JToggleButton finish;

    protected JPanel gameStateButtonContainer;

    private Map<GameStates, JToggleButton> buttonMapping = new HashMap<>();

    public GameStateComponent(){
        initial = new ToggleButton(GameStates.INITIAL.toString());
        ready = new ToggleButton(GameStates.READY.toString());
        set = new ToggleButton(GameStates.SET.toString());
        play = new ToggleButton(GameStates.PLAYING.toString());
        finish = new ToggleButton(GameStates.FINISHED.toString());

        initial.setSelected(true);

        ButtonGroup stateGroup = new ButtonGroup();
        stateGroup.add(initial);
        stateGroup.add(ready);
        stateGroup.add(set);
        stateGroup.add(play);
        stateGroup.add(finish);

        buttonMapping.put(GameStates.INITIAL, initial);
        buttonMapping.put(GameStates.READY, ready);
        buttonMapping.put(GameStates.SET, set);
        buttonMapping.put(GameStates.PLAYING, play);
        buttonMapping.put(GameStates.FINISHED, finish);

        initial.addActionListener(ActionBoard.initial);
        ready.addActionListener(ActionBoard.ready);
        set.addActionListener(ActionBoard.set);
        play.addActionListener(ActionBoard.play);
        finish.addActionListener(ActionBoard.finish);

        defineLayout();
    }

    // This eneds to be overwritten to specify the layouting of this group
    protected void defineLayout(){
        gameStateButtonContainer = new JPanel();
        TotalScaleLayout layout = new TotalScaleLayout(gameStateButtonContainer);

        gameStateButtonContainer.setLayout(layout);
        layout.add(0, 0, 0.2, 1, initial);
        layout.add(0.2, 0, 0.2, 1, ready);
        layout.add(0.4, 0, 0.2, 1, set);
        layout.add(0.6, 0, 0.2, 1, play);
        layout.add(0.8, 0, 0.2, 1, finish);

        //gameStateButtonContainer.setSize(100, 100);
        gameStateButtonContainer.setVisible(true);


        this.setLayout(new TotalScaleLayout(this));
        ((TotalScaleLayout) this.getLayout()).add(0, 0, 1, 1, gameStateButtonContainer);
        this.setVisible(true);
    }

    public void update(AdvancedData data)
    {
        initial.setEnabled(ActionBoard.initial.isLegal(data));
        ready.setEnabled(ActionBoard.ready.isLegal(data));
        set.setEnabled(ActionBoard.set.isLegal(data));
        play.setEnabled(ActionBoard.play.isLegal(data));
        finish.setEnabled(ActionBoard.finish.isLegal(data));

        buttonMapping.get(data.gameState).setSelected(true);

        highlight(finish, (data.gameState != GameStates.FINISHED)
                && (data.getRemainingGameTime(true) <= GUI.FINISH_HIGHLIGHT_SECONDS)
                && (finish.getBackground() != GUI.COLOR_HIGHLIGHT));
    }
}
