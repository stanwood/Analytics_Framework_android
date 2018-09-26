package io.stanwood.framework.analytics;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

import io.stanwood.framework.analytics.generic.TrackerContainer;
import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingEvent;
import timber.log.Timber;

class TrackerTree extends Timber.Tree {
    private final TrackerContainer trackerContainer;

    TrackerTree(@NonNull TrackerContainer trackerContainer) {
        this.trackerContainer = trackerContainer;
    }

    @Override
    protected boolean isLoggable(String tag, int priority) {
        return priority == Log.DEBUG || priority == Log.ERROR || priority == Log.ASSERT;
    }

    @Override
    protected void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable t) {
        if ((priority == Log.ERROR || priority == Log.ASSERT) && t != null) {
            trackerContainer.trackException(t);
        } else {
            TrackerParams.Builder builder = TrackerParams.builder(TrackingEvent.DEBUG);
            if (!TextUtils.isEmpty(message)) {
                builder.setName(message);
            }
            if (t != null) {
                builder.setId(getMessage(t));
            }
            trackerContainer.trackEvent(builder.build());
        }
    }

    private String getMessage(Throwable throwable) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = null;
        try {
            printStream = new PrintStream(out);
            throwable.printStackTrace(printStream);
            return new String(out.toByteArray(), Charset.forName("UTF-8"));
        } finally {
            if (printStream != null) {
                printStream.close();
            }
        }
    }
}