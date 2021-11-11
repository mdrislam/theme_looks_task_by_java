package com.mristudio.themelookstask.service;

import com.mristudio.themelookstask.model.product.ProductsResponse;
import com.mristudio.themelookstask.model.product_details.ProductDetailsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @GET("products")
    Call<List<ProductsResponse>> getAllProducts();

    @GET("products/{id}")
    Call<ProductDetailsResponse> getProductDetailsById(
            @Path("id") int id
    );
    @GET("products")
    Call<List<ProductsResponse>> getProductsByLimits(
            @Query("limit") int id
    );

}
