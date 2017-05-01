package controller.ui.ui.customized;

import controller.ui.gameplay.GUI;

import javax.swing.*;

/**
 * Created by rkessler on 2017-03-27.
 */
public class ToggleButton extends JToggleButton {

    protected static final long serialVersionUID = -7733709666734108610L;

    public ToggleButton()
    {
        setMargin(GUI.insets);
    }

    public ToggleButton(String text)
    {
        setMargin(GUI.insets);
        setText(text);
    }

    public void setText(String text)
    {
        super.setText(String.format(GUI.BUTTON_MASK, text));
    }

}
