package com.hoiyen.iats.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hoiyen.iats.BuildConfig;
import com.hoiyen.iats.R;
import com.hoiyen.iats.utils.IATS;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubscribeActivity extends Activity {

    private String invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        final Button subscribe = (Button) findViewById(R.id.subscribe_button);
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSubscription();
            }
        });

        final String clientKey = BuildConfig.CLIENT_KEY;
        final String clientUrl = BuildConfig.BASE_URL;

        // Submit payment
        SdkUIFlowBuilder.init(SubscribeActivity.this, clientKey, clientUrl, new TransactionFinishedCallback() {
            @Override
            public void onTransactionFinished(TransactionResult transactionResult) {

                if (transactionResult.getStatus().equals("success")) {
                    TransactionResponse response = transactionResult.getResponse();
                    Toast.makeText(SubscribeActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SubscribeActivity.this, BlogActivity.class));
                }

            }
        }).useBuiltInTokenStorage(false).buildSDK();
    }

    /**
     * Send request to server that we want to create payment for subscription
     */
    private void requestSubscription() {

        final String url = getString(R.string.api_subscription_request);

        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (invoice != null) {
                    createTransaction();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer ".concat(Prefs.getString("token", "")));
                return headers;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Response<JSONObject> jsonObjectResponse = super.parseNetworkResponse(response);
                if (jsonObjectResponse.isSuccess()) {
                    invoice = jsonObjectResponse.result.optString("invoice");
                }
                return jsonObjectResponse;
            }
        };

        IATS.getInstance().addToRequestQueue(request);

    }

    private void createTransaction() {

        // Init transaction request
        final TransactionRequest request = new TransactionRequest(invoice, Double.valueOf("2500000"));

        // Populate items
        ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
        itemDetailsList.add(new ItemDetails(
                "1",
                Integer.valueOf("2500000"), // Price
                1, // Qty
                "IATS Monthly Membership Fee"
        ));


        request.setItemDetails(itemDetailsList);

        // Credit Card information
        CreditCard creditCardOptions = new CreditCard();
        // Set to true if you want to save card to Snap
        creditCardOptions.setSaveCard(false);
        // Set to true to save card token as `one click` token
        creditCardOptions.setSecure(false);
        // Set acquiring bank (Optional)
        creditCardOptions.setBank(BankType.BCA);
        // Set MIGS channel (ONLY for BCA and Maybank Acquiring bank)
        creditCardOptions.setChannel(CreditCard.MIGS);
        // Set Credit Card Options
        request.setCreditCard(creditCardOptions);
        // Set card payment info
        request.setCardPaymentInfo("normal", true);

        MidtransSDK.getInstance().setTransactionRequest(request);
        MidtransSDK.getInstance().startPaymentUiFlow(SubscribeActivity.this);
    }
}
