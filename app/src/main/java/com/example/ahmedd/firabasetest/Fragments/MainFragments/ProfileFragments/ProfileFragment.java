package com.example.ahmedd.firabasetest.Fragments.MainFragments.ProfileFragments;


import android.os.Bundle;


import com.bumptech.glide.Glide;
import com.example.ahmedd.firabasetest.Adapters.ProfileFragmentViewPagerAdapter;

import com.example.ahmedd.firabasetest.ViewModel.MainActivityViewModel;


import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import com.example.ahmedd.firabasetest.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private MainActivityViewModel viewModel;

    private View view = null;


    private TextView txt_empty_cardView;
    private TabLayout tabLayout;

    //User data view
    private CircleImageView profile_img;
    private TextView txt_user_name;


    //ViewPager
    private ViewPager viewpager_profile;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.profile_collapsing_toolbar, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        //To initialize views
        initViews();

        //To handle when views clicked
        onViewClicked();

        //To setup viewPager
        setupViewPager();

        //To setup TabLayout
        setupTabLayout();

        //To observe image from liveData
        observeImagesFromLiveData();

        //To observe current user data from liveData
        observeCurrentUserData();


        return view;
    }


    /**
     * To initialize views
     */
    private void initViews() {
        txt_empty_cardView = view.findViewById(R.id.txt_empty_cardView);
        //recyclerView = view.findViewById(R.id.recyclerView_photos);
        tabLayout = view.findViewById(R.id.myImages_tabLayout);
        profile_img = view.findViewById(R.id.profile_img);
        txt_user_name = view.findViewById(R.id.user_name);
        viewpager_profile = view.findViewById(R.id.viewpager_profile);


    }


    /**
     * To setup viewPager
     * */
    private void setupViewPager() {
        ProfileFragmentViewPagerAdapter adapter
                = new ProfileFragmentViewPagerAdapter(getChildFragmentManager(), PagerAdapter.POSITION_NONE);
        adapter.addFragment(new CurrentUserImagesFragment());
        adapter.addFragment(new TaggedImagesFragment());
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        viewpager_profile.setAdapter(adapter);
        viewpager_profile.setOffscreenPageLimit(limit);
    }


    /**
     * To setup tabLayout
     * */
    private void setupTabLayout(){
        tabLayout.setupWithViewPager(viewpager_profile);
        tabLayout.setTabTextColors(getResources().getColor(R.color.black, null),
                getResources().getColor(R.color.colorPrimary, null));
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_grid);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_menu_gallery);
    }


    /**
     * on ViewClicked
     */
    private void onViewClicked() {
        txt_empty_cardView.setOnClickListener(v -> Log.e(TAG, "onClick ==> txt_empty_cardView"));
    }

    /**
     * To observe the liveData
     */
    private void observeImagesFromLiveData() {

        viewModel.getMyPhotosFragmentImages().observe(getViewLifecycleOwner(), photos ->{
            /*Log.e(TAG, "observeImagesFromLiveData: photos size ==> " + photos.size() );
            Log.e(TAG, "observeImagesFromLiveData: isPhotosEmpty ==> " + photos.isEmpty() );*/
            ShowTextPickImage(photos.isEmpty());
        });
    }


    /**
     * To observer current user data
     */
    private void observeCurrentUserData() {
        viewModel.getCurrentUserData().observe(getViewLifecycleOwner(), user -> {
            String userName = user.getUserName();
            String userImg = user.getImageURL();

            //To fill the view with user data
            setUserDataToViews(userName, userImg);
        });
    }


    /**
     * To fill views with current user data
     */
    private void setUserDataToViews(String name, String img) {
        txt_user_name.setText(name);
        if (isUserHasNoImage(img)) {
            profile_img.setImageResource(R.mipmap.ic_launcher);
        } else {

            Glide.with(requireActivity())
                    .load(img)
                    .into(profile_img);
        }
    }

    /**
     * To check if user has Profile image or not
     * return true if user doesn't have profile img
     */
    private boolean isUserHasNoImage(String img) {
        return img.equals(getString(R.string.txt_default));
    }


    /**
     * Show text (Pick a new photo) if the imagesList is empty
     */
    private void ShowTextPickImage(boolean isPhotosEmpty) {
        if (isPhotosEmpty) {
            txt_empty_cardView.setVisibility(View.VISIBLE);
            viewpager_profile.setVisibility(View.GONE);
        } else {
            txt_empty_cardView.setVisibility(View.GONE);
            viewpager_profile.setVisibility(View.VISIBLE);
        }

    }


}