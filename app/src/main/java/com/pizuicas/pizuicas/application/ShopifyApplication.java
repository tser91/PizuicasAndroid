package com.pizuicas.pizuicas.application;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.pizuicas.pizuicas.R;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Shop;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Application class that maintains instances of BuyClient and Checkout for the lifetime of the app.
 */
public class ShopifyApplication extends Application {

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
     * @param product
     * @param callback
     */
    public void createCheckout(final Product product, final Callback<Checkout> callback) {

        cart.addVariant(product.getVariants().get(0));

        checkout = new Checkout(cart);

        Address address = new Address();
        address.setFirstName("Dinosaur");
        address.setLastName("Banana");
        address.setAddress1("421 8th Ave");
        address.setCity("New York");
        address.setProvince("NY");
        address.setZip("10001");
        address.setCountryCode("US");
        checkout.setShippingAddress(address);

        checkout.setEmail("something@somehost.com");

        checkout.setWebReturnToUrl(getString(R.string.web_return_to_url));
        checkout.setWebReturnToLabel(getString(R.string.web_return_to_label));

        buyClient.createCheckout(checkout, wrapCheckoutCallback(callback));
    }

    public Checkout getCheckout() {
        return checkout;
    }

    public void getCheckoutCompletionStatus(final Callback<Boolean> callback) {
        buyClient.getCheckoutCompletionStatus(checkout, callback);
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