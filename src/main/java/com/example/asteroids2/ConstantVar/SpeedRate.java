package com.example.asteroids2.ConstantVar;

public enum SpeedRate {
    LOWER(1),
    NORMAL(3),
    FASTER(5);

    private int speedRate;

    SpeedRate(int speedRate) {
        this.speedRate = speedRate;
    }

    public int setSpeedRate() {
        return speedRate;
    }

}
