package com.example.ahmedd.firabasetest.BaseActivities;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class BaseActivity extends AppCompatActivity {

    protected static final String AdMobID = "ca-app-pub-8926610076081506~2692840354";

    protected MaterialDialog dialog;
    protected AppCompatActivity activity;
    public static final String sharedPreferencesName = "HOLY_QURAN";

    public  BaseActivity (){
        activity = this;
    }


    public MaterialDialog showMessage(String title, String message){

        dialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .positiveText("Dismiss")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
        return dialog;

    }


    public MaterialDialog showProgressBar (String title, String message ){
        dialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .cancelable(false)
                .show();
        return dialog;
    }


    public void hideProgressBar(){

        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();

        }
    }


    public void saveString(String key , String value){

        SharedPreferences.Editor editor =
                getSharedPreferences(sharedPreferencesName, MODE_PRIVATE).edit();

        editor.putString(key,value);
        editor.apply();

    }


    public String getString (String key, String defaultValue){

        SharedPreferences sharedPreferences =
                getSharedPreferences(sharedPreferencesName,MODE_PRIVATE);

        return sharedPreferences.getString(key,defaultValue);
    }

}
