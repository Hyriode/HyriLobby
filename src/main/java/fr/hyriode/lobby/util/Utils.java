package fr.hyriode.lobby.util;

import fr.hyriode.lobby.vip.casino.game.rollermachines.ERollingMachinesLoot;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static ERollingMachinesLoot getRollerMachinesLoot() {
        final float random = ThreadLocalRandom.current().nextFloat();
        float percentage = 0;
        for (ERollingMachinesLoot probability : ERollingMachinesLoot.values()) {
            percentage += probability.getProbability();
            if(random <= percentage) {
                return probability;
            }
        }
        return ERollingMachinesLoot.NOTHING;
    }
}
