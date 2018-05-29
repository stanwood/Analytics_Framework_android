package io.stanwood.framework.analytics.firebase;


import android.app.Application;

public class FirebaseTrackerImpl extends FirebaseTracker {

    protected FirebaseTrackerImpl(Builder builder) {
        super(builder);
    }

    public static Builder builder(Application context) {
        return new Builder(context);
    }

    public static class Builder extends FirebaseTracker.Builder {
        Builder(Application context) {
            super(context);
        }

        @Override
        public FirebaseTrackerImpl build() {
            if (mapFunc == null) {
                mapFunc = new DefaultMapFunction();
            }
            return new FirebaseTrackerImpl(this);
        }
    }

}