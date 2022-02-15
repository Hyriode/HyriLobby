package fr.hyriode.lobby.api.items;

public class Item {

    private final String name;
    private String material;
    private short data;

    public Item(String material) {
        this(" ", material);
    }

    public Item(String material, int data) {
        this(" ", material, data);
    }

    public Item(String name, String material) {
        this(name, material, 0);
    }

    public Item(String name, String material, int data) {
        this.name = name;
        this.material = material;
        this.data = (short) data;
    }

    public String getName() {
        return this.name;
    }

    public String getMaterial() {
        return this.material;
    }

    public short getData() {
        return this.data;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setData(int data) {
        this.data = (short) data;
    }

    public boolean isSimilar(Item compareTo) {
        return this.isSimilarWithoutData(compareTo) && this.data == compareTo.getData();
    }

    public boolean isSimilarWithoutData(Item compareTo) {
        return this.name.equalsIgnoreCase(compareTo.getName()) && this.material.equalsIgnoreCase(compareTo.getMaterial());
    }
}
