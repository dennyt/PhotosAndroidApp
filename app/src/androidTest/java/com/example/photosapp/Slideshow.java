package com.example.photosapp;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.photosapp.model.Album;
import com.example.photosapp.model.Photo;

import me.relex.circleindicator.CircleIndicator;

public class Slideshow extends AppCompatActivity {
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private SlideshowAdapter myPager;
    //private Button tagpersonbutton;
    //private Button taglocationbutton;
    public Album album;
    public Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slideshow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.slideshowtoolbar);
        setSupportActionBar(toolbar);

        photo = (Photo) getIntent().getParcelableExtra("photo");
        album = (Album) getIntent().getParcelableExtra("album");
        TextView tagsp = (TextView) findViewById(R.id.tagsboxp);
        TextView tagsl = (TextView) findViewById(R.id.tagsboxl);
        for (int i = 0; i < photo.locationtag.size(); i++) {
                tagsl.append("#" + photo.locationtag.get(i) + " ");
            }
        for (int i = 0; i < photo.persontag.size(); i++) {
                tagsp.append("#" + photo.persontag.get(i) + " ");
            }
        myPager = new SlideshowAdapter(this, album, photo);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(myPager);
        circleIndicator = findViewById(R.id.circle);
        circleIndicator.setViewPager(viewPager);
        //tagpersonbutton.findViewById(R.id.tagpersonbutton);


        final Button tagpersonbutton = findViewById(R.id.tagpersonbutton);
        tagpersonbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTagpDialog();
            }
        });

        final Button taglocationbutton = findViewById(R.id.taglocationbutton);
        taglocationbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTaglDialog();
            }
        });
    }


    private void showTagpDialog(){
        final Dialog dialog = new Dialog(Slideshow.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_tagp_dialog);

        final EditText tagEdittext=(EditText)dialog.findViewById(R.id.addtagp_edittext);
        final Button addButton=(Button)dialog.findViewById(R.id.addtagp_button);
        Button cancelButton=(Button)dialog.findViewById(R.id.canceladdtagp_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tagEdittext.getText().toString().equals("")) {
                    photo.persontag.add(tagEdittext.getText().toString());
                    TextView tagsp = (TextView) findViewById(R.id.tagsboxp);
                    tagsp.append("#" + tagEdittext.getText().toString() + " ");
                    dialog.dismiss();
                } else {
                    AlertDialog.Builder error =  new AlertDialog.Builder(Slideshow.this);
                    error.setTitle("Error!");
                    error.setMessage("You can't have an empty tag.");
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

    private void showTaglDialog(){
        final Dialog dialog = new Dialog(Slideshow.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_tagl_dialog);

        final EditText tagEdittext=(EditText)dialog.findViewById(R.id.addtagl_edittext);
        final Button addButton=(Button)dialog.findViewById(R.id.addtagl_button);
        Button cancelButton=(Button)dialog.findViewById(R.id.canceladdtagl_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tagEdittext.getText().toString().equals("")) {
                    photo.locationtag.add(tagEdittext.getText().toString());
                    TextView tagsp = (TextView) findViewById(R.id.tagsboxl);
                    tagsp.append("#" + tagEdittext.getText().toString() + " ");
                    dialog.dismiss();
                } else {
                    AlertDialog.Builder error =  new AlertDialog.Builder(Slideshow.this);
                    error.setTitle("Error!");
                    error.setMessage("You can't have an empty tag.");
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
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}