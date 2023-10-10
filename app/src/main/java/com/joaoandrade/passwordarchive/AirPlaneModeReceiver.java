package com.joaoandrade.passwordarchive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.audiofx.Equalizer;
import android.provider.Settings;
import android.util.Log;

import com.joaoandrade.passwordarchive.View.SettingsActivity;

import java.util.Objects;

public class AirPlaneModeReceiver extends BroadcastReceiver {

    ExternalListener externalListener = new ExternalListener();

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (Objects.equals(intent.getAction(), Intent.ACTION_AIRPLANE_MODE_CHANGED) && Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON) == 1){
                externalListener.IniciarToast(context, context.getString(R.string.modo_aviao_ativado));
            } else {
                externalListener.IniciarToast(context, context.getString(R.string.modo_aviao_desativado));
            }
        }catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }
}
