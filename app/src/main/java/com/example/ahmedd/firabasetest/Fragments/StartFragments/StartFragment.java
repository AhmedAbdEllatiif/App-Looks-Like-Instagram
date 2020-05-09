package com.example.ahmedd.firabasetest.Fragments.StartFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ahmedd.firabasetest.Activities.MainActivity;
import com.example.ahmedd.firabasetest.R;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class StartFragment extends Fragment {

    private View view;

    private Button btn_login,btn_register;
    private LoginButton btn_FBLogin_start;
    private FirebaseUser firebaseUser;
    private CallbackManager callbackManager;

    @Override
    public void onStart() {
        super.onStart();

        //check if user is already logged in with firebase or facebook
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        /*if (firebaseUser != null || AccessToken.getCurrentAccessToken() != null){
            Log.e("start","not null");
            startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();
        }*/
        if (firebaseUser != null ){
            Log.e("start","not null");
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_start, container, false);

        initViews();

        MyOnclickListeners();

        /*callbackManager = CallbackManager.Factory.create();
        MyFacebook.Login(getActivity(),btn_FBLogin_start,callbackManager);*/
        return view;
    }

/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("898000400409074");
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        setContentView(R.layout.fragment_start);

        initViews();
        MyOnclickListeners();

        callbackManager = CallbackManager.Factory.create();
        MyFacebook.Login(this,btn_FBLogin_start,callbackManager);



    }*/

    private void initViews() {
        btn_login = view.findViewById(R.id.btn_login_start);
        btn_register = view.findViewById(R.id.btn_register_start);
        //btn_FBLogin_start = view.findViewById(R.id.btn_FBLogin_start);
    }

    private void MyOnclickListeners() {
        final View.OnClickListener openLoginFragment = Navigation.createNavigateOnClickListener(R.id.action_startFragment_to_loginFragment4);
        View.OnClickListener openRegisterFragment = Navigation.createNavigateOnClickListener(R.id.action_startFragment_to_registerFragment);

        btn_login.setOnClickListener(openLoginFragment);
        btn_register.setOnClickListener(openRegisterFragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


    }

}
