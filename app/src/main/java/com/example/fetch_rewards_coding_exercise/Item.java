package com.example.fetch_rewards_coding_exercise;

import androidx.annotation.NonNull;

public class Item {
    private final String name;
    private final int num;

    Item(String name, int num) {
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " " + num;
    }
}
