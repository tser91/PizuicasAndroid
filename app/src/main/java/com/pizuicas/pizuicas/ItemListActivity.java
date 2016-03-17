package com.pizuicas.pizuicas;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
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
import com.pizuicas.pizuicas.provider.product.ProductColumns;
import com.pizuicas.pizuicas.provider.product.ProductContentValues;
import com.pizuicas.pizuicas.provider.product.ProductSelection;
import com.pizuicas.pizuicas.ui.DynamicHeightNetworkImageView;
import com.pizuicas.pizuicas.ui.ImageLoaderHelper;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.model.Product;

import java.net.MalformedURLException;
import java.nio.ByteBuffer;
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
    private final String PRODUCT_CLICKED = "PRODUCT_CLICKED";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private Product productClickedTwoPane;

    private List<Product> productsToShow;

    private Activity mContext;

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

        mContext = this;

        mTwoPane = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        productsToShow = new ArrayList<Product>();

        View recyclerView = findViewById(R.id.card_list);
        assert recyclerView != null;

        if (savedInstanceState != null && savedInstanceState.get(PRODUCT_LIST_KEY) != null) {
            ArrayList<String> savedProductList =
                    (ArrayList<String>) savedInstanceState.get(PRODUCT_LIST_KEY);
            for (int i = 0; i < savedProductList.size(); i++) {
                Log.d(TAG, "onCreate: " + Product.fromJson(savedProductList.get(i)).getTitle());
                productsToShow.add(Product.fromJson(savedProductList.get(i)));
                ((RecyclerView) recyclerView).setAdapter(new SimpleItemRecyclerViewAdapter(productsToShow));
            }

            if (savedInstanceState.get(PRODUCT_CLICKED) != null) {
                productClickedTwoPane = Product.fromJson(savedInstanceState.getString(PRODUCT_CLICKED));
            }
        }
        else {
            setupRecyclerView((RecyclerView) recyclerView);
        }

        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        ((RecyclerView) recyclerView).setLayoutManager(sglm);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_list_add);
        if (findViewById(R.id.card_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: Add to cart");
                    if (productClickedTwoPane != null) {
                        getShopifyApplication().addProductToCart(productClickedTwoPane);
                        Snackbar.make(view, getResources().getString(R.string.product_to_cart), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Action")
                                .setAction("AddCart")
                                .build());
                    }
                    else {
                        Snackbar.make(view, getResources().getString(R.string.no_product_selected), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
        }
        else {
            fab.setVisibility(View.GONE);
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
        if (productClickedTwoPane != null) {
            outState.putString(PRODUCT_CLICKED, productClickedTwoPane.toJsonString());
        }
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
                try {
                    onFetchedProducts(products, recyclerView);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e(TAG, "failure fetching products" );
                }
                Log.d(TAG, "success fetching products");
            }

            @Override
            public void failure(RetrofitError error) {
                onError(error);
                Log.d(TAG, "failure getting products from online store");
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
        supportFinishAfterTransition();;
    }

    private void onFetchedProducts(List<Product> products, RecyclerView recyclerView) throws MalformedURLException {
        Product tempProduct;
        ProductContentValues productValues;

        productsToShow = products;

        /* Store the products information into the DB using content provider */
        for (int index = 0; index < productsToShow.size(); index++) {
            tempProduct = productsToShow.get(index);

            new ProductImageRetriever() {
                @Override public void onPostExecute(Pair result)
                {
                    ProductSelection where = new ProductSelection();
                    where.shopifyid((String) result.first);

                    /* Resize Image */
                    byte[] imageBytes = (byte[])result.second;
                    Bitmap b = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    float ratio = b.getWidth()/b.getHeight();
                    int width = Math.round(getResources().getDimension(R.dimen.card_width));

                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, width, Math.round(width / ratio), false);

                    //calculate how many bytes our image consists of.
                    int bytes = scaledBitmap.getByteCount();

                    ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
                    scaledBitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

                    byte[] finalArray = buffer.array(); //Get the underlying array containing the data.

                    ProductContentValues productValues= new ProductContentValues();
                    productValues.putImage(finalArray);
                    getApplication().getContentResolver().update(ProductColumns.CONTENT_URI,
                            productValues.values(), where.sel(), where.args());
                }
            }.execute(tempProduct);

            productValues = new ProductContentValues();
            productValues.putJsonobject(tempProduct.toJsonString());
            productValues.putShopifyid(tempProduct.getProductId());
            Uri uri = productValues.insert(getContentResolver());
            ContentUris.parseId(uri);
        }

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(productsToShow));

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

            final String urlImage = mValues.get(position).getImages().get(0).getSrc();
            final String uriTitle = mValues.get(position).getTitle();
            final String uriDescription = mValues.get(position).getBodyHtml().replaceAll("<br>", "\n");

            holder.mItem = mValues.get(position);
            holder.mTitleView.setText(mValues.get(position).getTitle());
            holder.mPriceView.setText(getShopifyApplication().getCurrency() + " " +
                    mValues.get(position).getVariants().get(0).getPrice());
            holder.mImageView.setImageUrl(
                    urlImage,
                    ImageLoaderHelper.getInstance(ItemListActivity.this).getImageLoader());

            //TODO FIX THIS
            // holder.mImageView.setAspectRatio(ratio);


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoDetails(holder, v);
                }
            });

            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoDetails(holder, v);
                }
            });

            /**
             * Set content description for each card with its information.
             */
            String contentDescription =
                    mValues.get(position).getTitle() +
                            " " +
                            getResources().getString(R.string.line_item_price) +
                            " is " +
                            getShopifyApplication().getCurrency() +
                            " " +
                            mValues.get(position).getVariants().get(0).getPrice() +
                            " " +
                            mValues.get(position).getBodyHtml();

            holder.mView.setContentDescription(contentDescription);
        }

        private void gotoDetails(ViewHolder holder, View view) {
            if (mTwoPane) {
                productClickedTwoPane = holder.mItem;

                Bundle arguments = new Bundle();
                arguments.putString(
                        ItemDetailFragment.ARG_ITEM_ID, holder.mItem.toJsonString());
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.card_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(
                        ItemDetailFragment.ARG_ITEM_ID, holder.mItem.toJsonString());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(mContext,
                                (View) holder.mImageView,
                                getResources().getString(R.string.image_transition));
                startActivity(intent, options.toBundle());
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
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
