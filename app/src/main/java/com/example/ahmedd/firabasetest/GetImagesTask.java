package com.example.ahmedd.firabasetest;

import com.example.ahmedd.firabasetest.Model.Photos;

import java.util.List;

public interface GetImagesTask {


    void onGettingImagesCompleted(List<Photos> imgList);

}
