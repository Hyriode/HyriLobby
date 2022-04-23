package fr.hyriode.lobby.rewards;

import fr.hyriode.lobby.rewards.hyris.HyrisRewardManager;

public class RewardManager {

    private final HyrisRewardManager hyris;

    public RewardManager() {
        this.hyris = new HyrisRewardManager();
    }

    public HyrisRewardManager getHyrisRewardManager() {
        return this.hyris;
    }

}
