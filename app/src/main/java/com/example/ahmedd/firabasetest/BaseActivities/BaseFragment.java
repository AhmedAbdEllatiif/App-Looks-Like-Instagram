package com.example.ahmedd.firabasetest.BaseActivities;


import android.content.Context;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;

public class BaseFragment extends Fragment {

    protected static final String AdMobID = "ca-app-pub-8926610076081506~2692840354";
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
