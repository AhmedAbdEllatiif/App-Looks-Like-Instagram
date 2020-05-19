package com.example.ahmedd.firabasetest.Model;

public class SearchByImagesModel {

        private String imageUrl0;
        private String imageUrl1;
        private String imageUrl2;


        public SearchByImagesModel() {}

        public SearchByImagesModel(String imageUrl0, String imageUrl1, String imageUrl2) {
            this.imageUrl0 = imageUrl0;
            this.imageUrl1 = imageUrl1;
            this.imageUrl2 = imageUrl2;
        }

        public String getImageUrl0() {
            return imageUrl0;
        }

        public void setImageUrl0(String imageUrl0) {
            this.imageUrl0 = imageUrl0;
        }

        public String getImageUrl1() {
            return imageUrl1;
        }

        public void setImageUrl1(String imageUrl1) {
            this.imageUrl1 = imageUrl1;
        }

        public String getImageUrl2() {
            return imageUrl2;
        }

        public void setImageUrl2(String imageUrl2) {
            this.imageUrl2 = imageUrl2;
        }
}