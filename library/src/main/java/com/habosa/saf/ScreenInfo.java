package com.habosa.saf;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import pub.devrel.bundler.EasyBundler;

public class ScreenInfo<T extends ScreenState> implements Parcelable {

    private static final String TAG = "ScreenInfo";

    private Class<? extends Screen<T>> mScreenClass;
    private T mState;

    public ScreenInfo(Class<? extends Screen<T>> screenClass, T state) {
        mScreenClass = screenClass;
        mState = state;
    }

    public Class<? extends Screen<T>> getScreenClass() {
        return mScreenClass;
    }

    public T getState() {
        return mState;
    }

    public void setState(T state) {
        mState = state;
    }

    @SuppressWarnings("unchecked")
    protected ScreenInfo(Parcel in) {
        String screenClassName = in.readString();
        String stateClassName = in.readString();
        Bundle stateBundle = in.readBundle(getClass().getClassLoader());

        try {
            Class screenClass = Class.forName(screenClassName);
            Class stateClass = Class.forName(stateClassName);

            T state;
            if (EasyBundler.hasBundler(stateClass)) {
                state = (T) EasyBundler.fromBundle(stateBundle, stateClass);
            } else {
                state = (T) stateClass.getConstructor(Bundle.class).newInstance(stateBundle);
            }

            new ScreenInfo<T>(screenClass, state);
        } catch (Exception e) {
            Log.e(TAG, "Exception restoring ScreenInfo from parcel", e);
        }
    }

    private Bundle stateToBundle() {
        if (EasyBundler.hasBundler(mState.getClass())) {
            return EasyBundler.toBundle(mState);
        } else {
            return mState.toBundle();
        }
    }

    public static final Creator<ScreenInfo> CREATOR = new Creator<ScreenInfo>() {
        @Override
        public ScreenInfo createFromParcel(Parcel in) {
            return new ScreenInfo(in);
        }

        @Override
        public ScreenInfo[] newArray(int size) {
            return new ScreenInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mScreenClass.getCanonicalName());
        parcel.writeString(mState.getClass().getCanonicalName());
        parcel.writeBundle(stateToBundle());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + mScreenClass.hashCode();

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ScreenInfo)) {
            return false;
        }

        ScreenInfo other = (ScreenInfo) obj;

        // Compare the screen class
        if (!mScreenClass.equals(other.getScreenClass())) {
            return false;
        }

        // Compare the actual state bundle
        if (!stateToBundle().equals(other.stateToBundle())) {
            return false;
        }

        // Screen info objects are equal
        return true;
    }
}
