package com.tananaev.wwdc.schedule.model;

import java.io.Serializable;

public class Data implements Serializable {

    private Event[] events;

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    private Content[] contents;

    public Content[] getContents() {
        return contents;
    }

    public void setContents(Content[] contents) {
        this.contents = contents;
    }

    private ImageType[] imageTypes;

    public ImageType[] getImageTypes() {
        return imageTypes;
    }

    public void setImageTypes(ImageType[] imageTypes) {
        this.imageTypes = imageTypes;
    }

    private Map[] maps;

    public Map[] getMaps() {
        return maps;
    }

    public void setMaps(Map[] maps) {
        this.maps = maps;
    }

    private Resource[] resources;

    public Resource[] getResources() {
        return resources;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

    private Room[] rooms;

    public Room[] getRooms() {
        return rooms;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }

    private Track[] tracks;

    public Track[] getTracks() {
        return tracks;
    }

    public void setTracks(Track[] tracks) {
        this.tracks = tracks;
    }

}
