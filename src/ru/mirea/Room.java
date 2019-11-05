package ru.mirea;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Room {
    public List<Integer> paths = new ArrayList<>();

    private Item item = null;

    public Item getItem() {
        return item;
    }

    public void takeItem() {
        item = null;
    }

    public Room() {
    }

    public Room(List<Integer> paths) {
        this.paths = paths;
    }

    public Room(Item item) {
        this.item = item;
    }

    public Room(List<Integer> paths, Item item) {
        this.paths = paths;
        this.item = item;
    }
}
