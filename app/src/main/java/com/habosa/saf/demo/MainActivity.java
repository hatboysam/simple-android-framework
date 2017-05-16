package com.habosa.saf.demo;

import com.habosa.saf.HostActivity;
import com.habosa.saf.NoneState;

public class MainActivity extends HostActivity {

    @Override
    protected void initialize() {
        // Start off by showing the "Hello, World" screen
        show(HelloWorldScreen.class, new NoneState());
    }

}
