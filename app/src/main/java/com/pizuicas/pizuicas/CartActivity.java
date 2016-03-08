package com.pizuicas.pizuicas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pizuicas.pizuicas.application.ShopifyApplication;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.model.CartLineItem;
import com.shopify.buy.model.Checkout;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CartActivity extends AppCompatActivity {

    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    private String TAG = CartActivity.class.getName();

    private List<CartLineItem> cartProducts;

    protected ShopifyApplication getShopifyApplication() {
        return (ShopifyApplication) getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCheckout();
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("Checkout")
                        .build());
            }
        });

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        mTracker = getShopifyApplication().getDefaultTracker();
        // [END shared_tracker]
    }

    /**
     * When the user selects a product, create a new checkout for that product.
     *
     */
    private void createCheckout() {
        //showLoadingDialog(R.string.syncing_data);

        getShopifyApplication().createCheckout(new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                //dismissLoadingDialog();
                //onCheckoutCreated(checkout);
                Log.d(TAG, "success: checkout created");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(checkout.getWebUrl()));
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                onError(error);
            }
        });
    }

    protected void onError(RetrofitError error) {
        onError(BuyClient.getErrorBody(error));
    }

    /**
     * When we encounter an error with one of our network calls, we abort and return to the previous activity.
     * In a production app, you'll want to handle these types of errors more gracefully.
     *
     * @param errorMessage
     */
    protected void onError(String errorMessage) {
        //progressDialog.dismiss();
        Log.e(TAG, "Error: " + errorMessage);
        Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
        finish();
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        cartProducts = getShopifyApplication().getCartItems();
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(cartProducts));
    }

    @Override
    public void onResume() {

        super.onResume();

        final Tracker tracker = getShopifyApplication().getDefaultTracker();
        if(tracker != null){

            tracker.setScreenName(getClass().getSimpleName());
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<CartLineItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<CartLineItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mTitleView.setText(mValues.get(position).getTitle());
            holder.mPriceView.setText(mValues.get(position).getPrice());
            holder.mQuantityView.setText(String.valueOf(mValues.get(position).getQuantity()));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTitleView;
            public final TextView mPriceView;
            public final TextView mQuantityView;
            public CartLineItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                //TODO FIX THIS IDS
                mTitleView = (TextView) view.findViewById(R.id.id);
                mPriceView = (TextView) view.findViewById(R.id.content);
                mQuantityView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mPriceView.getText() + "'";
            }
        }
    }
}
