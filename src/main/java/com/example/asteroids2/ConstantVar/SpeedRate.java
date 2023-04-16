package com.example.asteroids2.ConstantVar;

public enum SpeedRate {
    LOWER(1),
    NORMAL(2),
    FASTER(3);

    private int speedRate;

    SpeedRate(int speedRate){
        this.speedRate = speedRate;
    }

    public  int setSpeedRate(){
        return  speedRate;
    }

}
