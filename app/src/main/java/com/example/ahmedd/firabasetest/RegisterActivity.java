package com.example.ahmedd.firabasetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editTextUserName, editTextEmail, editTextPassword;
    private Button btn_register;
    private Toolbar toolbar;


    private static DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setToolBar();
        registerButtonListener();


        //!userName.isEmpty()


    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void checkIfUserNameStartsWithCapitalLetter() {
        editTextUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("edit Text", "afterChanged");
                if (editTextUserName.getText().toString().length() > 0) {

                    boolean isUpperCase = Character.isUpperCase(editTextUserName.getText().toString().charAt(0));
                    if (!isUpperCase) {
                        editTextUserName.setError("First letter must be uppercase");
                    }
                } else if (editTextUserName.getText().toString().length() == 0) {
                    editTextUserName.requestFocus();
                }

            }
        });
    }

    private void registerButtonListener() {

        checkIfUserNameStartsWithCapitalLetter();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextUserName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String birthday = "dd,MM,YYYY";

                if (userName.isEmpty()) {
                    editTextUserName.setError("Enter user name");
                } else if (email.isEmpty()) {
                    editTextEmail.setError(getString(R.string.enter_mail));
                } else if (password.isEmpty() || password.length() < 6) {
                    editTextPassword.setError("Enter password at least 6 characters");
                } else {
                    Log.e("Button", "Clicked");
                    register(userName, email, password,birthday);
                }

            }
        });
    }


    public void register(final String userName, String email, String password, final String userBirthday) {
        Log.e("Register Method","Is here");
        //to create a new user with the email and password
        MyFireBase.getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("Task","Successful");
                            Log.e("userName","userName");
                            Log.e("id","userID");

                            //to get the userID
                            FirebaseUser user = MyFireBase.getAuth().getCurrentUser();
                            String userID = MyFireBase.getCurrentUser().getUid();

                            //make a branch users with childs each one named by its userID;
                            //get a reference on each child and make some info
                            //ID,userName,ImageURL
                            userReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userID);
                            hashMap.put("userName", userName);
                            hashMap.put("ImageURL", "default");
                            hashMap.put("status",R.string.offline);
                            hashMap.put("birthday",userBirthday);


                            MyFireBase.getReferenceOnAllUsers().child(userID).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, Main2Activity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Toast.makeText(RegisterActivity.this, "successfuil register", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "You can't register", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else if(!task.isSuccessful()){
                            Log.e("Task","not Successful");
                        }

                    }
                });


    }


    private void initViews() {
        editTextUserName = findViewById(R.id.ediText_userName);
        editTextEmail = findViewById(R.id.ediText_Email);
        editTextPassword = findViewById(R.id.ediText_password);
        btn_register = findViewById(R.id.btn_register);
        toolbar = findViewById(R.id.myToolBar);

    }

}
