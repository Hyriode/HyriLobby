package fr.hyriode.lobby.game.host;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.host.HostData;
import fr.hyriode.api.host.IHostManager;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyrame.item.ItemHead;
import fr.hyriode.lobby.util.UsefulHead;

/**
 * Created by AstFaster
 * on 30/07/2022 at 13:22
 */
public enum HostState {

    STARTING("starting", UsefulHead.YELLOW_CUBE),
    WHITELISTED("whitelisted", UsefulHead.BLUE_CUBE),
    WAITING("waiting", UsefulHead.LIME_GREEN_CUBE),
    PLAYING("playing", UsefulHead.RED_CUBE);

    private HyriLanguageMessage display;

    private final String name;
    private final ItemHead head;

    HostState(String name, ItemHead head) {
        this.name = name;
        this.head = head;
    }

    public HyriLanguageMessage getDisplay() {
        return this.display == null ? this.display = HyriLanguageMessage.get("host.state." + this.name + ".display") : this.display;
    }

    public ItemHead getHead() {
        return this.head;
    }

    public static HostState getFromServer(HyggServer server) {
        final IHostManager hostAPI = HyriAPI.get().getHostManager();
        final HostData hostData = hostAPI.getHostData(server);

        if (hostData == null) {
            return null;
        }

        final HyggServer.State state = server.getState();

        if (state == HyggServer.State.CREATING || state == HyggServer.State.STARTING) {
            return STARTING;
        } else if (state == HyggServer.State.READY && hostData.isWhitelisted()) {
            return WHITELISTED;
        } else if (state == HyggServer.State.READY && !hostData.isWhitelisted()) {
            return WAITING;
        } else if (state == HyggServer.State.PLAYING) {
            return PLAYING;
        }
        return null;
    }

}
