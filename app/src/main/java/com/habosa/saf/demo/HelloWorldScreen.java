package com.habosa.saf.demo;

import android.view.View;
import android.widget.EditText;

import com.habosa.saf.NoneState;
import com.habosa.saf.Screen;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Simple screen that displays a text field. Demonstrates passing state to a new screen.
 * See {@link ShowMessageScreen}.
 */
public class HelloWorldScreen extends Screen<NoneState> {

    @BindView(R.id.edit_message)
    protected EditText mMessageField;

    @Override
    public int getLayout() {
        return R.layout.screen_hello_world;
    }

    @Override
    public void onDisplay(View view, NoneState state) {
        // Bind all views and click listeners
        ButterKnife.bind(this, view);
    }

    @Override
    public NoneState onSaveState() {
        return new NoneState();
    }

    @OnClick(R.id.button_next)
    public void onNextClick() {
        // Get the message
        String message = mMessageField.getText().toString();

        // Pass it to the next Screen
        getHost().show(ShowMessageScreen.class, new ShowMessageScreen.MessageState(message));
    }
}
