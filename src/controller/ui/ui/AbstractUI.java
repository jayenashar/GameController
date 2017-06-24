package controller.ui.ui;

import controller.ui.GCGUI;
import controller.ui.ui.components.AbstractComponent;
import data.states.AdvancedData;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A modern extensible component based UI system which can be extended with elements
 */
public abstract class AbstractUI extends JFrame implements GCGUI {

    protected List<AbstractComponent> uiElements;

    public AbstractUI(boolean fullscreen, AdvancedData data){
        uiElements = new ArrayList<>();
    }

    @Override
    public void update(AdvancedData data) {
        for(AbstractComponent uiPart : uiElements){
            uiPart.update(data);
        }
        repaint();
    }
}
