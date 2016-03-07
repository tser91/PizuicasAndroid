package com.pizuicas.pizuicas;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pizuicas.pizuicas.provider.product.ProductColumns;
import com.pizuicas.pizuicas.provider.product.ProductCursor;
import com.pizuicas.pizuicas.provider.product.ProductSelection;

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
    private final String TAG = ItemDetailFragment.class.getName();
    /**
     * The item content this fragment is presenting.
     */
    private DetailProduct mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {

        mItem = new DetailProduct();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Log.d(TAG, "onCreate: arg item key is: "+getArguments().getString(ARG_ITEM_ID));

            /* The details of the product clicked will be shown, use shopify's product id */
            ProductSelection where = new ProductSelection();
            where.shopifyId(getArguments().getString(ARG_ITEM_ID));

            /* The details projection of the product */
            String[] projection = {
                    ProductColumns.SHOPIFY_ID,
                    ProductColumns.TITLE,
                    ProductColumns.DESCRIPTION,
                    ProductColumns.IMAGE,
                    ProductColumns.PRICE};

            /* A cursor where the URI, projection and query arguments are set */
            Cursor cursor = getContext().getContentResolver().query(ProductColumns.CONTENT_URI, projection,
                    where.sel(), where.args(), null);

            ProductCursor productCursor = new ProductCursor(cursor);

            /* First position of the cursor is the header */
            productCursor.moveToNext();

            /* Get Item's details using the content provider */
            mItem.setTitle(productCursor.getTitle());
            mItem.setDescription(productCursor.getDescription());
            mItem.setPrice(productCursor.getPrice());
            mItem.setImage(productCursor.getImage());

            /* Close the cursors used to query*/
            productCursor.close();
            cursor.close();

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the item content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.getDescription());
        }

        return rootView;
    }

    private class DetailProduct {

        private String title;
        private String description;
        private Double price;
        private byte[] image;

        public DetailProduct(byte[] image, String title, String description, Double price ) {
            this.title = title;
            this.image = image;
            this.description = description;
            this.price = price;
        }

        public DetailProduct() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public byte[] getImage() {
            return image;
        }

        public void setImage(byte[] image) {
            this.image = image;
        }
    }
}
