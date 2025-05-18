package com.example.chefvlogosss.ui.upload;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chefvlogosss.MainActivity;
import com.example.chefvlogosss.R;

import com.example.chefvlogosss.data.model.VlogVideo;
import com.example.chefvlogosss.databinding.ActivityVlogUploadBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class VlogUploadActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityVlogUploadBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVlogUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText vTitle = binding.vlogtitle;
        final EditText vLink = binding.vloglink;
        final Button loginButton = binding.upload;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(true);

            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(vTitle.getText().toString(),
                        vLink.getText().toString());
            }
        };
        vTitle.addTextChangedListener(afterTextChangedListener);
        vLink.addTextChangedListener(afterTextChangedListener);
        vLink.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(vTitle.getText().toString(),
                            vLink.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(v -> {
            feltolt(String.valueOf(vTitle.getText()), String.valueOf(vLink.getText()));
            valtas();
        });
    }

    public void feltolt(String cim, String link){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Automatikus dokumentum ID generálása
            DocumentReference vlogRef = db.collection("vlogs").document();
            String generatedVlogId = vlogRef.getId();

            // Példa videó adatok objektumba
            VlogVideo vlog = new VlogVideo(
                    userId,
                    link,
                    cim,
                    generatedVlogId
            );

            vlogRef.set(vlog)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Sikeres feltöltés!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hiba: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });

        } else {
            Toast.makeText(this,"Nincs bejelentkezett felhasználó!", Toast.LENGTH_LONG).show();
        }

    }
    public void valtas(){
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}