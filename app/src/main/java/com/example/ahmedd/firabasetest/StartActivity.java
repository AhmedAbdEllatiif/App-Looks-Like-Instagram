package com.example.ahmedd.firabasetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class StartActivity extends AppCompatActivity {

    private Button btn_login,btn_register;
    private LoginButton btn_FBLogin_start;
    FirebaseUser firebaseUser;
    CallbackManager callbackManager;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
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

        btn_login = findViewById(R.id.btn_login_start);
        btn_register = findViewById(R.id.btn_register_start);
        btn_FBLogin_start = (LoginButton) findViewById(R.id.btn_FBLogin_start);
/*
        if (MyFireBase.getCurrentUser() != null){
            startActivity(new Intent(StartActivity.this,MainActivity.class));
            finish();
        }*/



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


        callbackManager = CallbackManager.Factory.create();
        btn_FBLogin_start.setReadPermissions(Arrays.asList("public_profile","email","user_birthday"));
        btn_FBLogin_start.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("faceBookLogin","Loading....");

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {


                        Log.e("json",object.toString());
                        Log.e("reponse",response.toString());

                        try {
                            Log.e("jsonTry",object.getString("email"));
                            Log.e("jsonTry",object.getString("name"));
                            Log.e("jsonTry",object.getString("user_birthday"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }) ;

                Bundle bundle = new Bundle();
                bundle.putString("fields", "id,first_name,last_name,email,gender,birthday"); // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {

                Toast.makeText(StartActivity.this, "canceled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {

                Log.e("Error",error.toString());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        

    }
}
