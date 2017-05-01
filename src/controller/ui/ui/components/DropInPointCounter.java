package controller.ui.ui.components;

import data.states.AdvancedData;
import data.values.Penalties;
import data.values.Side;

import java.util.HashMap;
import java.util.Map;

/**
 * A class counting the drop in points for all players
 * Created by rkessler on 2017-05-01.
 */
public class DropInPointCounter {

    private Map<Side, int[]> robotPoints;
    private Map<Side, Refreshable> componentMap;

    public DropInPointCounter(){
        robotPoints = new HashMap<>();
        componentMap = new HashMap<>();
    }

    public void initializeSide(Side side, int teamSize, Refreshable refreshable){
        int[] points = new int[teamSize];
        for (int i=0; i < points.length; i++){
            points[i] = 0;
        }
        robotPoints.put(side, points);
        componentMap.put(side, refreshable);
    }

    public void scoreGoal(AdvancedData data, Side teamScoring){
        int[] pointList = robotPoints.get(teamScoring);

        for (int i = 0; i < pointList.length; i++){
            Penalties penalty = data.team[teamScoring.value()].player[i].penalty;
            if (penalty != Penalties.SUBSTITUTE && penalty != Penalties.HL_SERVICE) {
                pointList[i] += 1;
            }
        }

        int[] pointListOther = robotPoints.get(teamScoring.getOther());
        for (int i = 0; i < pointListOther.length; i++){
            pointListOther[i] -= 1;
        }

        componentMap.get(teamScoring.getOther()).refresh();
    }

    public int getPoints(Side side, int idx) {
        return robotPoints.get(side)[idx];
    }

    public void deltaPoints(Side side, int idx, int delta) {
        robotPoints.get(side)[idx] += delta;
    }
}
