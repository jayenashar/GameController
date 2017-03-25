package data.values;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by rkessler on 2017-03-25.
 */
public class ProtocolHelper {

    public static void main(String[] args) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        new ProtocolHelper();
    }

    ProtocolHelper() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        FileWriter fw = new FileWriter("protocol_appendix.html");

        List<Class> enumList = new ArrayList<Class>();
        enumList.add(GameStates.class);
        enumList.add(GameTypes.class);
        enumList.add(Penalties.class);
        enumList.add(PlayerResponses.class);
        enumList.add(SecondaryGameStates.class);
        enumList.add(TeamColors.class);

        for(Class c: enumList){
            Method row = c.getMethod("row");
            Method header = c.getMethod("header");
            StringBuilder sb = new StringBuilder();
            sb.append("<h3>");
            sb.append(c.getSimpleName());
            sb.append("</h3>\n");
            sb.append("<table border='1'>\n");
            sb.append(header.invoke(c.getEnumConstants()[0]));
            for(Object o : c.getEnumConstants()){
                sb.append(row.invoke(o));
            }
            sb.append("</table>\n\n");
            fw.append(sb.toString());
        }

        fw.close();
    }
}
