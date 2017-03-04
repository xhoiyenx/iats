package com.hoiyen.iats.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.hoiyen.iats.R;
import com.hoiyen.iats.adapter.CartItemListAdapter;
import com.hoiyen.iats.library.ApiRequest;
import com.hoiyen.iats.models.CartModel;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.scancard.ScanCard;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.cartList)
    RecyclerView cartList;

    @BindView(R.id.submit_button)
    Button submit;

    @BindView(R.id.total_amount)
    TextView totalText;

    @BindView(R.id.loader)
    ProgressBar loader;

    private final static String TAG = "Cart";
    private CartItemListAdapter adapter;
    private String invoice;
    private double amount;
    private List<CartModel> cartModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        setActionBar(toolbar);
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.text_my_orders);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        adapter = new CartItemListAdapter(CartActivity.this);
        submit.setOnClickListener(CartActivity.this);

        // Submit payment
        SdkUIFlowBuilder.init(CartActivity.this, getString(R.string.client_key), getString(R.string.api_checkout), new TransactionFinishedCallback() {
            @Override
            public void onTransactionFinished(TransactionResult transactionResult) {

            }
        }).setExternalScanner(new ScanCard()).buildSDK();

    }

    @Override
    protected void onStart() {
        super.onStart();

        cartList.setLayoutManager(new LinearLayoutManager(this));
        cartList.setHasFixedSize(true);
        cartList.setAdapter(adapter);

        ApiRequest.SendRequest("http://api.indonesiatrimmer.com/cart", new ApiRequest.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loader.setVisibility(View.GONE);

                JSONArray cart = response.optJSONArray("cart");
                if (cart != null && cart.length() > 0) {
                    cartModels = CartModel.parseJSON(cart);
                    adapter.putDataset(cartModels);

                }

                totalText.setText("Rp. " + response.optString("formatted") + ",-");
                invoice = response.optString("invoice");
                amount = response.optDouble("total");
            }

            @Override
            public void onErrorResponse(String response) {
                Log.e(TAG, response);
            }
        });
    }

    @Override
    public void onClick(View v) {
        createTransaction();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(CartActivity.this, ShopActivity.class));
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CartActivity.this, ShopActivity.class));
        finish();
    }

    private void createTransaction() {

        UserDetail userDetail = new UserDetail();
        userDetail.setEmail(Prefs.getString("usermail", ""));
        LocalDataHandler.saveObject("user_details", userDetail);

        // Init transaction request
        final TransactionRequest request = new TransactionRequest(invoice, amount);

        // Populate items
        ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
        if (cartModels != null) {
            for (CartModel cart : cartModels) {
                itemDetailsList.add(new ItemDetails(
                        cart.id,
                        cart.price, // Price
                        1, // Qty
                        cart.getName()
                ));
            }
        }

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
        MidtransSDK.getInstance().startPaymentUiFlow(CartActivity.this);
    }
}
