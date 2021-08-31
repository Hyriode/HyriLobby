package fr.hyriode.hyrilobby.util;

public enum UsefulHeads {

    MHF_Blaze("Blaze head"),
    MHF_CaveSpider("Cave Spider head"),
    MHF_Chicken("Chicken head"),
    MHF_Cow("Cow head"),
    MHF_Enderman("Enderman head"),
    MHF_Ghast("Ghast head"),
    MHF_Herobrine("Herobrine head"),
    MHF_LavaSlime("Magma Cube head"),
    MHF_MushrromCow("Mooshroom head"),
    MHF_Pig("Pig head"),
    MHF_PigZombie("Zombie Pigman head"),
    MHF_Sheep("Sheep head"),
    MHF_Slime("Slime head"),
    MHF_Spider("Spider head"),
    MHF_Squid("Squid head"),
    MHF_Villager("Villager head"),
    MHF_Golem("Iron Golem head"),
    MHF_Cactus("Cactus head"),
    MHF_Chest("Chest head"),
    MHF_Melon("Melon head"),
    MHF_OakLog("Oak Log head"),
    MHF_Pumpkin("Pumpkin head"),
    MHF_TNT("TNT head"),
    MHF_TNT2("TNT head"),
    MHF_ArrowUp("Arrow Up head"),
    MHF_ArrowDown("Arrow Down head"),
    MHF_ArrowLeft("Arrow Left head"),
    MHF_ArrowRight("Arrow Right head"),
    MHF_Exclamation("Exclamation Mark head"),
    MHF_Question("Question Mark head");

    private final String desc;

    private UsefulHeads(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }
}
