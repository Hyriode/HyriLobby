package fr.hyriode.lobby.util;

import java.util.List;

/**
 * Created by AstFaster
 * on 29/06/2022 at 13:35
 */
public class ListUtil {

    public static List<String> replace(List<String> input, String character, String replacer) {
        input.replaceAll(s -> s.replace(character, replacer));

        return input;
    }

}
