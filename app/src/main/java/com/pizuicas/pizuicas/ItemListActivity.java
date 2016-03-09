package com.pizuicas.pizuicas;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pizuicas.pizuicas.application.ShopifyApplication;
import com.pizuicas.pizuicas.provider.product.ProductContentValues;
import com.pizuicas.pizuicas.ui.DynamicHeightNetworkImageView;
import com.pizuicas.pizuicas.ui.ImageLoaderHelper;
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
    private final String PRODUCT_LIST_KEY = "PRODUCT_LIST_KEY";

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

        View recyclerView = findViewById(R.id.card_list);
        assert recyclerView != null;

        if (savedInstanceState != null
                && savedInstanceState.get(PRODUCT_LIST_KEY) != null) {
            ArrayList<String> savedProductList =
                    (ArrayList<String>) savedInstanceState.get(PRODUCT_LIST_KEY);
            for (int i = 0; i < savedProductList.size(); i++) {
                productsToShow.add(Product.fromJson(savedProductList.get(i)));
            }
            ((RecyclerView) recyclerView).setAdapter(new SimpleItemRecyclerViewAdapter(productsToShow));
        }
        else {
            setupRecyclerView((RecyclerView) recyclerView);
        }

        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        ((RecyclerView) recyclerView).setLayoutManager(sglm);

        if (findViewById(R.id.card_detail_container) != null) {
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.main_menu_goto_cart:
                gotoCart();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> listProducts = new ArrayList<String>();;
        for (int index = 0; index < productsToShow.size(); index++) {
            listProducts.add(productsToShow.get(index).toJsonString());
        }
        outState.putStringArrayList(PRODUCT_LIST_KEY, listProducts);
    }

    private void gotoCart() {
        Log.d(TAG, "gotoCart: ");
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("GoToCart")
                .build());
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        startActivity(intent);
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
     * When we encounter an error with one of our network calls,
     * we abort and return to the previous activity.
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
                    .putImage(tempProduct.getImage(
                            tempProduct.getVariants().get(0)).getSrc().getBytes())
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
                    .inflate(R.layout.card_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mTitleView.setText(mValues.get(position).getTitle());
            holder.mPriceView.setText(mValues.get(position).getVariants().get(0).getPrice());
            holder.mImageView.setImageUrl(
                    mValues.get(position).getImages().get(0).getSrc(),
                     ImageLoaderHelper.getInstance(ItemListActivity.this).getImageLoader());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(
                                ItemDetailFragment.ARG_ITEM_ID, holder.mItem.toJsonString());
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(
                                ItemDetailFragment.ARG_ITEM_ID, holder.mItem.toJsonString());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        private int dpToPx(int dp)
        {
            float density = getResources().getDisplayMetrics().density;
            return Math.round((float) dp * density);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTitleView;
            public final TextView mPriceView;
            public final DynamicHeightNetworkImageView mImageView;
            public Product mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleView = (TextView) view.findViewById(R.id.item_title);
                mPriceView = (TextView) view.findViewById(R.id.item_price);
                mImageView = (DynamicHeightNetworkImageView) view.findViewById(R.id.thumbnail);
            }
        }
    }
}
