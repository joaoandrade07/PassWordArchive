package com.joaoandrade.passwordarchive.View;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
//import android.view.View;

import androidx.appcompat.widget.SearchView;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.navigation.NavController;
//import androidx.navigation.fragment.NavHostFragment;
//import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.joaoandrade.passwordarchive.Controller.ExternalListener;
import com.joaoandrade.passwordarchive.Model.MessageEventBus;
import com.joaoandrade.passwordarchive.Model.UserModel;
import com.joaoandrade.passwordarchive.Controller.Notificacoes;
import com.joaoandrade.passwordarchive.R;
import com.joaoandrade.passwordarchive.databinding.HomeBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private HomeBinding  binding;

//    private NavHostFragment navHostFragment;
//    private NavController navController;
//    Notificacoes notificacoes;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
//        notificacoes = new Notificacoes(this);
        user = new UserModel();
//        user.setId(Objects.requireNonNull(auth.getCurrentUser()).getUid());

//        binding.menuItem1.setOnClickListener(view -> {
//            EventBus.getDefault().post(new MessageEventBus("Hello everyone!", view));
//        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }



    //Para criar as opções do menu
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem menuItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) menuItem.getActionView();
        assert searchView != null;
        searchView.setMaxWidth(Integer.MAX_VALUE);
        return true;
    }

    //Quando selecionar um item do menu faz tal ação
    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.searchView){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}