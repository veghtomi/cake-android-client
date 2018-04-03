package com.waracle.androidtest.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;

import com.waracle.androidtest.CakesTask;
import com.waracle.androidtest.view.adapter.CakeAdapter;

public class PlaceholderFragment extends ListFragment {

    private CakeAdapter mAdapter;
    private CakesTask cakesTask;

    public static PlaceholderFragment newInstance() {
        return new PlaceholderFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new CakeAdapter();
        setListAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        cakesTask = new CakesTask(mAdapter);
        cakesTask.execute();
    }

    @Override
    public void onStop() {
        cakesTask.cancel(true);

        super.onStop();
    }
}