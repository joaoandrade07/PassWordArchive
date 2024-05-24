package com.joaoandrade.passwordarchive.Controller;


import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class ExternalListener extends AppCompatActivity {
    public ExternalListener(){

    }
    public void IniciarToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        .show();
    }

    public void SnackBar(View view,String msg, int backgroundColor, Integer textColor){
        if(textColor == null){
            textColor = -1;
        }
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(backgroundColor);
        snackbar.setTextColor(textColor);
        snackbar.show();
    }



}
