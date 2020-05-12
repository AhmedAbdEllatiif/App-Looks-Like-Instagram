package com.example.ahmedd.firabasetest.Fragments.MainFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmedd.firabasetest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserActivityFragment extends Fragment {

    public UserActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uset_activiy, container, false);
    }
}
