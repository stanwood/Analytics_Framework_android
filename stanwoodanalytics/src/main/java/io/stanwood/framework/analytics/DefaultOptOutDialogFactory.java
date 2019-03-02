package io.stanwood.framework.analytics;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DefaultOptOutDialogFactory implements OptOutDialogFactory {
    @NonNull
    @Override
    public DialogFragment createDialog() {
        return new OptOutDialog();
    }
}
