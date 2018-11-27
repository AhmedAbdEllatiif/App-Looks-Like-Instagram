package com.example.ahmedd.firabasetest.BaseActivities;

import android.content.Context;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;

public class BaseDialogFragment extends DialogFragment {

    protected BaseActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BaseActivity) context;


    }

    public MaterialDialog showMessage(String title, String Message){
        return  activity.showMessage(title,Message);
    }

    public MaterialDialog showProgressBar(String title,String messge){
        return activity.showProgressBar(title, messge);
    }

    public void hideProgressBar(){

        activity.hideProgressBar();
    }
}
