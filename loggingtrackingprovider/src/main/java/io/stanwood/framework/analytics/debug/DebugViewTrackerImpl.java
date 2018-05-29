package io.stanwood.framework.analytics.debug;


import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.stanwood.framework.analytics.generic.TrackerParams;

public class DebugViewTrackerImpl extends DebugViewTracker {

    protected DebugViewTrackerImpl(Builder builder) {
        super(builder);
    }

    public static Builder builder(Application context) {
        return new Builder(context);
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        JSONObject object = new JSONObject();
        try {
            object.put("time", System.currentTimeMillis());
            object.put("eventname", params.getEventName());
            if (params.getCustomPropertys() != null) {
                object.put("itemid", params.getCustomPropertys());
            } else if (!TextUtils.isEmpty(params.getItemId())) {
                object.put("itemid", params.getItemId());
            }
            if (!TextUtils.isEmpty(params.getName())) {
                object.put("name", params.getName());
            }
            Intent intent = new Intent("io.stanwood.action.log.tracker");
            intent.putExtra("data", object.toString());
            intent.putExtra("appid", context.getPackageName());
            context.sendBroadcast(intent);

        } catch (JSONException e) {
            // noop
        }
    }

    public static class Builder extends DebugViewTracker.Builder {

        Builder(Application context) {
            super(context);
        }

        @Override
        public DebugViewTrackerImpl build() {
            return new DebugViewTrackerImpl(this);
        }
    }

}