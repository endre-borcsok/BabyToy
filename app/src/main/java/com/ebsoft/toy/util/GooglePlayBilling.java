package com.ebsoft.toy.util;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

public class GooglePlayBilling implements PurchasesUpdatedListener {

    public interface GooglePlayBillingListener {
        void onGooglePlayBillingReady();
        void onError(String debugMessage);
    }

    public static final String SKU_ALL_BOARDS = "id_all_boards";

    private final GooglePlayBillingListener mGooglePlayBillingListener;
    private final BillingClient mBillingClient;
    private final Activity mActivity;

    private List<SkuDetails> mSkuDetailsList;

    public GooglePlayBilling(Activity activity) {
        mActivity = activity;
        mGooglePlayBillingListener = (GooglePlayBillingListener) activity;
        mBillingClient = BillingClient.newBuilder(activity)
                .enablePendingPurchases()
                .setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener() {

            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    findProducts();
                } else {
                    mGooglePlayBillingListener.onError(billingResult.getDebugMessage());
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                mGooglePlayBillingListener.onError("Billing disconnected");
            }
        });
    }

    private void findProducts() {
        List<String> skuList = new ArrayList<> ();
        skuList.add(SKU_ALL_BOARDS);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        mBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            mSkuDetailsList = skuDetailsList;
                            mGooglePlayBillingListener.onGooglePlayBillingReady();
                        } else {
                            mGooglePlayBillingListener.onError(billingResult.getDebugMessage());
                        }
                    }
                });
    }

    public String getPrice(String id) {
        if (mSkuDetailsList == null) return "Couldn't find product";

        for (SkuDetails sku : mSkuDetailsList) {
            if (sku.getSku().equals(id)) {
                return sku.getPrice();
            }
        }
        mGooglePlayBillingListener.onError("Couldn't find product");
        return "";
    }

    public void purchase(String id) {
        if (mSkuDetailsList == null) {
            mGooglePlayBillingListener.onError("Couldn't find product");
        }

        for (SkuDetails sku : mSkuDetailsList) {
            if (sku.getSku().equals(id)) {
                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(sku)
                        .build();
                mBillingClient.launchBillingFlow(mActivity, flowParams);
                return;
            }
        }
        mGooglePlayBillingListener.onError("Couldn't find product");
    }

    public boolean hasProductBeenPurchased(String id) {
        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);

        for (Purchase purchase : purchasesResult.getPurchasesList()) {
            if (purchase.getSku().equals(id)) {
                if (purchase.isAcknowledged() && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                    return true;
                }
            }
        }

        return false;
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            //TODO
                        } else {
                            mGooglePlayBillingListener.onError(billingResult.getDebugMessage());
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            // Handle any other error codes.
        }
    }
}
