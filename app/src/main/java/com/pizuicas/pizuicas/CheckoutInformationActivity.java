package com.pizuicas.pizuicas;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.pizuicas.pizuicas.application.ShopifyApplication;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Checkout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CheckoutInformationActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 161; // Magic number
    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String TAG = CheckoutInformationActivity.class.getName();

    private TextInputLayout inputLayoutName, inputLayoutLastName, inputLayoutEmail,
            inputLayoutAddress, inputLayoutCity, inputLayoutProvince, inputLayoutZip,
            inputLayoutCountryCode;

    private EditText inputName, inputLastName, inputEmail, inputAddress, inputCity, inputProvince,
            inputZip, inputCountryCode;

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    protected ShopifyApplication getShopifyApplication() {
        return (ShopifyApplication) getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.checkout));
        setSupportActionBar(toolbar);

        Button btnCheckout = (Button) findViewById(R.id.btn_checkout);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitForm()) {
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("GoToCheckoutWeb")
                            .build());
                    setCheckoutInfo();
                }
            }
        });

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        initializeCheckout();

        inputLayoutName = (TextInputLayout) findViewById(R.id.inputLayout_name);
        inputLayoutLastName = (TextInputLayout) findViewById(R.id.inputLayout_last_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.inputLayout_email);
        inputLayoutAddress = (TextInputLayout) findViewById(R.id.inputLayout_address);
        inputLayoutCity = (TextInputLayout) findViewById(R.id.inputLayout_city);
        inputLayoutProvince = (TextInputLayout) findViewById(R.id.inputLayout_province);
        inputLayoutZip = (TextInputLayout) findViewById(R.id.inputLayout_zip);
        inputLayoutCountryCode = (TextInputLayout) findViewById(R.id.inputLayout_country);

        inputName = (EditText) findViewById(R.id.form_name);
        inputLastName = (EditText) findViewById(R.id.form_last_name);
        inputEmail = (EditText) findViewById(R.id.form_email);
        inputAddress = (EditText) findViewById(R.id.form_address);
        inputCity = (EditText) findViewById(R.id.form_city);
        inputProvince = (EditText) findViewById(R.id.form_province);
        inputZip = (EditText) findViewById(R.id.form_zip);
        inputCountryCode = (EditText) findViewById(R.id.form_country);


        inputName.addTextChangedListener(new PisuicasTextWatcher(inputName));
        inputLastName.addTextChangedListener(new PisuicasTextWatcher(inputLastName));
        inputEmail.addTextChangedListener(new PisuicasTextWatcher(inputEmail));
        inputAddress.addTextChangedListener(new PisuicasTextWatcher(inputAddress));
        inputCity.addTextChangedListener(new PisuicasTextWatcher(inputCity));
        inputProvince.addTextChangedListener(new PisuicasTextWatcher(inputProvince));
        inputZip.addTextChangedListener(new PisuicasTextWatcher(inputZip));
        inputCountryCode.addTextChangedListener(new PisuicasTextWatcher(inputCountryCode));

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        mTracker = getShopifyApplication().getDefaultTracker();
        // [END shared_tracker]
    }

    private void getInfoFromLocation(double latitude, double longitude) {

        Geocoder geocoder;
        List<android.location.Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "getInfoFromLocation: addresses size is " + addresses.size());

        if (addresses != null && addresses.size() > 0) {
            //android.location.Address tempAddress = addresses.get(0);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();

            inputAddress.setText(address);
            inputCity.setText(city);
            inputCountryCode.setText(country);
            inputProvince.setText(state);
            inputZip.setText(postalCode);
        }
    }

    private void initializeCheckout() {

        Callback<Checkout> callback = new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                Log.d(TAG, "success: checkout created");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failure: could not create checkout");
                onError(error);
            }
        };

        getShopifyApplication().createCheckout(callback);
    }

    private void updateCheckout(Address address, String email) {

        Callback<Checkout> callback = new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                Log.d(TAG, "success: checkout updated");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("GoToCheckoutWeb")
                        .build());

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getShopifyApplication().getCheckout().getWebUrl()));
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failure: checkout could not be updated");
                onError(error);
            }
        };

        getShopifyApplication().updateCheckout(address, email, callback);
    }

    private void setCheckoutInfo() {

        Address address = new Address();
        address.setAddress1(inputAddress.getText().toString());
        address.setCity(inputCity.getText().toString());
        address.setCountryCode(inputCountryCode.getText().toString());
        address.setFirstName(inputName.getText().toString());
        address.setLastName(inputLastName.getText().toString());
        address.setProvince(inputProvince.getText().toString());
        address.setZip(inputZip.getText().toString());

        updateCheckout(address, inputEmail.getText().toString());
    }

    /**
     * Validating form
     */
    private boolean submitForm() {
        if (!validateName()
                || !validateLastName()
                || !validateEmail()
                || !validateAddress()
                || !validateCity()
                || !validateProvince()
                || !validateCountryCode()
                || !validateZip()) {
            return false;
        }
        Log.d(TAG, "submitForm: Validated.");

        return true;
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLastName() {
        if (inputLastName.getText().toString().trim().isEmpty()) {
            inputLayoutLastName.setError(getString(R.string.err_msg_last_name));
            requestFocus(inputLastName);
            return false;
        } else {
            inputLayoutLastName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAddress() {
        if (inputAddress.getText().toString().trim().isEmpty()) {
            inputLayoutAddress.setError(getString(R.string.err_msg_address));
            requestFocus(inputAddress);
            return false;
        } else {
            inputLayoutAddress.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateCity() {
        if (inputCity.getText().toString().trim().isEmpty()) {
            inputLayoutCity.setError(getString(R.string.err_msg_city));
            requestFocus(inputCity);
            return false;
        } else {
            inputLayoutCity.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateProvince() {
        if (inputProvince.getText().toString().trim().isEmpty()) {
            inputLayoutProvince.setError(getString(R.string.err_msg_province));
            requestFocus(inputProvince);
            return false;
        } else {
            inputLayoutProvince.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateZip() {
        if (inputZip.getText().toString().trim().isEmpty()) {
            inputLayoutZip.setError(getString(R.string.err_msg_zip));
            requestFocus(inputZip);
            return false;
        } else {
            inputLayoutZip.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateCountryCode() {
        if (inputCountryCode.getText().toString().trim().isEmpty() ) {
            //|| !inputCountryCode.getText().toString().equals(
            //      getResources().getString(R.string.costa_rica_code)) ) {
            inputLayoutCountryCode.setError(getString(R.string.err_msg_country));
            requestFocus(inputCountryCode);
            return false;
        } else {
            inputLayoutCountryCode.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onConnected: looking for a new location");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            Log.d(TAG, "onConnected: permission not granted yet");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        getInfoFromLocation(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: ");
        handleNewLocation(location);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: permission was granted");
                    // permission was granted
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: permission was not granted");
                }
                return;
            }
        }
    }

    private class PisuicasTextWatcher implements TextWatcher {

        private View view;

        private PisuicasTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.form_name:
                    validateName();
                    break;
                case R.id.form_last_name:
                    validateLastName();
                    break;
                case R.id.form_email:
                    validateEmail();
                    break;
                case R.id.form_address:
                    validateAddress();
                    break;
                case R.id.form_city:
                    validateCity();
                    break;
                case R.id.form_province:
                    validateProvince();
                    break;
                case R.id.form_zip:
                    validateZip();
                    break;
                case R.id.form_country:
                    validateCountryCode();
                    break;
            }
        }
    }
}
