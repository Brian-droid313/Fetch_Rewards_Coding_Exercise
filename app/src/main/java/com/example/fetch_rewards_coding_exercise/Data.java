package com.example.fetch_rewards_coding_exercise;

import androidx.annotation.NonNull;

public class Data {
    private final int id;
    private final int listId;
    private final Item name;
    public Data(int id, int listId, String name) {
        this.id = id;
        this.listId = listId;

        String[] item = name.split(" ");
        this.name = new Item(item[0], Integer.parseInt(item[1]));
    }

    @NonNull
    @Override
    public String toString() {
        return this.id + " " + this.listId + " " + this.name;
    }

    public int getId() {
        return id;
    }

    public int getListId() {
        return listId;
    }

    public Item getName() {
        return name;
    }
}
