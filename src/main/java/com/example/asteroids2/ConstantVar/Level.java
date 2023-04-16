package com.example.asteroids2.ConstantVar;

public enum Level {
    EASY(60000),
    MODERATE(120000),
    HARD(180000);

    private int level;

    Level(int level){
        this.level = level;
    }

    public int setLevel(){
        return level;
    }
}
