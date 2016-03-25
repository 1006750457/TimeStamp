package com.mobilephonesensor.test;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.base.presenter.Presenter;
import com.base.presenter.PresenterFactory;
import com.mobilephonesensor.R;
import com.mobilephonesensor.base.SupperActivity;


/**
 * Created by heng on 16-3-21.
 */
public class TestActivity extends SupperActivity implements TestPresenterView {

    private int[] images = {R.mipmap.themebg_d_2, R.mipmap.themebg_dn_2};

    private int current = 0;

    @Override
    protected int getContentResId() {
        return R.layout.act_test;
    }

    @Override
    protected PresenterFactory obtainPresenterFactory() {
        return new TestPresenterFactory();
    }

    @Override
    protected void onPresenterComplete(Presenter p) {
        super.onPresenterComplete(p);
        if (p instanceof TestPresenterFactory.TestPresenter) {
            ((TestPresenterFactory.TestPresenter) p).setDataToView();
        }
    }

    @Override
    public void showTestText(String showText) {

        ((TextView) find(R.id.act_test_text)).setText(showText);

        final ImageView imageView = find(R.id.act_text_image);

        final ImageSwitcher switcher = find(R.id.act_test_switcher);
        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView view = new ImageView(TestActivity.this);
                ImageSwitcher.LayoutParams params = new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT,
                        ImageSwitcher.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(params);
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                return view;
            }
        });
        switcher.setImageResource(images[current]);
        current = 1;
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current >= images.length) {
                    current = 0;
                }
                switcher.setImageResource(images[current]);
                current++;
            }
        });

        TestThread testThread = new TestThread(){

            @Override
            protected void onSuccess(Integer result) {
                super.onSuccess(result);
                imageView.setImageResource(result);
                Toast.makeText(getApplicationContext(), "testThread", Toast.LENGTH_LONG).show();
            }
        };
        testThread.execute();
    }
}
