package com.tananaev.wwdc.schedule.model;

import java.io.Serializable;

public class ImageType implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private ImageVariant[] variants;

    public ImageVariant[] getVariants() {
        return variants;
    }

    public void setVariants(ImageVariant[] variants) {
        this.variants = variants;
    }

}
