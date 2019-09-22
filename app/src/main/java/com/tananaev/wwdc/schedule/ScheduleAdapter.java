package com.tananaev.wwdc.schedule;

import android.content.Intent;
import android.graphics.Color;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tananaev.fmelib.EasyUtil;
import com.tananaev.wwdc.R;
import com.tananaev.wwdc.schedule.model.Content;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private Fragment fragment;
    private Database database;

    private ArrayList<Content> contents;

    public ScheduleAdapter(Fragment fragment, Database database) {
        this.fragment = fragment;
        this.database = database;
        filter(null);
    }

    public void filter(Set<String> ids) {
        if (ids == null) {
            contents = new ArrayList<>(database.getContents());
        } else {
            contents = new ArrayList<>();
            for (Content content : database.getContents()) {
                if (ids.contains(content.getId())) {
                    contents.add(content);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Content content = contents.get(position);

        DateFormat dayFormat = new SimpleDateFormat("EEEE h:mm a", Locale.US);
        dayFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        DateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US);
        timeFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

        if (content.getStartTime() == null) {
            holder.header.setVisibility(View.GONE);
        }

        String dayTime = dayFormat.format(content.getStartTime());
        if (position == 0 || !dayTime.equals(dayFormat.format(contents.get(position - 1).getStartTime()))) {
            holder.header.setText(dayTime);
            holder.header.setVisibility(View.VISIBLE);
        } else {
            holder.header.setVisibility(View.GONE);
        }

        holder.colorView.setBackgroundColor(
                Color.parseColor(database.getTrack(content.getTrackId()).getColor()));
        holder.titleView.setText(content.getTitle());

        holder.detailsView.setText(timeFormat.format(content.getStartTime())
                + " - " + timeFormat.format(content.getEndTime())
                + " - " + database.getRoom(content.getRoomId()).getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = EasyUtil.createAliasIntent(fragment.getContext(), ".DetailsActivity");
            intent.putExtra(DetailsFragment.KEY_CONTENT, content);
            fragment.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView header;
        private View colorView;
        private TextView titleView;
        private TextView detailsView;

        public ViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            colorView = itemView.findViewById(R.id.color);
            titleView = itemView.findViewById(R.id.title);
            detailsView = itemView.findViewById(R.id.details);
        }

    }

}
