package fr.hyriode.lobby.utils;

import fr.hyriode.api.rank.EHyriRank;
import fr.hyriode.api.rank.HyriPermission;

public enum LobbyPermission implements HyriPermission {

    ADMIN,
    BUILD,
    DROP;

    public static void register() {
        ADMIN.add(EHyriRank.ADMINISTRATOR);
        BUILD.add(EHyriRank.ADMINISTRATOR, EHyriRank.BUILDER);
        DROP.add(EHyriRank.ADMINISTRATOR, EHyriRank.BUILDER, EHyriRank.STAFF);
    }
}
