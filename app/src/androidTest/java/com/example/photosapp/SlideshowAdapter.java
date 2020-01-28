package com.example.photosapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.photosapp.model.Album;
import com.example.photosapp.model.Photo;

import java.io.IOException;

public class SlideshowAdapter extends PagerAdapter {
        private Context context;
        private Album album;
        private Photo photo;

        public SlideshowAdapter(Context context, Album album, Photo photo) {
            this.context = context;
            this.album = album;
            this.photo = photo;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.viewpager_photo, null);
            ImageView imageView = view.findViewById(R.id.image);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photo.getUri());
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageBitmap(bitmap);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }


        @Override
        public int getCount() {
           return album.getSize();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

    }
