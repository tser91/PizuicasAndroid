package com.pizuicas.pizuicas;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pizuicas.pizuicas.application.ShopifyApplication;
import com.pizuicas.pizuicas.provider.product.ProductColumns;
import com.pizuicas.pizuicas.provider.product.ProductCursor;
import com.pizuicas.pizuicas.provider.product.ProductSelection;
import com.pizuicas.pizuicas.ui.DynamicHeightNetworkImageView;
import com.pizuicas.pizuicas.ui.ImageLoaderHelper;
import com.pizuicas.pizuicas.ui.SimpleDividerItemDecoration;
import com.shopify.buy.model.CartLineItem;
import com.shopify.buy.model.Product;

import java.util.List;

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
        toolbar.setTitle(getResources().getString(R.string.cart));
        setSupportActionBar(toolbar);

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCheckout();
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
    private void gotoCheckout() {
        //showLoadingDialog(R.string.syncing_data);

        Intent intent = new Intent(getApplicationContext(), CheckoutInformationActivity.class);
        startActivity(intent);
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        cartProducts = getShopifyApplication().getCartItems();
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(cartProducts));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

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
            holder.mTitleView.setText(mValues.get(position).getVariant().getProductTitle());
            holder.mPriceView.setText(mValues.get(position).getPrice());
            holder.mPriceView.setText(
                    String.valueOf((float) (Float.valueOf(mValues.get(position).getPrice()) *
                            mValues.get(position).getQuantity())));
            holder.mQuantityView.setText(String.valueOf(mValues.get(position).getQuantity()));

            String[] projection = { ProductColumns.JSONOBJECT};
            ProductSelection where = new ProductSelection();
            where.shopifyid(String.valueOf(mValues.get(position).getVariant().getProductId()));
            Cursor standardCursor = getApplicationContext().getContentResolver().query(
                    ProductColumns.CONTENT_URI, projection,
                    where.sel(), where.args(), null);
            ProductCursor productCursor = new ProductCursor(standardCursor);

            productCursor.moveToNext();

            Product tempProduct = Product.fromJson(productCursor.getJsonobject());

            standardCursor.close();
            productCursor.close();

            holder.mImageView.setImageUrl(
                    tempProduct.getImages().get(0).getSrc(),
                    ImageLoaderHelper.getInstance(CartActivity.this).getImageLoader());
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
            public final DynamicHeightNetworkImageView mImageView;
            public CartLineItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleView = (TextView) view.findViewById(R.id.cart_item_title);
                mPriceView = (TextView) view.findViewById(R.id.cart_item_price);
                mQuantityView = (TextView) view.findViewById(R.id.cart_item_quantity);
                mImageView = (DynamicHeightNetworkImageView) view.findViewById(R.id.imageView_cart_image);
            }
        }
    }
}
