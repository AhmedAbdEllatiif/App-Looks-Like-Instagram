package com.example.ahmedd.firabasetest.Fragments.StartFragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.ahmedd.firabasetest.Activities.MainActivity;
import com.example.ahmedd.firabasetest.R;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginFragment extends Fragment {

    private View view;

    private TextInputEditText email, password;
    private Button btn_login;
    private Toolbar toolbar;
    private TextView txt_forgetPassword;

    private String txt_email;
    String txt_password;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        initViews();

        //setToolBar();
        clickLoginButton();

        return view;
    }
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        initViews();

        //setToolBar();
        clickLoginButton();


    }*/

    private void initViews() {
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        txt_forgetPassword = view.findViewById(R.id.txt_forgetPassword);
        btn_login = view.findViewById(R.id.btn_login);
        toolbar = view.findViewById(R.id.myToolBar);
    }

   /* private void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }*/

    private void clickLoginButton() {
        //to login with the email & password
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_email = email.getText().toString().trim();
                txt_password = password.getText().toString().trim();
                Log.e("login","clicked");
                if (txt_email.isEmpty() || txt_password.isEmpty()) {
                    Toast.makeText(getActivity(), "All fields required ", Toast.LENGTH_SHORT).show();
                } else {
                    MyFireBase.getAuth().signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.e("isSuccessfulLogin",task.isSuccessful()+"");
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Log.e("login","clicked");
                                        getActivity().finish();
                                    }
                                    else {
                                        Toast.makeText(getActivity(), "Auth failed...!", Toast.LENGTH_SHORT).show();
                                        Log.e("login","clicked But failed");
                                    }

                                }
                            });
                }
            }
        });


        txt_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);

                if (email.getText().toString().trim().equals("")) {
                    intent.putExtra("Email", "");
                }else {
                    intent.putExtra("Email",email.getText().toString().trim());
                }
                startActivity(intent);

            }
        });

    }



}
