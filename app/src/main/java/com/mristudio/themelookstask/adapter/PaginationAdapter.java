package com.mristudio.themelookstask.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mristudio.themelookstask.R;
import com.mristudio.themelookstask.activity.DetailsActivity;
import com.mristudio.themelookstask.model.product.ProductsResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ProductsResponse> productList;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        productList = new ArrayList<>();
    }

    public void setProductList(List<ProductsResponse> movieList) {
        this.productList = movieList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.lyt_product_row, parent, false);
                viewHolder = new ProductViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.lyt_progress, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ProductsResponse product = productList.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                ProductViewHolder productViewHolder = (ProductViewHolder) holder;

                double double_price = product.getPrice();
                int int_price = (int) double_price;

                double double_rating = product.getRating().getRate();
                float float_rating = (float) double_rating;

                productViewHolder.new_priceTV.setText("৳"+int_price);
                // productViewHolder.old_priceTv.setText(String.valueOf(position));
                productViewHolder.product_rating.setRating(float_rating);
                productViewHolder.rating_counterTV.setText(String.valueOf(product.getRating().getCount()));
                productViewHolder.product_nameTV.setText(product.getTitle());
                Log.e(TAG, "onBindViewHolder: " + product.getId());

                Picasso.get().load(product.getImage()).into(productViewHolder.product_image);

                productViewHolder.productsClickRl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("id", product.getId());
                        context.startActivity(intent);
                    }
                });

                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == productList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ProductsResponse());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = productList.size() - 1;
        ProductsResponse result = getItem(position);

        if (result != null) {
            productList.remove(position);
            notifyItemRemoved(position);
        }
    }


    public void add(ProductsResponse product) {
        productList.add(product);
        notifyItemInserted(productList.size() - 1);
    }

    public void addAll(List<ProductsResponse> productResult) {
        for (ProductsResponse result : productResult) {
            add(result);
        }
    }

    public ProductsResponse getItem(int position) {
        return productList.get(position);
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout productsClickRl;
        private ImageView product_image;
        private TextView new_priceTV, old_priceTv, rating_counterTV, product_nameTV;
        private RatingBar product_rating;


        public ProductViewHolder(View itemView) {
            super(itemView);
            productsClickRl = itemView.findViewById(R.id.productsClickRl);
            product_image = itemView.findViewById(R.id.product_image);
            new_priceTV = itemView.findViewById(R.id.new_priceTV);
            old_priceTv = itemView.findViewById(R.id.old_priceTv);
            rating_counterTV = itemView.findViewById(R.id.rating_counterTV);
            product_nameTV = itemView.findViewById(R.id.product_nameTV);
            product_rating = itemView.findViewById(R.id.product_rating);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.lodingPB);

        }
    }

}
