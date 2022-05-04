package fr.hyriode.hyrilobby.util;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 29/04/2022 at 17:32
 */
public class InventoryUtil {

    public static int[] getBarrierSlots() {
        return new int[] {
                0,
                1,
                2,
                6,
                7,
                8,
                9,
                17,
                36,
                44,
                45,
                46,
                47,
                51,
                52,
                53
        };
    }

    public static int[] getAvailableSlots() {
        return new int[] {
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
    }
}
