package fr.hyriode.lobby.quest;

import fr.hyriode.api.util.Skin;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.util.UsefulSkin;
import org.bukkit.Location;

public class TestQuest extends AQuest {
    public TestQuest(HyriLobby plugin) {
        super(plugin);
    }

    @Override
    protected String getNPCName() {
        return "test";
    }

    @Override
    protected Location getNPCLocation() {
        return new Location(IHyrame.WORLD.get(), -367D, 163D, -14D);
    }

    @Override
    protected Skin getSkin() {
        return UsefulSkin.EARTH_SKIN;
    }

    @Override
    protected String getLore() {
        return "Ceci est un lore";
    }
}
