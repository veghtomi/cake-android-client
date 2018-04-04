package com.waracle.androidtest.screens.cakes;

import android.os.AsyncTask;

import com.waracle.androidtest.model.Cake;
import com.waracle.androidtest.network.tasks.CakesTask;

import java.util.List;

public class CakesPresenter implements CakesContract.Presenter {

    private CakesContract.View screen;
    private AsyncTask<Void, Void, List<Cake>> cakesTask;

    @Override
    public void loadCakes() {
        cakesTask = new CakesTask(new CakesTask.OnItemsLoadedListener() {
            @Override
            public void onItemsLoaded(List<Cake> newCakes) {
                screen.showCakes(newCakes);
            }
        }).execute();
    }

    @Override
    public void attach(CakesContract.View view) {
        screen = view;
    }

    @Override
    public void detach() {
        cakesTask.cancel(true);

        screen = null;
    }
}
