package fr.hyriode.lobby.vip.casino.game.inventory;

import fr.hyriode.lobby.vip.casino.game.AGame;

public class GameInventoryBuilder {

    private AGame game;
    private int minimum;
    private int maximum;
    private int[] modifiers;
    private InventoryValidatorCallback clickCallback;

    public GameInventoryBuilder setMinimum(int minimum) {
        this.minimum = minimum;

        return this;
    }

    public GameInventoryBuilder setMaximum(int maximum) {
        this.maximum = maximum;

        return this;
    }

    public GameInventoryBuilder setModifiers(int... modifiers)  {
        this.modifiers = modifiers;

        return this;
    }

    public GameInventoryBuilder setButtonAction(InventoryValidatorCallback clickCallback) {
        this.clickCallback = clickCallback;

        return this;
    }

    public GameInventoryBuilder setGame(AGame game) {
        this.game = game;

        return this;
    }

    public GameInventory build() {
        return new GameInventory(this.game,
                54,
                this.minimum,
                this.maximum,
                this.modifiers == null ? new int[]{1, 10} : this.modifiers,
                this.clickCallback == null ? (hyris) -> {} : this.clickCallback);
    }
}
