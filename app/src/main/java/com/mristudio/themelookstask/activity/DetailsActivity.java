package com.mristudio.themelookstask.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mristudio.themelookstask.R;
import com.mristudio.themelookstask.apiclient.RetrofitClient;
import com.mristudio.themelookstask.model.product.ProductsResponse;
import com.mristudio.themelookstask.model.product_details.ProductDetailsResponse;
import com.mristudio.themelookstask.service.Service;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsActivity";
    private Toolbar toolbar;
    private ImageView productImageIV;
    private TextView product_nameTV, product_priceTV, rating_counterTV, descriptionTV;
    private RatingBar product_rating;
    private Button addToCart, buyNow;
    private ProgressBar item_progress_bar;
    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImageIV = findViewById(R.id.productImageIV);
        product_nameTV = findViewById(R.id.product_nameTV);
        product_priceTV = findViewById(R.id.product_priceTV);
        rating_counterTV = findViewById(R.id.rating_counterTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        product_rating = findViewById(R.id.product_rating);
        addToCart = findViewById(R.id.addToCart);
        buyNow = findViewById(R.id.buyNow);
        item_progress_bar = findViewById(R.id.item_progress_bar);
        item_progress_bar.setVisibility(View.VISIBLE);
        service = RetrofitClient.getApiClient().create(Service.class);


        if (getIntent().getExtras() != null) {
            loadProductById(getIntent().getExtras().getInt("id"));
        }

    }

    //Load Product By Id
    private void loadProductById(int id) {
        Log.e(TAG, "loadProductById: "+id );
        Call<ProductDetailsResponse> call = service.getProductDetailsById(id);

        call.enqueue(new Callback<ProductDetailsResponse>() {
            @Override
            public void onResponse(Call<ProductDetailsResponse> call, Response<ProductDetailsResponse> response) {
                if (response.isSuccessful()) {
                    ProductDetailsResponse product = response.body();
                    double double_price = product.getPrice();
                    int int_price = (int) double_price;

                    double double_rating = product.getRating().getRate();
                    float float_rating = (float) double_rating;

                    Picasso.get().load(product.getImage()).into(productImageIV);
                    product_nameTV.setText(product.getTitle());
                    product_priceTV.setText("৳"+int_price);
                    product_rating.setRating(float_rating);
                    rating_counterTV.setText("(" + product.getRating().getCount() + ")");
                    descriptionTV.setText(product.getDescription());
                    item_progress_bar.setVisibility(View.GONE);

                } else {
                    item_progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ProductDetailsResponse> call, Throwable t) {
                item_progress_bar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}