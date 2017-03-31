package controller.ui.ui.components;

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
        stateGroup.add(ready);
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
        this.setSize(400, 300);
        this.add(initial);
        this.add(ready);
        this.add(set);
        this.add(play);
        this.add(finish);
        this.setLayout(new FlowLayout());
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
