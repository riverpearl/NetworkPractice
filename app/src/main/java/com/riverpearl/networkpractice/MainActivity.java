package com.riverpearl.networkpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.riverpearl.networkpractice.autodata.Product;
import com.riverpearl.networkpractice.autodata.Tstore;
import com.riverpearl.networkpractice.manager.NetworkManager;
import com.riverpearl.networkpractice.manager.NetworkRequest;
import com.riverpearl.networkpractice.manager.TstoreSearchRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edit_keyword)
    TextView keywordView;
    @BindView(R.id.rv_app_list)
    RecyclerView listView;

    ProductAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        pAdapter = new ProductAdapter();
        pAdapter.setOnProductAdapterItemClickListener(new ProductAdapter.OnProductAdapterItemClickListener() {
            @Override
            public void onProductAdapterItemClick(View view, Product product, int position) {
                Toast.makeText(MainActivity.this, product.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(pAdapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        listView.setLayoutManager(manager);
    }

    @OnClick(R.id.btn_search)
    public void onSearch(View view) {
        String keyword = keywordView.getText().toString();
        if (!TextUtils.isEmpty(keyword)) {
            TstoreSearchRequest request = new TstoreSearchRequest(keyword);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<Tstore>() {

                @Override
                public void onSuccess(NetworkRequest<Tstore> request, Tstore result) {
                    pAdapter.addAll(result.getProducts().getProduct());
                }

                @Override
                public void onFail(NetworkRequest<Tstore> request, int errorCode, String errorMsg) {
                    Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
