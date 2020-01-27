package com.example.photosapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.recyclerview.widget.RecyclerView;
import com.example.photosapp.model.Album;
import com.example.photosapp.model.Photo;

import java.io.IOException;
import java.util.ArrayList;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
        private ArrayList<Photo> galleryList;
        private Context context;
        private Album album;
        private ArrayList<Album> albums;

        public Album getAlbum () {
            return album;
        }

        public PhotosAdapter(Context context, ArrayList<Photo> galleryList, ArrayList<Album> albums) {
            this.galleryList = galleryList;
            this.context = context;
            this.albums = albums;
        }

        @Override
        public PhotosAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photogridlayout, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotosAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.title.setText(galleryList.get(i).getImage_title());
            viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), galleryList.get(i).getUri());
            } catch (IOException e) {
                e.printStackTrace();
            }
            viewHolder.img.setImageBitmap(bitmap);

            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    final AlertDialog.Builder confirm = new AlertDialog.Builder(v.getContext());
                    final AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
                    String[] options = {"Open", "Delete", "Move"};
                    builder.setTitle("What would you like to do?");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent intent = new Intent(v.getContext(), Slideshow.class);
                                Photo photo = galleryList.get(i);
                                intent.putExtra("photo",(Parcelable)photo);
                                intent.putExtra("album",(Parcelable) album);
                                v.getContext().startActivity(intent);

                            } else if (which == 1) {
                                //delete album
                                confirm.setTitle("Delete Album?");
                                confirm.setMessage("Are you sure you want to delete this photo?");
                                confirm.setNegativeButton("Cancel", null);
                                confirm.setPositiveButton("Confirm", new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        updateData(i);
                                    }
                                });
                                confirm.show();
                            }
                            else if (which == 2) {
                                b.setTitle("Select Album");
                                String[] types = arrayofAlbumsNames(albums);
                                b.setItems(types, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        albums.get(which).photos.add(galleryList.get(i));
                                        dialog.dismiss();
                                    }
                                });
                                b.show();
                            }
                        }
                    });
                    builder.show();
                }
            });
        }


        @Override
        public int getItemCount() {
            return galleryList.size();
        }

    public void updateData(int position) {
        galleryList.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, getItemCount()-1);
        this.notifyDataSetChanged();
    }

    public void update(Album album) {
            this.album = album;
            this.galleryList = album.photos;
            this.notifyDataSetChanged();
    }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView title;
            private ImageView img;
            public ViewHolder(View view) {
                super(view);

                title = (TextView)view.findViewById(R.id.title);
                img = (ImageView) view.findViewById(R.id.img);
            }
        }

            public Photo getItem(int position) {
                return galleryList.get(position);
            }

            public String[] arrayofAlbumsNames (ArrayList<Album> albums) {
                String[] names = new String [albums.size()];
                for (int i = 0; i < albums.size(); i++) {
                    names[i] = albums.get(i).name;
                }

                return names;
            }

    }
