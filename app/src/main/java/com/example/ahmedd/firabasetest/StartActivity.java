package com.example.ahmedd.firabasetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.ahmedd.firabasetest.AutoLogin.MyFacebook;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class StartActivity extends AppCompatActivity {

    private Button btn_login,btn_register;
    private LoginButton btn_FBLogin_start;
    FirebaseUser firebaseUser;
    CallbackManager callbackManager;

    @Override
    protected void onStart() {
        super.onStart();

        //check if user is already logged in with firebase or facebook
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null || AccessToken.getCurrentAccessToken() != null){
            startActivity(new Intent(StartActivity.this,Main2Activity.class));
            finish();
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("898000400409074");
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_start);

        initViews();
        MyOnclickListeners();

        callbackManager = CallbackManager.Factory.create();
        MyFacebook.Login(this,btn_FBLogin_start,callbackManager);



    }

    private void initViews() {
        btn_login = findViewById(R.id.btn_login_start);
        btn_register = findViewById(R.id.btn_register_start);
        btn_FBLogin_start = findViewById(R.id.btn_FBLogin_start);
    }

    private void MyOnclickListeners() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,LoginActivity.class));
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,RegisterActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


    }

}
