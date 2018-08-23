package io.stanwood.framework.analytics;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public interface OptOutDialogFactory {
    @NonNull
    DialogFragment createDialog();
}
