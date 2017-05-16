package com.habosa.saf;

import android.os.Bundle;

/**
 * Represents the state of an {@link Screen}. Must be able to save to / restore from a
 * {@link Bundle}. For most cases using {@link pub.devrel.bundler.BundlerClass} can simplify
 * this object by getting the bundle code for free.
 */
public abstract class ScreenState {

    public ScreenState() {}

    public ScreenState(Bundle bundle) {}

    public Bundle toBundle() {
        return new Bundle();
    }

}
