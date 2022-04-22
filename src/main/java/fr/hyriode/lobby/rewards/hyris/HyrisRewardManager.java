package fr.hyriode.lobby.rewards.hyris;

import fr.hyriode.lobby.api.redis.LobbyDataManager;

public class HyrisRewardManager extends LobbyDataManager<HyrisReward> {

    public HyrisRewardManager() {
        super("rewards:hyris:", HyrisReward.class);
    }
}
