package com.habosa.saf.demo;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.habosa.saf.Screen;
import com.habosa.saf.ScreenState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.bundler.BundlerClass;

/**
 * Displays a message passed in from {@link HelloWorldScreen}.
 */
public class ShowMessageScreen extends Screen<ShowMessageScreen.MessageState> {

    private MessageState mState;

    @BindView(R.id.text_message)
    protected TextView mMessageView;

    @Override
    public int getLayout() {
        return R.layout.screen_message;
    }

    @Override
    public void onDisplay(View view, MessageState state) {
        mState = state;

        // Bind views
        ButterKnife.bind(this, view);

        // Get the message passed in from the previous screen
        String message = state.message;

        // Show the message
        if (!TextUtils.isEmpty(message)) {
            mMessageView.setText(message);
        } else {
            mMessageView.setText("No message!");
        }
    }

    @Override
    public MessageState onSaveState() {
        return mState;
    }

    @OnClick(R.id.button_go_to_map)
    public void onGoToMapClick() {
        // Go to the map screen
        getHost().show(MapScreen.class, new MapScreen.MarkerState());
    }

    /**
     * State that is passed into this screen to display a message.
     */
    @BundlerClass
    public static class MessageState extends ScreenState {

        public String message;

        public MessageState() {}

        public MessageState(String message) {
            this.message = message;
        }

    }
}
