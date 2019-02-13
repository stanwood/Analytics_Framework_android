package io.stanwood.framework.analytics.firebase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Map;

import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingEvent;
import io.stanwood.framework.analytics.generic.TrackingKey;

public class DefaultMapFunction implements MapFunction {
    @Nullable
    @Override
    public Bundle map(TrackerParams params) {
        if (params.getEventName().equalsIgnoreCase(TrackingEvent.PURCHASE)) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.VALUE, params.getCustomPropertys().get(TrackingKey.PURCHASE_PRICE).toString());
            bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, params.getCustomPropertys().get(TrackingKey.PURCHASE_ORDERID).toString());
            return bundle;
        }
        Bundle bundle = null;
        if (!TextUtils.isEmpty(params.getItemId())) {
            bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, params.getItemId());
        }
        if (!TextUtils.isEmpty(params.getCategory())) {
            bundle = bundle != null ? bundle : new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, params.getCategory());
        }
        if (!TextUtils.isEmpty(params.getContentType())) {
            bundle = bundle != null ? bundle : new Bundle();
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, params.getContentType());
        }
        if (!TextUtils.isEmpty(params.getName())) {
            bundle = bundle != null ? bundle : new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, params.getName());
        }
        return bundle;
    }

    @Nullable
    @Override
    public Map<String, Object> mapKeys(TrackerParams params) {
        return params.getCustomPropertys();
    }
}