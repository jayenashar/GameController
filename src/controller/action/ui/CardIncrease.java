package controller.action.ui;

import controller.action.ActionType;
import controller.action.GCAction;
import data.states.AdvancedData;
import data.values.Side;

import java.awt.*;

/**
 * Created by rkessler on 2017-06-11.
 */
public class CardIncrease extends GCAction {

    private final Side side;
    private final int player;
    private final Color color;

    public CardIncrease(Side side, int player, Color color) {
        super(ActionType.UI);
        this.player = player;
        this.side = side;
        this.color = color;
    }

    @Override
    public void perform(AdvancedData data) {
        if (this.color == Color.YELLOW) {
            data.team[side.value()].player[player].yellowCardCount += 1;
        }
        if (this.color == Color.RED) {
            data.team[side.value()].player[player].redCardCount += 1;
        }
    }

    @Override
    public boolean isLegal(AdvancedData data) {
        return true;
    }

}
