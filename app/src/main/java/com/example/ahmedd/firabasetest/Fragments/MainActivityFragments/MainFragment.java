package com.example.ahmedd.firabasetest.Fragments.MainActivityFragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Adapters.MainPageAdapter;
import com.example.ahmedd.firabasetest.Fragments.HomeFragment;
import com.example.ahmedd.firabasetest.Fragments.MyPhotos;
import com.example.ahmedd.firabasetest.Fragments.UsersFragment;
import com.example.ahmedd.firabasetest.Helpers.MyViewPager;
import com.example.ahmedd.firabasetest.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    //Views
    private View view;
    private TextView txt_title;

    //Prepare Fragments
    private final Fragment fragment1 = new HomeFragment();
    private final Fragment fragment2 = new UsersFragment();
    private final Fragment fragment3 = new MyPhotos();
    private FragmentManager fm ;
    private Fragment active = fragment1;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getChildFragmentManager();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        initViews();

        setBottomNavigationView();



        return view;
    }

    private void initViews() {
        txt_title = view.findViewById(R.id.title);
    }







    /*******************************************************************************************************/
    //BottomNavigationView
    private void setBottomNavigationView() {
        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.mainBottom_nav);


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm.beginTransaction().add(R.id.mainFragment_view, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.mainFragment_view, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.mainFragment_view,fragment1, "1").commit();
        navigation.setSelectedItemId(R.id.home);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    //viewPager.setCurrentItem(0,false);
                    //fragment = new HomeFragment();
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    txt_title.setText("Home");
                    return true;
                case R.id.users:
                    //viewPager.setCurrentItem(1,false);
                    //fragment = new UsersFragment();
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    txt_title.setText("Users");
                    return true;
                case R.id.myPhotos:
                    //viewPager.setCurrentItem(2,false);
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    txt_title.setText("Photos");
                    return true;
            }
            return false;
        }
    };
    /*******************************************************************************************************/





    /*******************************************************************************************************/
/*    private void setUpViewPager(){
        MainPageAdapter pageAdapter = new MainPageAdapter(getChildFragmentManager(), PagerAdapter.POSITION_NONE);
        pageAdapter.addFragment(new HomeFragment());
        pageAdapter.addFragment(new UsersFragment());
        pageAdapter.addFragment(new MyPhotos());
        //pageAdapter.addFragment(new UsersFragment());
        //pageAdapter.addFragment(new MyPhotos());
        int limit = (pageAdapter.getCount() > 1 ? pageAdapter.getCount() - 1 : 1);
        viewPager.setOffscreenPageLimit(limit);
        viewPager.setAdapter(pageAdapter);
    }*/
    /*******************************************************************************************************/

}