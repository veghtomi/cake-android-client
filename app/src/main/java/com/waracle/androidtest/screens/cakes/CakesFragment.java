package com.waracle.androidtest.screens.cakes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;

import com.waracle.androidtest.model.Cake;
import com.waracle.androidtest.screens.cakes.adapter.CakeAdapter;

import java.util.List;

public class CakesFragment extends ListFragment implements CakesContract.View {

    private CakesPresenter presenter;

    private CakeAdapter adapter;

    public static CakesFragment newInstance() {
        return new CakesFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new CakesPresenter();
        presenter.attach(this);

        adapter = new CakeAdapter();
        setListAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.loadCakes();
    }

    @Override
    public void onStop() {
        presenter.detach();

        super.onStop();
    }

    @Override
    public void showCakes(List<Cake> cakeList) {
        adapter.setItems(cakeList);
    }
}