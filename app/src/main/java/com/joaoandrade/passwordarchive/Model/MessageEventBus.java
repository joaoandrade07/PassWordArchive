package com.joaoandrade.passwordarchive.Model;

import android.view.View;

public class MessageEventBus {

    public final String message;

    private final View view;

    public MessageEventBus(String message, View view) {
        this.message = message;
        this.view = view;
    }

    public View getView() {
        return view;
    }
}
