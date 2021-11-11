package com.mristudio.themelookstask.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mristudio.themelookstask.R;
import com.mristudio.themelookstask.adapter.PaginationAdapter;
import com.mristudio.themelookstask.apiclient.RetrofitClient;
import com.mristudio.themelookstask.model.product.ProductsResponse;
import com.mristudio.themelookstask.paging_lesenner.PaginationScrollListener;
import com.mristudio.themelookstask.service.Service;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView productsRV;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private PaginationAdapter paginationAdapter;
    private int end_limit = 0;
    private int start_limit = 4;
    private boolean isLastpage;
    private boolean isLoading;



    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        productsRV = findViewById(R.id.productsRV);
        progressBar = findViewById(R.id.progressBar);

        //  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        paginationAdapter = new PaginationAdapter(this);
        productsRV.setLayoutManager(gridLayoutManager);
        productsRV.setAdapter(paginationAdapter);

        service = RetrofitClient.getApiClient().create(Service.class);
        //Load All Products data to init total paroducts
        loadAllProduct();


        productsRV.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                start_limit = start_limit + 4;
                loadNextPage(start_limit);
            }

            @Override
            public boolean isLastPage() {
                // Log.e(TAG, "isLastPage: "+start_limit );
                return isLastpage;
            }

            @Override
            public boolean isLoading() {
                // Log.e(TAG, "isLoading: "+start_limit );
                return isLoading;
            }
        });

    }

    //Load All Products
    void loadAllProduct() {
        Call<List<ProductsResponse>> call = service.getAllProducts();

        call.enqueue(new Callback<List<ProductsResponse>>() {
            @Override
            public void onResponse(Call<List<ProductsResponse>> call, Response<List<ProductsResponse>> response) {

                if (response.isSuccessful()) {

                    end_limit = response.body().size();
                    Log.e(TAG, "onResponse: " + response.body().size());
                    loadFirstPage(start_limit);

                }
            }

            @Override
            public void onFailure(Call<List<ProductsResponse>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //Load Fist Products Page
    void loadFirstPage(int limit) {

        Call<List<ProductsResponse>> call = service.getProductsByLimits(limit);

        call.enqueue(new Callback<List<ProductsResponse>>() {
            @Override
            public void onResponse(Call<List<ProductsResponse>> call, Response<List<ProductsResponse>> response) {

                if (response.isSuccessful()) {

                    List<ProductsResponse> productsResponseList = response.body();
                    progressBar.setVisibility(View.GONE);

                    //paginationAdapter.addAll(productsResponseList);
                    paginationAdapter.setProductList(productsResponseList);

                    if (start_limit <= end_limit) paginationAdapter.addLoadingFooter();
                    else isLastpage = true;

                }
            }

            @Override
            public void onFailure(Call<List<ProductsResponse>> call, Throwable t) {

                t.printStackTrace();
            }
        });


    }

    //Load  Products Page By Limits
    void loadNextPage(int limit) {
        Call<List<ProductsResponse>> call = service.getProductsByLimits(limit);

        call.enqueue(new Callback<List<ProductsResponse>>() {
            @Override
            public void onResponse(Call<List<ProductsResponse>> call, Response<List<ProductsResponse>> response) {

                if (response.isSuccessful()) {
                    List<ProductsResponse> productsResponseList = response.body();

                    paginationAdapter.removeLoadingFooter();
                    isLoading = false;

                    //paginationAdapter.addAll(productsResponseList);
                    paginationAdapter.setProductList(productsResponseList);

                    if (start_limit != end_limit) paginationAdapter.addLoadingFooter();
                    else isLastpage = true;


                }
            }

            @Override
            public void onFailure(Call<List<ProductsResponse>> call, Throwable t) {

                t.printStackTrace();
            }
        });


    }
}