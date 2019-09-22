package com.tananaev.wwdc.schedule;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tananaev.fmelib.EasyFragment;
import com.tananaev.wwdc.R;

public class ScheduleFragment extends EasyFragment implements NavigationView.OnNavigationItemSelectedListener {

    private MainApplication application;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MenuItem favoritesMenuItem;
    private ScheduleAdapter adapter;
    private boolean showFavorites;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        application = (MainApplication) getActivity().getApplication();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        drawerLayout = (DrawerLayout) getView();
        navigationView = (NavigationView) getView().findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        loadDatabase();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.filter(showFavorites ? application.getFavorites() : null);
        }
    }

    public void loadDatabase() {
        application.getDatabase(database -> {
            if (getActivity() != null) {
                getActivity().setTitle(database.getCurrentEvent().getName());
                adapter = new ScheduleAdapter(this, database);
                adapter.filter(showFavorites ? application.getFavorites() : null);
                ((RecyclerView) getView().findViewById(android.R.id.list)).setAdapter(adapter);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.schedule, menu);
        favoritesMenuItem = menu.findItem(R.id.action_favorites);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reload) {
            application.loadDatabase(true);
            loadDatabase();
            return true;
        } else if (item.getItemId() == R.id.action_favorites) {
            if (showFavorites) {
                adapter.filter(null);
                favoritesMenuItem.setIcon(R.drawable.ic_star_border);
            } else {
                adapter.filter(application.getFavorites());
                favoritesMenuItem.setIcon(R.drawable.ic_star);
            }
            showFavorites = !showFavorites;
            return true;
        } else if (item.getItemId() == R.id.action_filter) {
            if (drawerLayout.isDrawerOpen(Gravity.END)) {
                drawerLayout.closeDrawer(Gravity.END);
            } else {
                drawerLayout.openDrawer(Gravity.END);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        return false;
    }

}
