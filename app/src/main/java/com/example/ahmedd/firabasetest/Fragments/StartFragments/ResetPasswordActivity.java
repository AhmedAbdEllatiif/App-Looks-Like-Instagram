package com.example.ahmedd.firabasetest.Fragments.StartFragments;

import androidx.annotation.NonNull;

import com.example.ahmedd.firabasetest.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ResetPasswordActivity extends AppCompatActivity {


    private static final String TAG = "ResetPasswordActivity";


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
        String emailFromIntent = getIntent().getStringExtra("Email");
        Log.e("Email","emailFromIntent");
       // editTxt_email_resetActivity.setText(emailFromIntent);
        editTxt_email_resetActivity.requestFocus();

        btn_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String emailFromIntent = getIntent().getStringExtra("Email");
                Log.e("emailFromIntent","emailFromIntent");
                //editTxt_email_resetActivity.setText(emailFromIntent);

                if (editTxt_email_resetActivity.getText().toString().trim().equals("")){
                    editTxt_email_resetActivity.setError(getString(R.string.enter_mail));
                }else {
                    EMAIL = editTxt_email_resetActivity.getText().toString().trim();
                    MyFireBase.getAuth().sendPasswordResetEmail(EMAIL).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this, "Check your mail", Toast.LENGTH_SHORT).show();
                               Snackbar.make(v, "please, Check your mail", Snackbar.LENGTH_LONG)
                                       .setAction("Action", null).show();
                               editTxt_email_resetActivity.requestFocus();
                           }else {
                               String taskError = task.getException().getMessage();
                               Toast.makeText(ResetPasswordActivity.this, taskError, Toast.LENGTH_SHORT).show();
                                if (taskError != null){
                                    Log.e(TAG, "onComplete: " + taskError );
                                }
                                editTxt_email_resetActivity.requestFocus();
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
