package com.example.photosapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.photosapp.model.Album;

public class AlbumsListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;

    //to store the list of albums
    private final Album[] albums;

    public AlbumsListAdapter(Activity context, Album[] albums) {

        super(context, R.layout.albumlist_rows, albums);

        this.context=context;
        this.albums = albums;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.albumlist_rows, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView albumsField = rowView.findViewById(R.id.albumNameField);

        //this code sets the values of the objects to values from the arrays
        albumsField.setText(albums[position].name);

        return rowView;

    };
}
