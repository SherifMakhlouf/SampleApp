package com.sherifmakhlouf.goeuro;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sherifmakhlouf.goeuro.api.GoEuroService;
import com.sherifmakhlouf.goeuro.api.model.GeoPositionTypeAdapter;
import com.sherifmakhlouf.goeuro.api.model.Location;
import com.sherifmakhlouf.goeuro.view.ViewModule;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(
        includes = {
                ViewModule.class
        },
        injects = {
                GoEuroApp.class
        },
        complete = false,
        library = true
)
public class AppModule {
    public static final String API_URL = "http://api.goeuro.com/api/v2/position/suggest/";
    private final GoEuroApp app;

    public AppModule(GoEuroApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(API_URL);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application app) {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(Endpoint endpoint, OkHttpClient client) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Location.GeoPosition.class, new GeoPositionTypeAdapter(app))
                .create();
        return new RestAdapter.Builder()
                .setEndpoint(endpoint).setClient(new OkClient(client)).setConverter(new GsonConverter(gson))
                .build();
    }
    @Provides @Singleton
    GoEuroService provideGalleryService(RestAdapter restAdapter) {
        return restAdapter.create(GoEuroService.class);
    }

    @Provides @Singleton
    List<Location> provideLocationList(){
        return new ArrayList<Location>();
    }
}
