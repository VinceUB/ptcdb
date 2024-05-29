package net.ultrabanana.ptcdb.cl;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter @Setter
public class Card {
    private String name = "Unknown";
    private String description = "Unknown";
    private String generation = "Unknown";
    private HashSet<String> possibleOwners = new HashSet<>();
    private String ownersText = "Ukendt";
    private String history = "No data";
    private String image = "No data";

    private int indent = 0;

    public void addPossibleOwner(String owner){
        possibleOwners.add(owner.toLowerCase());
    }

    String toHtml(){
        return String.format("""
                <tr>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td class="owner %s">%s</td>
                    <td>%s</td>
                    <td>%s</td>
                </tr>
                """.indent(indent),
                name,
                description,
                generation,
                String.join(" ", possibleOwners),
                ownersText,
                history,
                image.equals("No data")?
                        image :
                        String.format(
                                "<img src=\"images/%s\", width=\"100%%\", alt=\"%s\">",
                                image, description)
                );
    }
}
