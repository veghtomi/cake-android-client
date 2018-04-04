package com.waracle.androidtest;

public interface BasePresenter<T> {

    void attach(T view);

    void detach();
}
