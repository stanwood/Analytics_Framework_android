package io.stanwood.framework.analytics;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class DefaultOptOutDialogFactory implements OptOutDialogFactory {
    @NonNull
    @Override
    public DialogFragment createDialog() {
        return new OptOutDialog();
    }
}
