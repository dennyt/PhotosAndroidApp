package com.example.photosapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photosapp.model.Album;
import com.example.photosapp.model.Photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class PhotosActivity extends AppCompatActivity {

    ArrayList<Album> albums;
    Album album;
    PhotosAdapter adapter;
    public static final int GET_FROM_GALLERY = 3;
    private Context context = this;

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photosbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photosgallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.photostoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        albums = getIntent().getParcelableArrayListExtra("list");
        int i = (int) getIntent().getIntExtra("position",0);
        album = albums.get(i);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.photosgallery);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        //album.photos = prepareData();
        adapter = new PhotosAdapter(getApplicationContext(), (ArrayList<Photo>) album.photos, albums);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchbytag:

                /*final Dialog dialog = new Dialog(PhotosActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_dialog);

                final EditText addEdittext=(EditText)dialog.findViewById(R.id.addalbum_edittext);
                final Button addButton=(Button)dialog.findViewById(R.id.addalbum_button);
                Button cancelButton=(Button)dialog.findViewById(R.id.canceladdalbum_button);

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!addEdittext.getText().toString().equals("")) {
                            list.add(new Album(addEdittext.getText().toString()));
                            dialog.dismiss();
                        } else {
                            AlertDialog.Builder error =  new AlertDialog.Builder(MainActivity.this);
                            error.setTitle("Error!");
                            error.setMessage("You can't have an album name that's blank.");
                            error.setNeutralButton("OK",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            error.show();
                        }
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                return true;*/

            case R.id.addphoto:

                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    //private ArrayList<Photo> prepareData(){ //in the future will update so that it just loads from album's photos
                                            // also replace image title with tags
        /*ArrayList<Photo> theimage = new ArrayList<>();
        for(int i = 0; i< image_titles.length; i++){
            Photo createList = new Photo();
            createList.setImage_title(image_titles[i]);
            createList.setBitmap(image_ids[i]);
            createList.tag.add(image_titles[i]);
            theimage.add(createList);
        }*/
        //return theimage;
    //}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            File file = new File(selectedImage.getPath());
            final String[] split = file.getPath().split(":");
            String fileName = file.getName();
            Photo photo = new Photo();
                photo.setUri(selectedImage);
                photo.setImage_title(fileName);
                album.photos.add(photo);

                store();
                adapter.update(album);
        }
    }

    public void store(){
        try {
            FileOutputStream fos = context.openFileOutput(MainActivity.savefile, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(MainActivity.list);
            os.close();
            fos.close();

            File f = new File(getFilesDir(),MainActivity.savefile);
            if(f.exists()){
                System.out.println("STORED FILE");
            }else{
                System.out.println("DID NOT STORE FILE");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
