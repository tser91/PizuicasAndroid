package com.pizuicas.pizuicas;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    public TextView mTotalQuantityView;
    private int appBarHeight;

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
        final FloatingActionButton fab;
        View recyclerView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.cart));
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        fab = (FloatingActionButton) findViewById(R.id.fab);
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

        mTotalQuantityView = (TextView) findViewById(R.id.textView_cart_total_quantity);
        mTotalQuantityView.setText(getShopifyApplication().getCurrency() + " " + getTotal());

        fab.post(new Runnable()
        {
            public void run()
            {
                appBarHeight = (int) (getResources().getDimension(R.dimen.fab_margin)*2 +
                        fab.getHeight());

                View contentLayout = findViewById(R.id.content_cart_root ); // root View id from that link

                LinearLayout linearLayout = (LinearLayout) contentLayout.findViewById(R.id.action_bar_total_cart);

                // Gets the layout params that will allow you to resize the layout
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
                // Changes the height and width to the specified *pixels*
                params.height = appBarHeight;
                linearLayout.setLayoutParams(params);

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.item_list);
                ViewGroup.MarginLayoutParams recycParams = (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
                recycParams.bottomMargin = appBarHeight;
                recyclerView.setLayoutParams(recycParams);
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

    private float getTotal() {
        float total = 0;
        for (int index = 0; index < cartProducts.size(); index++) {
            total += Float.valueOf(cartProducts.get(index).getPrice()) *
                    cartProducts.get(index).getQuantity();
        }
        Log.d(TAG, "getTotal: " + total);
        return total;
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
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);

            String itemTitle = mValues.get(position).getVariant().getProductTitle();
            holder.mTitleView.setText(itemTitle);
            holder.mTitleView.setContentDescription(itemTitle);

            setItemPrice(holder.mPriceView, position);

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
            //TODO Fix This
            //holder.mImageView.setAspectRatio((float) 0.8);

            holder.mNumberPicker.setValue((int) mValues.get(position).getQuantity(), false);
            holder.mNumberPicker.setContentDescription(String.valueOf(mValues.get(position).getQuantity()));

            holder.mNumberPicker.setOnValueChangeListener(new OnValueChangeListener() {
                @Override
                public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                    cartProducts.get(position).setQuantity(newValue);
                    getShopifyApplication().setProductQuantityCart(
                            mValues.get(position).getVariant(), newValue);
                    setItemPrice(holder.mPriceView, position);
                    holder.mNumberPicker.setContentDescription(
                            String.valueOf(newValue));
                    setTotalItemPrice();
                    return true;
                }
            });

            holder.mDeleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getShopifyApplication().removeProductFromCart(
                            mValues.get(position).getVariant());
                    setTotalItemPrice();
                    notifyDataSetChanged();
                    Log.d(TAG, "onClick: removed position " + position);
                }
            });


        }

        private void setTotalItemPrice() {
            String totalPrice = getShopifyApplication().getCurrency() + " " + getTotal();
            mTotalQuantityView.setText(totalPrice);
            mTotalQuantityView.setContentDescription(totalPrice);
        }

        private void setItemPrice(TextView mPriceView, int position) {
            String price = getShopifyApplication().getCurrency() + " " +
                    String.valueOf((float) (Float.valueOf(mValues.get(position).getPrice()) *
                            mValues.get(position).getQuantity()));
            mPriceView.setText(price);
            mPriceView.setContentDescription(price);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTitleView;
            public final TextView mPriceView;
            public final TextView mDeleteView;
            public final DynamicHeightNetworkImageView mImageView;
            public final SwipeNumberPicker mNumberPicker;
            public CartLineItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleView = (TextView) view.findViewById(R.id.cart_item_title);
                mPriceView = (TextView) view.findViewById(R.id.cart_item_price);
                mImageView = (DynamicHeightNetworkImageView) view.findViewById(R.id.imageView_cart_image);
                mNumberPicker = (SwipeNumberPicker) view.findViewById(R.id.number_picker);
                mDeleteView = (TextView) view.findViewById(R.id.cart_item_delete);
            }
        }
    }
}
