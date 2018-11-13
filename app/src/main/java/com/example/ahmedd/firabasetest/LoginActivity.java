package com.example.ahmedd.firabasetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {


    private TextInputEditText email, password;
    private Button btn_login;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        setToolBar();
        clickLoginButton();


    }

    private void initViews() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        toolbar = findViewById(R.id.myToolBar);
    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void clickLoginButton() {
        //to login with the email & password
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                Log.e("login","clicked");
                if (txt_email.isEmpty() || txt_password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "All fields required ", Toast.LENGTH_SHORT).show();
                } else {
                    MyFireBase.getAuth().signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.e("isSuccessfulLogin",task.isSuccessful()+"");
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Log.e("login","clicked");
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this, "Auth failed...!", Toast.LENGTH_SHORT).show();
                                        Log.e("login","clicked But failed");
                                    }

                                }
                            });
                }
            }
        });
    }



}
