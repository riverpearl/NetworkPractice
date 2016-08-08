package com.riverpearl.networkpractice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riverpearl.networkpractice.autodata.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-08.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder>
    implements ProductViewHolder.OnProductItemClickListener {

    List<Product> items = new ArrayList<>();

    public void add(Product p) {
        items.add(p);
        notifyDataSetChanged();
    }

    public void addAll(Product[] p) {
        items.addAll(Arrays.asList(p));
        notifyDataSetChanged();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_product, parent, false);
        ProductViewHolder pvh = new ProductViewHolder(view);
        pvh.setOnProductItemClickListener(this);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.setProduct(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnProductAdapterItemClickListener {
        public void onProductAdapterItemClick(View view, Product product, int position);
    }

    OnProductAdapterItemClickListener pListener;
    public void setOnProductAdapterItemClickListener(OnProductAdapterItemClickListener listener) {
        pListener = listener;
    }

    @Override
    public void onProductItemClick(View view, Product product, int position) {
        if (pListener != null)
            pListener.onProductAdapterItemClick(view, product, position);
    }
}
