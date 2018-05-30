package com.tananaev.wwdc.schedule.model;

import java.io.Serializable;

public class Geomerty implements Serializable {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private int[][][][] coordinates;

    public int[][][][] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[][][][] coordinates) {
        this.coordinates = coordinates;
    }

}
