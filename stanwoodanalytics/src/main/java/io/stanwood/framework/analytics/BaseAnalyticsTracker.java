package io.stanwood.framework.analytics;


import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import java.util.Map;

import io.stanwood.framework.analytics.fabric.FabricTracker;
import io.stanwood.framework.analytics.firebase.FirebaseTracker;
import io.stanwood.framework.analytics.generic.AnalyticsTracker;
import io.stanwood.framework.analytics.generic.Tracker;
import io.stanwood.framework.analytics.generic.TrackerContainer;
import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingEvent;
import io.stanwood.framework.analytics.generic.TrackingKey;
import timber.log.Timber;

/**
 * TrackerContainer proxy to force default tracker integration
 */
public class BaseAnalyticsTracker implements AnalyticsTracker, TrackerContainer.TrackerMigrationCallback {
    private final TrackerContainer trackerContainer;

    @Nullable
    private OptOutDialogFactory dialogFactory = null;

    protected BaseAnalyticsTracker(@NonNull Context context, @NonNull FabricTracker fabricTracker, @NonNull FirebaseTracker firebaseTracker,
                                   @Nullable Tracker... optional) {
        TrackerContainer.Builder builder = TrackerContainer.builder(context)
                .addTracker(fabricTracker, firebaseTracker)
                .setMigrationCallback(this);
        if (optional != null) {
            builder.addTracker(optional);
        }
        trackerContainer = builder.build();
        Timber.plant(new TrackerTree(trackerContainer));
    }

    /**
     * Override to set the enabled state of a tracker for the first time
     * This should be used to migrate opt out state from other tracking frameworks
     *
     * @param trackerName Name of the tracker ro migrate
     * @return tracker enabled state , default true
     */
    @Override
    public boolean migrateTrackerState(@NonNull String trackerName) {
        return true;
    }

    /***
     * Convenience function for setting state from a e.g. view context
     * @param context FragmentActivity child context
     * @param enable State to set tracker to
     * @param trackerNames List of tracker names or null to apply to all trackers
     */
    public void enable(@NonNull Context context, boolean enable, @Nullable String... trackerNames) {
        FragmentActivity activity = findActivity(context);
        if (activity == null) {
            throw new IllegalStateException("Illegal context used");
        }
        enableImpl(activity, enable, trackerNames);
    }

    /***
     * Set enable state of given tracker names
     * @param context FragmentActivity context
     * @param enable State to set tracker to
     * @param trackerNames List of tracker names or null to apply to all trackers
     */
    public void enable(@NonNull FragmentActivity context, boolean enable, @Nullable String... trackerNames) {
        enableImpl(context, enable, trackerNames);
    }

    /***
     * Set enable state of given tracker names , use this in e.g. watch apps
     * This will NOT show any opt out dialogs
     * @param enable State to set tracker to
     * @param trackerNames List of tracker names or null to apply to all trackers
     */
    public void enableSilent(boolean enable, @Nullable String... trackerNames) {
        enableImpl(null, enable, trackerNames);
    }

    public void setOptOutDialogFactory(@Nullable OptOutDialogFactory factory) {
        dialogFactory = factory;
    }

    @NonNull
    private OptOutDialogFactory getOptOutDialogFactory() {
        return dialogFactory == null ? new DefaultOptOutDialogFactory() : dialogFactory;
    }

    private void enableImpl(@Nullable FragmentActivity context, boolean enable, @Nullable String... trackerNames) {
        if (!enable) {
            trackEvent(TrackerParams.builder("tracking_opt_out").build());
        }
        trackerContainer.enableTrackers(enable, trackerNames);
        if (enable) {
            trackEvent(TrackerParams.builder("tracking_opt_in").build());
        } else if (context != null) {
            DialogFragment dialog = getOptOutDialogFactory().createDialog();
            dialog.show(context.getSupportFragmentManager(), "analytics_opt_out");
        }
    }


    private FragmentActivity findActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof FragmentActivity) {
                return (FragmentActivity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /***
     * Check if there is at least one enabled tracker
     * @return true if there are enabled trackers
     */
    public boolean hasEnabledTracker() {
        return trackerContainer.hasEnabledTracker();
    }

    /***
     * Returns the trackers enabled state
     * @param trackerName Tracker name
     * @return true if tracker is enabled
     */
    public boolean isTrackerEnabled(@NonNull String trackerName) {
        return trackerContainer.isTrackerEnabled(trackerName);
    }

    /**
     * Tracks a screen view.
     * <br><br>
     *
     * @param screenName an unique screen name
     */
    public void trackScreenView(@NonNull String screenName) {
        trackScreenView(screenName, null);
    }

    /***
     * Tracks a screen view.
     * <br><br>
     *
     * @param screenName an unique screen name
     * @param customProps custom property's
     */
    protected void trackScreenView(@NonNull String screenName, Map<String, Object> customProps) {
        trackerContainer.trackEvent(TrackerParams.builder(TrackingEvent.SCREEN_VIEW).setName(screenName).addCustomProperty(customProps).build());
    }

    /**
     * Tracks a user.
     * <br><br>
     *
     * @param id        the user ID
     * @param email     the user's Email address
     * @param pushToken the device push token
     */
    public void trackUser(@Nullable String id, @Nullable String email, @Nullable String pushToken) {
        trackerContainer.trackEvent(TrackerParams.builder(TrackingEvent.IDENTIFY_USER)
                .addCustomProperty(TrackingKey.USER_ID, id)
                .addCustomProperty(TrackingKey.USER_EMAIL, email)
                .addCustomProperty(TrackingKey.PUSH_TOKEN, pushToken)
                .build());
    }

    public void trackPurchase(@NonNull String id, @NonNull String orderId, @NonNull String name, int quantity, double price, @Nullable String category, @Nullable String brand) {
        trackerContainer.trackEvent(TrackerParams.builder(TrackingEvent.PURCHASE)
                .setName(name)
                .setId(id)
                .setCategory(category)
                .addCustomProperty(TrackingKey.PURCHASE_ORDERID, orderId)
                .addCustomProperty(TrackingKey.PURCHASE_QUANTITY, quantity)
                .addCustomProperty(TrackingKey.PURCHASE_PRICE, price)
                .addCustomProperty(TrackingKey.PURCHASE_BRAND, brand)
                .build());
    }

    /**
     * Tracks a full-fledged event.
     * <br><br>
     * For simple screen views use {@link #trackScreenView(String)}
     * <br><br>
     * Will become PROTECTED in the future!
     *
     * @param params {@link TrackerParams}
     */
    @Override
    public void trackEvent(TrackerParams params) {
        trackerContainer.trackEvent(params);
    }

}
