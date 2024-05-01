package Entities;

public class LivreurItem {
    private final int id;
    private final String name;

    public LivreurItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
