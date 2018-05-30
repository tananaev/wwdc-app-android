package com.tananaev.wwdc.schedule.model;

import java.io.Serializable;

public class Region implements Serializable {

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

    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private Geomerty geomerty;

    public Geomerty getGeomerty() {
        return geomerty;
    }

    public void setGeomerty(Geomerty geomerty) {
        this.geomerty = geomerty;
    }

}
