package com.joaoandrade.passwordarchive.UI.AlterarEmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.joaoandrade.passwordarchive.UI.TelaPrincipal.HomeActivity;
import com.joaoandrade.passwordarchive.databinding.AlterarEmailBinding;

public class AlterarEmailActivity extends AppCompatActivity {

    AlterarEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AlterarEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.voltarSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.voltarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AlterarEmailActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}