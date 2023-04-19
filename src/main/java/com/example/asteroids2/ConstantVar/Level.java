package com.example.asteroids2.ConstantVar;

public enum Level {
    EASY_TIME(10000),
    MODERATE_TIME(20000),
    HARD_TIME(30000),

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
