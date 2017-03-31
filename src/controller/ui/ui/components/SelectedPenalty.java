package controller.ui.ui.components;

import data.values.Penalties;

/**
 * Created by rkessler on 2017-03-29.
 */
public interface SelectedPenalty {

    /** Returns the selected penalty **/
    Penalties selectedPenalty();

    /** Resets the selection of all penalties **/
    void reset();
}
