package com.joaoandrade.passwordarchive.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.joaoandrade.passwordarchive.databinding.AlterarNomeBinding;

public class AlterarNomeActivity extends AppCompatActivity {

    AlterarNomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AlterarNomeBinding.inflate(getLayoutInflater());
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
                startActivity(new Intent(AlterarNomeActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}