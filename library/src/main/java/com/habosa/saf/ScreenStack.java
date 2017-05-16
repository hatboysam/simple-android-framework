package com.habosa.saf;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Stack;

/**
 * A simple stack of {@link ScreenInfo} objects that can be written to or restored from
 * a {@link Bundle}
 */
public class ScreenStack extends Stack<ScreenInfo> {

    private static final String KEY_SCREEN_INFO_LIST = "KEY_SCREEN_INFO_LIST";

    public ScreenStack() {}

    public void writeToBundle(Bundle out) {
        ArrayList<ScreenInfo> list = new ArrayList<>(this);
        out.putParcelableArrayList(KEY_SCREEN_INFO_LIST, list);
    }

    public ScreenStack(Bundle in)  {
        ArrayList<ScreenInfo> list = in.getParcelableArrayList(KEY_SCREEN_INFO_LIST);
        addAll(list);
    }

}
