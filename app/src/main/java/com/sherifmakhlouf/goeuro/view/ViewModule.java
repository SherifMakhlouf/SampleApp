package com.sherifmakhlouf.goeuro.view;

import dagger.Module;

@Module(injects = {SearchActivity.class, LocationFragment.class,CalendarFragment.class},
        complete = false,
        library = true
)
public class ViewModule {
}
