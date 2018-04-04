package com.waracle.androidtest.screens.cakes;

import com.waracle.androidtest.BasePresenter;
import com.waracle.androidtest.model.Cake;

import java.util.List;

public interface CakesContract {

    interface View {
        void showCakes(List<Cake> cakeList);
    }

    interface Presenter extends BasePresenter<View> {
        void loadCakes();
    }
}