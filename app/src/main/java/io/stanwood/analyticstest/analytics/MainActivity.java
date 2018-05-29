package io.stanwood.analyticstest.analytics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.stanwood.framework.analytics.fabric.FabricTrackerImpl;
import io.stanwood.framework.analytics.firebase.FirebaseTrackerImpl;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void samples() {
        AdvancedAppTracker.init(getApplication());
        AdvancedAppTracker.instance().trackUser("alice", "alice@bob.com", null);
        AdvancedAppTracker.instance().trackAdLoaded("123456");
        AdvancedAppTracker.instance().trackShowDetails("id", "details of id");
        AdvancedAppTracker.instance().enable(this, true);
        AdvancedAppTracker.instance().enable(this, true, FabricTrackerImpl.TRACKER_NAME, FirebaseTrackerImpl.TRACKER_NAME);
        AdvancedAppTracker.instance().isTrackerEnabled(FabricTrackerImpl.TRACKER_NAME);
        Timber.d("message");
        Timber.e(new IllegalStateException("error"));
    }
}
