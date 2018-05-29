package io.stanwood.framework.analytics.testfairy;

import android.app.Application;

public class TestfairyTrackerImpl extends TestfairyTracker {
    public static Builder builder(Application context, String appKey) {
        return new Builder(context,appKey);
    }

    TestfairyTrackerImpl(TestfairyTracker.Builder builder){
        super(builder);
    }


    public static class Builder extends TestfairyTracker.Builder {
        Builder(Application context,String appKey) {
            super(context,appKey);
        }

        public TestfairyTracker build(){
            return new TestfairyTrackerImpl(this);
        }
    }
}
