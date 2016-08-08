package com.riverpearl.networkpractice;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.riverpearl.networkpractice.autodata.Product;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tacademy on 2016-08-08.
 */
public class ProductView extends FrameLayout {

    Product product;

    @BindView(R.id.text_title)
    TextView titleView;

    @BindView(R.id.text_like)
    TextView likeView;

    @BindView(R.id.text_download)
    TextView downloadView;

    @BindView(R.id.text_desc)
    TextView descView;

    public ProductView(Context context) {
        this(context, null);
    }

    public ProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_product, this);
        ButterKnife.bind(this);
    }
}
