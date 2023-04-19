package com.example.asteroids2.ConstantVar;

public enum Level {
    EASY_TIME(60000),
    MODERATE_TIME(120000),
    HARD_TIME(180000),

    EASY_LEVEL(1),
    MODERATE_LEVEL(2),
    HARD_LEVEL(3);

    private int level;

    Level(int level){
        this.level = level;
    }

    public int setLevel(){
        return level;
    }

}
