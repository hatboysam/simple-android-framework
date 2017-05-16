package com.habosa.saf;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Activity that can be a host for {@link Screen} objects.
 */
public abstract class HostActivity extends AppCompatActivity implements Host {

    private static final String TAG = "HostActivity";

    // Current screen
    private Screen mCurrentScreen;

    // Stack of ScreenInfo for recreating things
    private ScreenStack mScreenStack = new ScreenStack();

    /**
     * Called for first-time initialization, not called when the Activity can be restored
     * from saved instance state.
     */
    protected abstract void initialize();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Attempt to restore the screen stack
        if (savedInstanceState != null) {
            mScreenStack = new ScreenStack(savedInstanceState);
        }

        if (getCurrentScreenInfo() != null) {
            // If we have a restored stack, then just resume
            showScreenFromInfo(getCurrentScreenInfo());
        } else {
            // Otherwise, tell the Activity to show a screen
            initialize();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO(samstern): What about onStart?
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Tell screen it was hidden
        hideCurrentScreen();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save state and write
        saveCurrentScreenState();
        mScreenStack.writeToBundle(outState);
    }

    @Override
    public void onBackPressed() {
        // Attempt delegation to the screen, otherwise go back
        if (mCurrentScreen.onBackPressed()) {
            Log.d(TAG, "onBackPressed handled by screen");
        } else if (goBack()) {
            Log.d(TAG, "onBackPressed handled by HostActivity");
        } else {
            Log.d(TAG, "Delegating onBackPressed to system");
            super.onBackPressed();
        }
    }

    @Override
    public <T extends ScreenState> void show(Class<? extends Screen<T>> screenClass, T state) {
        // Show the screen
        showScreenFromInfo(new ScreenInfo<T>(screenClass, state));
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * Show a screen from ScreenInfo.
     */
    private <T extends ScreenState> void showScreenFromInfo(@NonNull ScreenInfo<T> info) {
        // Instantiate the screen
        Screen<T> screen;
        try {
            screen = info.getScreenClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate screen", e);
        }

        // Inflate and display view
        setContentView(screen.getLayout());

        // Set as current
        setCurrentScreen(screen, info);
    }

    /**
     * Go back by one level. If possible, drop the top screen of the stack and show
     * the new top screen.
     * @return true if a new screen from the stack was shown.
     */
    private boolean goBack() {
        // Pop current screen off the stack
        mScreenStack.pop();

        // Notify current screen it is being hidden
        hideCurrentScreen();

        if (getCurrentScreenInfo() == null) {
            // Nothing left on the stack, so we can't go back
            return false;
        } else {
            // Show the top thing on the stack
            showScreenFromInfo(getCurrentScreenInfo());
            return true;
        }
    }

    @Nullable
    private ScreenInfo getCurrentScreenInfo() {
        if (mScreenStack.isEmpty()) {
            return null;
        }

        return mScreenStack.peek();
    }

    private void hideCurrentScreen() {
        if (mCurrentScreen != null) {
            mCurrentScreen.onHide();
        }
    }

    private <T extends ScreenState> void setCurrentScreen(Screen<T> screen, ScreenInfo<T> info) {
        // Don't add the same screen twice
        if (!info.equals(getCurrentScreenInfo())) {
            // Save current screen state
            saveCurrentScreenState();

            // Push new screen onto back stack
            mScreenStack.push(info);
        }

        // Set new screen
        mCurrentScreen = screen;

        // Set host and then notify the screen it was shown
        screen.setHost(this);
        screen.onDisplay(findViewById(android.R.id.content), info.getState());
    }

    private void saveCurrentScreenState() {
        if (getCurrentScreenInfo() != null) {
            getCurrentScreenInfo().setState(mCurrentScreen.onSaveState());
        }
    }

}
