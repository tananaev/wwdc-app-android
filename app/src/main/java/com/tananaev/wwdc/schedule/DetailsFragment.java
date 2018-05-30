package com.tananaev.wwdc.schedule;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tananaev.fmelib.EasyFragment;
import com.tananaev.wwdc.R;
import com.tananaev.wwdc.schedule.model.Content;
import com.tananaev.wwdc.schedule.model.Track;

import java.util.Locale;

public class DetailsFragment extends EasyFragment {

    public static final String KEY_CONTENT = "content";

    private MainApplication application;
    private MenuItem favoritesMenuItem;
    private Database database;
    private Content content;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        application = (MainApplication) getActivity().getApplication();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        application.getDatabase(database -> {
            this.database = database;
            content = (Content) getArguments().getSerializable(KEY_CONTENT);
            getActivity().setTitle(content.getTitle());

            Track track = database.getTrack(content.getTrackId());
            TextView trackView = (TextView) getView().findViewById(R.id.track);
            trackView.setText(track.getName());
            trackView.setTextColor(Color.parseColor(track.getColor()));

            String sessionNumber = content.getId().substring(content.getId().lastIndexOf('-') + 1);
            ((TextView) getView().findViewById(R.id.number)).setText(getString(R.string.session_number, sessionNumber));

            ((TextView) getView().findViewById(R.id.title)).setText(content.getTitle());
            ((TextView) getView().findViewById(R.id.description)).setText(content.getDescription());

            ((TextView) getView().findViewById(R.id.time)).setText(String.format(Locale.US,
                    "%1$tA, %1$tB %1$te, %1$tl:%1$tM %1$tp - %2$tl:%2$tM %2$tp", content.getStartTime(), content.getEndTime()));

            ((TextView) getView().findViewById(R.id.room)).setText(database.getRoom(content.getRoomId()).getName());
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details, menu);
        favoritesMenuItem = menu.findItem(R.id.action_favorites);
        if (content != null && application.getFavorites().contains(content.getId())) {
            favoritesMenuItem.setIcon(R.drawable.ic_star);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (content == null) {
            return false;
        }
        if (item.getItemId() == R.id.action_favorites) {
            if (application.getFavorites().contains(content.getId())) {
                application.removeFavorite(content.getId());
                favoritesMenuItem.setIcon(R.drawable.ic_star_border);
            } else {
                application.addFavorite(content.getId());
                favoritesMenuItem.setIcon(R.drawable.ic_star);
            }
            return true;
        } else if (item.getItemId() == R.id.action_calendar) {
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra(CalendarContract.Events.TITLE, content.getTitle());
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, content.getStartTime().getTime());
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, content.getEndTime().getTime());
            intent.putExtra(CalendarContract.Events.ALL_DAY, false);
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, database.getRoom(content.getRoomId()).getName());
            startActivity(intent);
            return true;
        }
        return false;
    }

}
