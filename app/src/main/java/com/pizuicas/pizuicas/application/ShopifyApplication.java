package com.pizuicas.pizuicas.application;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.pizuicas.pizuicas.R;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.CartLineItem;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductVariant;
import com.shopify.buy.model.Shop;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Application class that maintains instances of BuyClient and Checkout for the lifetime of the app.
 */
public class ShopifyApplication extends Application {

    private final String TAG = ShopifyApplication.class.getName();

    Cart cart;
    /* Tracker for Google Analytics service */
    private Tracker mTracker;
    private BuyClient buyClient;
    private Checkout checkout;
    private Shop shop;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeBuyClient();
    }

    private void initializeBuyClient() {
        String shopUrl = getString(R.string.shop_url);
        if (TextUtils.isEmpty(shopUrl)) {
            throw new IllegalArgumentException("You must populate the 'shop_url' entry in strings.xml, in the form '<myshop>.myshopify.com'");
        }

        String shopifyApiKey = getString(R.string.shopify_api_key);
        if (TextUtils.isEmpty(shopifyApiKey)) {
            throw new IllegalArgumentException("You must populate the 'shopify_api_key' entry in strings.xml");
        }

        String channelId = getString(R.string.channel_id);
        if (TextUtils.isEmpty(channelId)) {
            throw new IllegalArgumentException("You must populate the 'channel_id' entry in the strings.xml");
        }

        String applicationName = getPackageName();

        /**
         * Create the BuyClient
         */
        buyClient = BuyClientFactory.getBuyClient(shopUrl, shopifyApiKey, channelId, applicationName);

        buyClient.getShop(new Callback<Shop>() {
            @Override
            public void success(Shop shop, Response response) {
                ShopifyApplication.this.shop = shop;
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ShopifyApplication.this, R.string.shop_error, Toast.LENGTH_LONG).show();
            }
        });

        /* Create cart */
        cart = new Cart();
    }

    public String getCurrency() {
        if (shop != null) {
            return shop.getCurrency();
        }
        else {
            return getResources().getString(R.string.default_currency);
        }
    }


    public void getAllProducts(final Callback<List<Product>> callback) {
        // For this sample app, "all" products will just be the first page of products
        buyClient.getProductPage(1, callback);
    }

    public void getProducts(String collectionId, Callback<List<Product>> callback) {
        // For this sample app, we'll just fetch the first page of products in the collection
        buyClient.getProducts(1, collectionId, callback);
    }

    /* There are no variants for this shop */
    public void addProductToCart(Product product){
        cart.addVariant(product.getVariants().get(0));
    }

    /**
     * Create a new checkout with the selected product. For convenience in the sample app we will hardcode the user's shipping address.
     * The shipping rates fetched in ShippingRateListActivity will be for this address.
     *
     * @param callback
     */
    public void createCheckout(final Callback<Checkout> callback) {

        Log.d(TAG, "createCheckout: Creates checkout with cart.");
        checkout = new Checkout(cart);
        buyClient.createCheckout(checkout, wrapCheckoutCallback(callback));
    }

    /**
     * Update a checkout with the buyer information.
     *
     * @param address
     * @param email
     * @param callback
     */
    public void updateCheckout(Address address, String email, final Callback<Checkout> callback) {

        checkout.setShippingAddress(address);

        checkout.setEmail(email);

        checkout.setWebReturnToUrl(getString(R.string.web_return_to_url));
        checkout.setWebReturnToLabel(getString(R.string.web_return_to_label));

        Log.d(TAG, "createCheckout: LLEGA CON EL CHECKOUT LLENO");

        buyClient.updateCheckout(checkout, wrapCheckoutCallback(callback));
    }

    public Checkout getCheckout() {
        return checkout;
    }

    public List<CartLineItem> getCartItems() {
        return cart.getLineItems();
    }

    public void getCheckoutCompletionStatus(final Callback<Boolean> callback) {
        buyClient.getCheckoutCompletionStatus(checkout, callback);
    }

    public void setProductQuantityCart(ProductVariant variant, int quantity) {
        cart.setVariantQuantity(variant, quantity);
    }

    public void removeProductFromCart(ProductVariant variant) {
        setProductQuantityCart(variant, 0);
    }

    /**
     * Wraps the callbacks that are provided by the activities so that the checkout ivar is always up to date.
     *
     * @param callback
     * @return
     */
    private Callback<Checkout> wrapCheckoutCallback(final Callback<Checkout> callback) {
        return new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                ShopifyApplication.this.checkout = checkout;
                callback.success(checkout, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        };
    }


    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

}