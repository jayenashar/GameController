package controller.action.ui;

import common.Log;
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
            int currentCount = data.team[side.value()].player[player].yellowCardCount;

            if (currentCount < 2){
                data.team[side.value()].player[player].yellowCardCount += 1;
                Log.state(data, "Added yellow card");
            }


        }
        if (this.color == Color.RED) {
            int currentCount = data.team[side.value()].player[player].redCardCount;

            if (currentCount == 0){
                data.team[side.value()].player[player].redCardCount = 1;
                Log.state(data, "Added red card");
            }
        }
    }

    @Override
    public boolean isLegal(AdvancedData data) {
        return true;
    }

}
