package com.example.asteroids2.ConstantVar;

public enum Size {
    SMALL(1),
    MEDIUM(2),
    LARGE(3);

    private int size;

    Size(int size){
        this.size = size;
    }

    public  int setSize(){
        return  size;
    }

}
