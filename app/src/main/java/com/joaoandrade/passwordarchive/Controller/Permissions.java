package com.joaoandrade.passwordarchive.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Permissions extends AppCompatActivity {

    public Permissions(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }
    private final Activity activity;
    private final Context context;
    private static final int PERMISSAO_CODE = 100;

    private static final String PERMISSAO_GALERIA = Manifest.permission.READ_EXTERNAL_STORAGE;

    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public void requestRuntimePermission(){
        if(ActivityCompat.checkSelfPermission(context, PERMISSAO_GALERIA) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Permissao garantida", Toast.LENGTH_SHORT).show();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSAO_GALERIA)) {
            alertDialog = new AlertDialog.Builder(context)
                    .setTitle("Atanção")
                    .setCancelable(false)
                    .setMessage("Precisamos do acesso a galeria do dispositivo, deseja permitir agora?")
                    .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(activity, new String[]{PERMISSAO_GALERIA}, PERMISSAO_CODE);
                        }
                    });
            //alertDialog.create();
            alertDialog.show();
        }else{
            ActivityCompat.requestPermissions(activity, new String[]{PERMISSAO_GALERIA}, PERMISSAO_CODE);
        }
    }



}
