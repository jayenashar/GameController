package controller.ui.helper;

import java.awt.*;

/**
 * Created by rkessler on 2017-06-24.
 */
public class FontHelper {

    protected static final int TITLE_FONT_SIZE = 24;
    protected static final String STANDARD_FONT = "Helvetica";

    static public Font boldStandardFont(){
        return new Font(STANDARD_FONT, Font.BOLD, TITLE_FONT_SIZE);
    }

}
