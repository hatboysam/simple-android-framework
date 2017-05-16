package com.habosa.saf;

import android.content.Context;

public interface Host {

    Context getContext();

    <T extends ScreenState> void show(Class<? extends Screen<T>> screenClass, T state);

}
