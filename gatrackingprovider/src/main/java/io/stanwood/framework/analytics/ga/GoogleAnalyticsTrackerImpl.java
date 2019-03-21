package io.stanwood.framework.analytics.ga;


import android.annotation.SuppressLint;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import android.text.TextUtils;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;

import java.util.Map;

import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingEvent;
import io.stanwood.framework.analytics.generic.TrackingKey;

public class GoogleAnalyticsTrackerImpl extends GoogleAnalyticsTracker {
    private com.google.android.gms.analytics.Tracker tracker;

    protected GoogleAnalyticsTrackerImpl(Builder builder) {
        super(builder);
    }

    @RequiresPermission(
            allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"}
    )
    public static Builder builder(Application context, String appKey) {
        return new Builder(context, appKey);
    }

    @Override
    @SuppressLint("MissingPermission")
    protected void enable(boolean enabled) {
        if (enabled && tracker == null) {
            tracker = GoogleAnalytics.getInstance(context).newTracker(appKey);
            tracker.enableExceptionReporting(exceptionTrackingEnabled);
            tracker.setSampleRate(sampleRate);
            tracker.enableAutoActivityTracking(activityTracking);
            tracker.enableAdvertisingIdCollection(adIdCollection);
            tracker.setAnonymizeIp(anonymizeIp);
        }
        GoogleAnalytics.getInstance(context).setAppOptOut(!enabled);
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        TrackerParams mapped = mapFunc.mapParams(params);
        HitBuilders.HitBuilder<?> builder = null;
        if (mapped != null) {
            if (TrackingEvent.SCREEN_VIEW.equalsIgnoreCase(mapped.getEventName())) {
                tracker.setScreenName(mapped.getName());
                builder = new HitBuilders.ScreenViewBuilder();
            } else if (TrackingEvent.PURCHASE.equalsIgnoreCase(mapped.getEventName())) {
                trackPurchase(mapped);
            } else {
                HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder().setCategory(mapped.getEventName());
                if (!TextUtils.isEmpty(mapped.getName())) {
                    eventBuilder.setAction(mapped.getName());
                    if (!TextUtils.isEmpty(mapped.getItemId())) {
                        eventBuilder.setLabel(mapped.getItemId());
                    }
                }
                builder = eventBuilder;
            }
        }
        Map<Integer, Object> mappedKeys = mapFunc.mapKeys(params);
        if (mappedKeys != null) {
            if (builder == null) {
                builder = new HitBuilders.EventBuilder();
            }
            for (Map.Entry<Integer, Object> entry : mappedKeys.entrySet()) {
                builder.setCustomDimension(entry.getKey(), (String) entry.getValue());
            }
        }
        if (builder != null) {
            tracker.send(builder.build());
        }
    }

    private void trackPurchase(TrackerParams params) {
        Product product = new Product()
                .setId(params.getItemId())
                .setName(params.getName())
                .setCategory(params.getCategory())
                .setBrand(params.getCustomPropertys().get(TrackingKey.PURCHASE_BRAND).toString())
                .setPrice((Double) params.getCustomPropertys().get(TrackingKey.PURCHASE_PRICE))
                .setQuantity((Integer) params.getCustomPropertys().get(TrackingKey.PURCHASE_QUANTITY));
        ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
                .setTransactionId(params.getCustomPropertys().get(TrackingKey.PURCHASE_ORDERID).toString())
                .setTransactionAffiliation("Google Play Store")
                .setTransactionRevenue((Double) params.getCustomPropertys().get(TrackingKey.PURCHASE_PRICE));
        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                .addProduct(product)
                .setProductAction(productAction);
        tracker.setScreenName("transaction");
        tracker.send(builder.build());
    }

    @Override
    public void track(@NonNull Throwable throwable) {
        tracker.send(new HitBuilders.ExceptionBuilder()
                .setDescription(new StandardExceptionParser(context, null)
                        .getDescription(Thread.currentThread().getName(), throwable))
                .setFatal(false)
                .build());
    }

    public void setClientId(String id) {
        if (tracker != null) {
            tracker.setClientId(id);
        }
    }


    public static class Builder extends GoogleAnalyticsTracker.Builder {

        Builder(Application context, String appKey) {
            super(context, appKey);
        }

        @Override
        public GoogleAnalyticsTracker build() {
            return new GoogleAnalyticsTrackerImpl(this);
        }
    }
}
