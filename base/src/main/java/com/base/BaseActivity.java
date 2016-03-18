package com.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.presenter.Presenter;
import com.base.presenter.PresenterFactory;
import com.base.presenter.PresenterLoader;

/**
 * Created by heng on 16-3-16.
 */
public abstract class BaseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Presenter> {

    private final static int ACT_LOADER_ID = 1;

    private PresenterFactory mPresenterFactory;

    private Presenter mPresenter;

    protected abstract int getContentResId();

    /** 实例化控件在这里执行 */
    protected abstract void bindView();

    protected PresenterFactory obtainPresenterFactory(){
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentResId());
        bindView();
        setSupportActionBar(onCreateToolbar());
        mPresenterFactory = obtainPresenterFactory();
        if (mPresenterFactory != null) {
            getSupportLoaderManager().initLoader(ACT_LOADER_ID, null, this);
        }
    }

    protected Toolbar onCreateToolbar() {
        return null;
    }

    protected <T extends View> T find(int resId) {
        return (T) (findViewById(resId));
    }

    protected LayoutInflater loaderInflater(){
        return (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onStart() {
        if (mPresenter != null) {
            mPresenter.onViewAttach(this);
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (mPresenter != null) {
            mPresenter.onViewDetach();
        }
        super.onStop();
    }

    @Override
    public Loader<Presenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, mPresenterFactory);
    }

    @Override
    public void onLoadFinished(Loader<Presenter> loader, Presenter data) {
        if (mPresenter == null) {
            this.mPresenter = data;
        }
    }

    @Override
    public void onLoaderReset(Loader<Presenter> loader) {

    }

    protected ViewGroup getViewGroup() {
        return null;
    }
}
