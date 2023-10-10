package com.joaoandrade.passwordarchive.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
//import android.view.View;

import androidx.appcompat.widget.SearchView;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.navigation.NavController;
//import androidx.navigation.fragment.NavHostFragment;
//import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.joaoandrade.passwordarchive.Model.UserModel;
import com.joaoandrade.passwordarchive.Notificacoes;
import com.joaoandrade.passwordarchive.R;
import com.joaoandrade.passwordarchive.databinding.HomeBinding;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private HomeBinding  binding;

//    private NavHostFragment navHostFragment;
//    private NavController navController;
    Notificacoes notificacoes;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        notificacoes = new Notificacoes(this);
        user = new UserModel();
        user.setId(Objects.requireNonNull(auth.getCurrentUser()).getUid());

        binding.menuItem1.setOnClickListener(view -> {
            notificacoes.notificacao("Teste", "Vamos ver se mantem o canal criado");
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem menuItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) menuItem.getActionView();
        assert searchView != null;
        searchView.setMaxWidth(Integer.MAX_VALUE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}