package com.tananaev.wwdc.schedule;

import android.util.SparseArray;

import com.tananaev.wwdc.schedule.model.Content;
import com.tananaev.wwdc.schedule.model.Data;
import com.tananaev.wwdc.schedule.model.Event;
import com.tananaev.wwdc.schedule.model.Room;
import com.tananaev.wwdc.schedule.model.Track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Database {

    private Event currentEvent;
    private final List<Content> contents = new ArrayList<>();
    private final SparseArray<Room> rooms = new SparseArray<>();
    private final SparseArray<Track> tracks = new SparseArray<>();

    public Database(Data data) {
        currentEvent = null;
        for (Event event : data.getEvents()) {
            if (event.isCurrent()) {
                currentEvent = event;
                break;
            }
        }
        if (currentEvent != null) {
            for (Content content : data.getContents()) {
                if (content.getEventId().equals(currentEvent.getId())
                        && content.getStartTime() != null && content.getEndTime() != null) {
                    contents.add(content);
                }
            }
            Collections.sort(contents, (o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()));
        }
        for (Room room : data.getRooms()) {
            rooms.put(room.getId(), room);
        }
        for (Track track : data.getTracks()) {
            tracks.put(track.getId(), track);
        }
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public List<Content> getContents() {
        return contents;
    }

    public Room getRoom(int id) {
        return rooms.get(id);
    }

    public Track getTrack(int id) {
        return tracks.get(id);
    }

}
