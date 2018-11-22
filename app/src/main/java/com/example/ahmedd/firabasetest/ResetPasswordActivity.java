package com.example.ahmedd.firabasetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ResetPasswordActivity extends AppCompatActivity {

    private Toolbar myResetPasswordToolbar;
    private Button btn_resetPassword;
    private TextInputEditText editTxt_email_resetActivity;
    private String EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initViews();
        setToolBar();

        btn_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTxt_email_resetActivity.equals("")){
                    editTxt_email_resetActivity.setError(getString(R.string.enter_mail));
                }else {
                    EMAIL = editTxt_email_resetActivity.getText().toString().trim();
                    MyFireBase.getAuth().sendPasswordResetEmail(EMAIL).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this, "Check your mail", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ResetPasswordActivity.this,MainActivity.class));
                           }else {
                               String taskError = task.getException().getMessage();
                               Toast.makeText(ResetPasswordActivity.this, taskError, Toast.LENGTH_SHORT).show();


                           }


                        }
                    });
                }

            }
        });


    }

    private void initViews(){
        myResetPasswordToolbar = findViewById(R.id.myResetPasswordToolbar);
        btn_resetPassword = findViewById(R.id.btn_resetPassword);
        editTxt_email_resetActivity = findViewById(R.id.editTxt_email_resetActivity);
    }

    private void setToolBar() {
        setSupportActionBar(myResetPasswordToolbar);
        getSupportActionBar().setTitle(R.string.title_reset_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
