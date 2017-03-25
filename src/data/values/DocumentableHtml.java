package data.values;

/**
 * Created by rkessler on 2017-03-25.
 */
public interface DocumentableHtml {

    default String header(){
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        sb.append(String.format("<th>Identifier</th>", name()));
        sb.append(String.format("<th>Byte Value</th>", value()));
        sb.append("</tr>");
        sb.append("\n");

        return sb.toString();
    }

    String name();

    byte value();

    default String row(){
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        sb.append(String.format("<td style='text-align:left; width: 30%%;'>%s</td>", name()));
        sb.append(String.format("<td style='text-align:center; width: 20%%;'>%s</td>", value()));
        sb.append("</tr>");
        sb.append("\n");
        return sb.toString();
    }
}
