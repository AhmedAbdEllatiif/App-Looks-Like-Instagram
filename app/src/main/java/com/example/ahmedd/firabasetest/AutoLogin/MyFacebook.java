package com.example.ahmedd.firabasetest.AutoLogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.Main2Activity;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;


public class MyFacebook {


    private static Bundle bundle;
    private static String username;
    private static String img_url;

    public static void Login(final Activity context, LoginButton loginButton, CallbackManager callbackManager){

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
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
                                        hashMap.put("status", String.valueOf(R.string.offline));


                                        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                                                .setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(context, Main2Activity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    context.startActivity(intent);
                                                    Toast.makeText(context, "successfuil register", Toast.LENGTH_SHORT).show();
                                                    context.finish();
                                                } else {
                                                    Toast.makeText(context.getApplicationContext(), "You can't register", Toast.LENGTH_SHORT).show();
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

                // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
                bundle = new Bundle();
                bundle.putString("fields", "id,first_name,last_name,email,gender,birthday,picture.type(large)");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {
                Log.e("LoginWithFB","Canceld");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Error",error.toString());
            }
        });
    }

}
