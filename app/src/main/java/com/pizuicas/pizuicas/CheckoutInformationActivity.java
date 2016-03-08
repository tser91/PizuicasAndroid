package com.pizuicas.pizuicas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pizuicas.pizuicas.application.ShopifyApplication;

public class CheckoutInformationActivity extends AppCompatActivity {

    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

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
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitForm()) {
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("GoToCheckoutWeb")
                            .build());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(getShopifyApplication().getCheckout().getWebUrl()));
                    startActivity(intent);
                }
            }
        });

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
        if (inputCountryCode.getText().toString().trim().isEmpty()) {
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
