package com.ebsoft.babytoy;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;
import com.ebsoft.babytoy.util.Purchase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Endre on 08/04/2017.
 */

public class Purchases {

    public static final String SKU_ALL_BOARDS = "id_all_boards";
    private final String TAG = Purchase.class.getSimpleName();

    private Bundle mQuerySkus;
    private IInAppBillingService mService;

    public interface OnPurchaseEventListener {
        void onError(String error);
    }

    public Purchases(IInAppBillingService service) {
        this.mService = service;
        mQuerySkus = new Bundle();
        ArrayList<String> skuList = new ArrayList<String>();
        skuList.add(SKU_ALL_BOARDS);
        mQuerySkus.putStringArrayList("ITEM_ID_LIST", skuList);
    }

    /**
     * Call to check what's available for purchase.
     * @param activity the activity to call from
     * @return a list of available products
     * @throws RemoteException
     */
    public Bundle getSkuDetails(Activity activity) throws RemoteException {
        /*
        Warning: Don't call the getSkuDetails method on the main thread.
        Calling this method triggers a network request that could block your main thread.
        Instead, create a separate thread and call
        the getSkuDetails method from inside of that thread.
         */
        return mService.getSkuDetails(3, activity.getPackageName(), "inapp", mQuerySkus);
    }

    /**
     * Warning: Don't call this method on the main thread.
     * Calling this method triggers a network request that could block your main thread.
     * Instead, create a separate thread and call
     * the getSkuDetails method from inside of that thread.
     *
     * @param activity the Activity to interact with
     * @param selectedSku the selected sku
     * @return the price of the selected sku
     */
    public String getSkuPrice(Activity activity, String selectedSku, OnPurchaseEventListener onError) throws RemoteException {
        Bundle skuDetails = getSkuDetails(activity);
        int response = skuDetails.getInt("RESPONSE_CODE");
        if (response == 0) {
            ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");

            for (String thisResponse : responseList) {
                JSONObject object = null;
                try {
                    object = new JSONObject(thisResponse);
                    String sku = object.getString("productId");
                    final String price = object.getString("price");
                    if (sku.equals(selectedSku)) {
                        return price;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.toString());
                    if (onError != null) {
                        onError.onError(e.toString());
                    }
                }
            }
        } else {
            if (onError != null) {
                onError.onError(Integer.toString(response));
            }
        }

        return null;
    }

    /**
     * Call to check whether the you product has been purchased or not.
     * @param activity the activity to call from
     * @param product the product to check
     * @return
     * @throws RemoteException
     */
    public boolean hasProductPurchased(Activity activity, String product, OnPurchaseEventListener onError) throws RemoteException {

        if (product == null) {
            return false;
        }

        Bundle ownedItems = mService.getPurchases(3, activity.getPackageName(), "inapp", null);
        int response = ownedItems.getInt("RESPONSE_CODE");
        if (response == 0) {
            ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
            ArrayList<String>  purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            ArrayList<String>  signatureList = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
            String continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");

            for (int i = 0; i < purchaseDataList.size(); ++i) {
                String purchaseData = purchaseDataList.get(i);
                String signature = signatureList.get(i);
                String sku = ownedSkus.get(i);

                if (product.equals(sku)) {
                    return true;
                }
            }
            // if continuationToken != null, call getPurchases again
            // and pass in the token to retrieve more items
        } else {
            if (onError != null) {
                onError.onError(Integer.toString(response));
            }
        }
        return false;
    }

    /**
     * Use this to purchase products.
     * @param activity the activity to call form
     * @param sku the product to purchase
     * @throws RemoteException
     * @throws IntentSender.SendIntentException
     */
    public void purchase(Activity activity, String sku, OnPurchaseEventListener onError) throws RemoteException, IntentSender.SendIntentException {
        Bundle buyIntentBundle = mService.getBuyIntent(
                3,
                activity.getPackageName(),
                sku,
                "inapp",
                "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");

        int response = buyIntentBundle.getInt("RESPONSE_CODE");
        if (response == 0) {
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            activity.startIntentSenderForResult(
                    pendingIntent.getIntentSender(),
                    1001,
                    new Intent(),
                    Integer.valueOf(0),
                    Integer.valueOf(0),
                    Integer.valueOf(0));
        } else {
            if (onError != null) {
                onError.onError(Integer.toString(response));
            }
        }
    }
}
