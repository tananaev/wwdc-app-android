package com.tananaev.wwdc.schedule;

import android.app.Application;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.Rfc3339DateJsonAdapter;
import com.tananaev.wwdc.schedule.model.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainApplication extends Application {

    private static final String TAG = MainApplication.class.getSimpleName();

    public static final String PREFERENCE_FAVORITES = "favorites";

    private static final String DATABASE = "data.bin";
    private static final String HOST = "https://api2017.wwdc.io/";

    public interface DatabaseCallback {
        void onReady(Database data);
    }

    private Database database;
    private Set<String> favorites;
    private final ArrayList<DatabaseCallback> callbacks = new ArrayList<>();

    public void getDatabase(DatabaseCallback callback) {
        if (database != null) {
            callback.onReady(database);
        } else {
            callbacks.add(callback);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadDatabase(false);
        favorites = PreferenceManager.getDefaultSharedPreferences(this)
                .getStringSet(PREFERENCE_FAVORITES, new HashSet<>());
    }

    public Set<String> getFavorites() {
        return favorites;
    }

    public void addFavorite(String id) {
        favorites.add(id);
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit().clear().putStringSet(PREFERENCE_FAVORITES, favorites).apply();
    }

    public void removeFavorite(String id) {
        favorites.remove(id);
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit().clear().putStringSet(PREFERENCE_FAVORITES, favorites).apply();
    }

    public void loadDatabase(boolean reload) {
        database = null;
        final File file = new File(getFilesDir(), DATABASE);
        if (!reload && file.exists()) {
            new AsyncTask<Void, Void, Data>() {
                @Override
                protected Data doInBackground(Void... params) {
                    try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                        return (Data) input.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        Log.w(TAG, e);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Data data) {
                    database = new Database(data);
                    for (DatabaseCallback callback : callbacks) {
                        callback.onReady(database);
                    }
                    callbacks.clear();
                }
            }.execute();
        } else {
            MoshiConverterFactory converterFactory = MoshiConverterFactory.create(
                    new Moshi.Builder().add(Date.class, new Rfc3339DateJsonAdapter()).build());

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(HOST).addConverterFactory(converterFactory).build();

            retrofit.create(Service.class).getData().enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {
                    if (response.isSuccessful()) {
                        Data data = response.body();
                        database = new Database(data);
                        for (DatabaseCallback callback : callbacks) {
                            callback.onReady(database);
                        }
                        callbacks.clear();
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
                                    output.writeObject(data);
                                } catch (IOException e) {
                                    Log.w(TAG, e);
                                }
                                return null;
                            }
                        }.execute();
                    }
                }

                @Override
                public void onFailure(Call<Data> call, Throwable t) {
                    Log.w(TAG, t);
                }
            });
        }
    }

}
