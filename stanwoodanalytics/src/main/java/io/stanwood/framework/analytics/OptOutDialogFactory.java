package io.stanwood.framework.analytics;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public interface OptOutDialogFactory {
    @NonNull
    DialogFragment createDialog();
}
