package io.stanwood.framework.analytics.debug;


import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.util.Map;

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
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis())
                .append("|")
                .append(params.getEventName())
                .append("|");
        if (!TextUtils.isEmpty(params.getItemId())) {
            sb.append("itemid").append("|").append(params.getItemId()).append("|");
        }
        if (!TextUtils.isEmpty(params.getName())) {
            sb.append("name").append("|").append(params.getName()).append("|");
        }
        if (!TextUtils.isEmpty(params.getContentType())) {
            sb.append("content type").append("|").append(params.getContentType()).append("|");
        }
        if (!TextUtils.isEmpty(params.getCategory())) {
            sb.append("category").append("|").append(params.getCategory()).append("|");
        }
        if (params.getCustomPropertys() != null && !params.getCustomPropertys().isEmpty()) {
            for (Map.Entry<String, Object> entry : params.getCustomPropertys().entrySet()) {
                sb.append(entry.getKey()).append("|").append(entry.getValue() != null ? entry.getValue().toString() : "").append("|");
            }
        }
        sb.setLength(sb.length() - 1);
        Intent intent = new Intent("io.stanwood.debugapp.plugin");
        intent.putExtra("source", "debugtracker");
        intent.putExtra("data", sb.toString());
        context.sendBroadcast(intent);
    }

    @Override
    public void track(@NonNull Throwable throwable) {
        Intent intent = new Intent("io.stanwood.debugapp.plugin");
        intent.putExtra("source", "debugtracker");
        intent.putExtra("data", System.currentTimeMillis() + "|exception|" + Log.getStackTraceString(throwable));
        context.sendBroadcast(intent);
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