package com.habosa.saf;

import android.support.annotation.LayoutRes;
import android.support.annotation.RestrictTo;
import android.view.View;

/**
 * Basic controller object for UI.
 */
public abstract class Screen<T extends ScreenState> {

    private Host mHost;

    public Screen() {}

    /**
     * Get the layout id used to display this screen.
     * @return a resource ID pointing to a layout file.
     */
    @LayoutRes public abstract int getLayout();

    /**
     * Called each time the screen's view is inflated and displayed by the {@link Host}.
     * @param view the view for this screen.
     * @param state state passed into this screen.
     */
    public abstract void onDisplay(View view, T state);

    /**
     * Called when the host needs this screen to save its state. Screens should always be prepared
     * for this method to be called.
     * @return the current ScreenState, which will be restored later if necessary.
     */
    public abstract T onSaveState();

    /**
     * Called each time the screen is removed from screen by the {@link Host}.
     */
    public void onHide() {}

    /**
     * Called when the user presses the back button. Screens should override this method if they
     * want to intercept back button presses.
     * @return {@code true} if the screen wants to override the default behavior, {@code false}
     * otherwise.
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * Get the {@link Host} displaying this screen. May be {@code null} before
     * {@link #onDisplay(View, ScreenState)} is called.
     * @return a Host, or null.
     */
    public Host getHost() {
        return mHost;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected void setHost(Host host) {
        this.mHost = host;
    }

}
