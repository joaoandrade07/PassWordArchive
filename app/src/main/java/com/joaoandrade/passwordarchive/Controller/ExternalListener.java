package com.joaoandrade.passwordarchive.Controller;


import static android.provider.Settings.Secure.getString;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.startActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.provider.Settings;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.joaoandrade.passwordarchive.View.HomeActivity;
import com.joaoandrade.passwordarchive.View.LoginActivity;

import java.util.concurrent.Executor;

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
