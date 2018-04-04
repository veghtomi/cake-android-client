package com.waracle.androidtest.screens.cakes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.MenuItem;
import android.view.View;

import com.waracle.androidtest.R;
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

        setHasOptionsMenu(true);

        presenter = new CakesPresenter();

        adapter = new CakeAdapter();
        setListAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                presenter.loadCakes();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.attach(this);
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