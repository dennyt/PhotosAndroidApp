package com.example.photosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.photosapp.model.Album;
import com.example.photosapp.model.Photo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_PERMISSION_KEY = 1;

    public static final String EXTRA_MESSAGE = "I hate my life.";

    ListView listView;

    public static ArrayList<Album> list = new ArrayList<Album>();

    private Context context = this;

    public static String savefile = "album.ser";

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        File f = new File(getFilesDir(), savefile);
        if(f.exists()) {
            try {
                FileInputStream fis = context.openFileInput(savefile);
                ObjectInputStream is = new ObjectInputStream(fis);
                list = (ArrayList<Album>) is.readObject();
                is.close();
                fis.close();
            } catch (Exception i) {
                i.printStackTrace();
                return;
            }
        }

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        final ArrayAdapter<Album> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        ListView listView = findViewById(R.id.AlbumsList);
        listView.setAdapter(itemsAdapter);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder confirm = new AlertDialog.Builder(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final Album album = itemsAdapter.getItem(position);
                String[] options = {"Open", "Rename", "Delete"};

                builder.setTitle("What would you like to do?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            openPhotos(position);
                        } else if (which == 1) { //rename album
                            showEditDialog(position, itemsAdapter);
                        } else { //delete album
                            confirm.setTitle("Delete Album?");
                            confirm.setMessage("Are you sure you want to delete " + list.get(position).name + "?");
                            confirm.setNegativeButton("Cancel", null);
                            confirm.setPositiveButton("Confirm", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    list.remove(position);
                                    itemsAdapter.notifyDataSetChanged();
                                    store();
                                }
                            });
                            confirm.show();
                        }
                    }
                });

                builder.show();
            }
        });

    }


    private void showEditDialog(final int position, final ArrayAdapter<Album> itemsAdapter){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_dialog);


        final EditText renameEdittext=(EditText)dialog.findViewById(R.id.rename_edittext);
        final Button renameButton=(Button)dialog.findViewById(R.id.rename_button);
        Button cancelButton=(Button)dialog.findViewById(R.id.cancel_button);


        renameEdittext.setText(list.get(position).name);

        //When rename button is clicked, first rename edittext should be checked if it is empty
        //If it is not empty, data and listview item should be changed.
        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!renameEdittext.getText().toString().equals("")) {
                    list.get(position).setName(renameEdittext.getText().toString());
                    itemsAdapter.notifyDataSetChanged();
                    store();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:

                final Dialog dialog = new Dialog(MainActivity.this);
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
                            store();
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

                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void openPhotos(int i) {
        Intent intent = new Intent(this, PhotosActivity.class);
        intent.putParcelableArrayListExtra("list",list);
        intent.putExtra("position",i);
        startActivity(intent);
    }

    public void store(){
        try {
            FileOutputStream fos = context.openFileOutput(savefile, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(list);
            os.close();
            fos.close();

            File f = new File(getFilesDir(),savefile);
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
