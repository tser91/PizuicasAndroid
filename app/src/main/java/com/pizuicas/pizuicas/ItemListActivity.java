package com.pizuicas.pizuicas;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.pizuicas.pizuicas.provider.product.ProductContentValues;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    private final String TAG = ItemListActivity.class.getName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private List<Product> productsToShow;

    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    protected ShopifyApplication getShopifyApplication() {
        return (ShopifyApplication) getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        productsToShow = new ArrayList<Product>();

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        mTracker = getShopifyApplication().getDefaultTracker();
        // [END shared_tracker]
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {

        Callback<List<Product>> callback = new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                //dismissLoadingDialog();
                onFetchedProducts(products, recyclerView);
                Log.d(TAG, "success fetching products");
            }

            @Override
            public void failure(RetrofitError error) {
                onError(error);
                Log.d(TAG, "failure fetching products");
            }
        };

        getShopifyApplication().getAllProducts(callback);

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

    private void onFetchedProducts(List<Product> products, RecyclerView recyclerView) {
        Product tempProduct;
        ProductContentValues productValues;

        productsToShow = products;
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(productsToShow));

        /* Store the products information into the DB using content provider */
        for (int index = 0; index < productsToShow.size(); index++) {
            tempProduct = productsToShow.get(index);

            productValues = new ProductContentValues();
            productValues.putTitle(tempProduct.getTitle())
                    .putDescription(tempProduct.getBodyHtml())
                    .putImage(tempProduct.getImage(tempProduct.getVariants().get(0)).getSrc().getBytes())
                    .putShopifyId(tempProduct.getProductId())
                    .putPrice(Double.valueOf(tempProduct.getVariants().get(0).getPrice()));
            Uri uri = productValues.insert(getContentResolver());
            ContentUris.parseId(uri);
        }

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

        private final List<Product> mValues;

        public SimpleItemRecyclerViewAdapter(List<Product> items) {
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
            holder.mIdView.setText(mValues.get(position).getTitle());
            holder.mContentView.setText(mValues.get(position).getBodyHtml());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.toJsonString());
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.toJsonString());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Product mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
