package com.sherifmakhlouf.goeuro.api;

import com.sherifmakhlouf.goeuro.api.model.Location;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface GoEuroService {
    @GET("/{local}/{term}")
    Observable<List<Location>> getLocations(
            @Path("local") String local,
            @Path("term") String term
    );


}
