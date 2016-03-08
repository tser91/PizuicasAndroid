package com.pizuicas.pizuicas;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pizuicas.pizuicas.application.ShopifyApplication;
import com.shopify.buy.model.Product;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String KEY_ITEM_JSON = "KEY_ITEM_JSON";
    private final String TAG = ItemDetailFragment.class.getName();
    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;
    /**
     * The item content this fragment is presenting.
     */
    private Product mItem;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    protected ShopifyApplication getShopifyApplication() {
        return (ShopifyApplication) getActivity().getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Log.d(TAG, "onCreate: arg item key is: " + getArguments().getString(ARG_ITEM_ID));

            mItem = Product.fromJson(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout =
                    (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitle());
            }
        }

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Add to cart");
                getShopifyApplication().addProductToCart(mItem);
                Toast.makeText(getContext(), getResources().getString(R.string.product_to_cart), Toast.LENGTH_SHORT).show();
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("AddCart")
                        .build());
            }
        });

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        mTracker = getShopifyApplication().getDefaultTracker();
        // [END shared_tracker]
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null
                && savedInstanceState.get(KEY_ITEM_JSON) != null){
            mItem = Product.fromJson(savedInstanceState.getString(KEY_ITEM_JSON));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the item content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.getBodyHtml());
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_ITEM_JSON, mItem.toJsonString());
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

}
