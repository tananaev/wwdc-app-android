package com.tananaev.wwdc.schedule.model;

import java.io.Serializable;

public class Level implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int ordinal;

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    private int[] clFloors;

    public int[] getClFloors() {
        return clFloors;
    }

    public void setClFloors(int[] clFloors) {
        this.clFloors = clFloors;
    }

    private Geomerty geomerty;

    public Geomerty getGeomerty() {
        return geomerty;
    }

    public void setGeomerty(Geomerty geomerty) {
        this.geomerty = geomerty;
    }

    private Region[] regions;

    public Region[] getRegions() {
        return regions;
    }

    public void setRegions(Region[] regions) {
        this.regions = regions;
    }

}
