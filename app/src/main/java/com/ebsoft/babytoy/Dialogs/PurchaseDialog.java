package com.ebsoft.babytoy.Dialogs;

import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.ebsoft.babytoy.Purchases;
import com.ebsoft.babytoy.R;
import com.ebsoft.babytoy.util.Purchase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * Created by Endre on 26/03/2017.
 */

public class PurchaseDialog extends Dialog {

    private Typeface mTypeFace = null;
    private TextView mQuestionText;
    private TextView mPriceText;
    private ImageView mAccept;
    private IInAppBillingService mService;
    private Purchases mPurchases;

    public static PurchaseDialog newInstance(String title, Runnable onDialogCompleted, IInAppBillingService service) {

        if (service == null) {
            return null;
        }

        PurchaseDialog frag = new PurchaseDialog();
        frag.addOnCompletedRunnable(onDialogCompleted);
        frag.addBillingService(service);
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void addBillingService(IInAppBillingService service) {
        this.mService = service;
        this.mPurchases = new Purchases(service);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.layout_purchase_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTypeFace = Typeface.createFromAsset(getDialog().getContext().getAssets(),"fonts/Chewy.ttf");

        mQuestionText = (TextView) view.findViewById(R.id.textQuestion);
        mPriceText = (TextView) view.findViewById(R.id.textPrice);
        mAccept = (ImageView) view.findViewById(R.id.tick);

        mQuestionText.setTypeface(mTypeFace);
        mPriceText.setTypeface(mTypeFace);

        mPriceText.setText(getResources().getText(R.string.layout_purchase_dialog_price_loading));

        mAccept.setVisibility(View.GONE);
        mAccept.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((ImageView)view).setImageResource(R.drawable.tick_red);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isVisible()) {
                            dismiss();
                        }

                        mOnDialogCompleted.run();
                    case MotionEvent.ACTION_CANCEL:
                        ((ImageView)view).setImageResource(R.drawable.tick_green);
                        break;
                }
                return true;
            }
        });

        Thread getPrice = new Thread() {
            @Override
            public void run() {
                try {
                    Bundle skuDetails = mPurchases.getSkuDetails(getActivity());
                    int response = skuDetails.getInt("RESPONSE_CODE");
                    if (response == 0) {
                        ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");

                        for (String thisResponse : responseList) {
                            JSONObject object = null;
                            try {
                                object = new JSONObject(thisResponse);
                                String sku = object.getString("productId");
                                final String price = object.getString("price");
                                if (sku.equals(Purchases.SKU_ALL_BOARDS)) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAccept.setVisibility(View.VISIBLE);
                                            mPriceText.setText(price);
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, e.toString());
                }
            }
        };
        getPrice.start();
    }
}
