package com.example.ahmedd.firabasetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

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

    String username;
    String userEmail;
    String img_url;
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

        btn_FBLogin_start.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        btn_FBLogin_start.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.e("faceBookLogin","Loading....");

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(final JSONObject object, GraphResponse response) {


                        Log.e("json",object.toString());
                        Log.e("reponse",response.toString());

                        try {
                            Log.e("jsonTry",object.getString("email"));
                            Log.e("jsonTry", object.getString("first_name"));
                            Log.e("jsonTry", object.getString("birthday"));

                            username = object.getString("first_name");
                            userEmail = object.getString("email");
                            img_url ="http://graph.facebook.com/"+object.getString("id")+"/picture?height=1000&width=1000&type=square&redirect=true";

                            AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                            MyFireBase.getAuth().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        Log.e("Task Credential", "Successful");


                                        FirebaseUser user = MyFireBase.getCurrentUser();
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("userName", username);
                                        hashMap.put("id", user.getUid());
                                        hashMap.put("ImageURL", img_url);
                                        hashMap.put("status", R.string.offline);


                                        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                                                .setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(StartActivity.this, Main2Activity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    Toast.makeText(StartActivity.this, "successfuil register", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(StartActivity.this, "You can't register", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    } else if (!task.isSuccessful()) {
                                        Log.e("Task Credential", "not Successful");
                                    }


                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }) ;

                Bundle bundle = new Bundle();
                bundle.putString("fields", "id,first_name,last_name,email,gender,birthday,picture.type(large)"); // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
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


        if (AccessToken.getCurrentAccessToken() != null) {
            Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


    }

    public void register(final String userName, String email, String password) {
        Log.e("Register Method", "Is here");
        //to create a new user with the email and password


        MyFireBase.getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("Task", "Successful");
                            Log.e("userName", "userName");
                            Log.e("id", "userID");

                            //to get the userID
                            FirebaseUser user = MyFireBase.getAuth().getCurrentUser();
                            String userID = MyFireBase.getCurrentUser().getUid();

                            //make a branch users with childs each one named by its userID;
                            //get a reference on each child and make some info
                            //ID,userName,ImageURL

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userID);
                            hashMap.put("userName", userName);
                            hashMap.put("ImageURL", "default");
                            hashMap.put("status", R.string.offline);


                            MyFireBase.getReferenceOnAllUsers().child(userID).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(StartActivity.this, Main2Activity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Toast.makeText(StartActivity.this, "successfuil register", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(StartActivity.this, "You can't register", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else if (!task.isSuccessful()) {
                            Log.e("Task", "not Successful");
                        }

                    }
                });


    }
}
