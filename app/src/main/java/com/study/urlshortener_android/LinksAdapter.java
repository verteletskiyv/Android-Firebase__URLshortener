package com.study.urlshortener_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.study.urlshortener_android.Models.Link;

import java.util.ArrayList;
import java.util.List;

public class LinksAdapter extends ArrayAdapter<Link> {

    private LayoutInflater layoutInflater;
    private List<Link> links;
    private ArrayList<String> allKeys;
    private int layoutListRow;
    private Context context;

    public LinksAdapter(@NonNull Context context, int resource, @NonNull List<Link> objects, ArrayList<String> allKeys) {
        super(context, resource, objects);
        links = objects;
        this.allKeys = allKeys;
        layoutListRow = resource;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = layoutInflater.inflate(layoutListRow, null);

        Link link = links.get(position);

        if (link != null) {
            final TextView shortLinkRow = convertView.findViewById(R.id.shortlink_row);

            if (shortLinkRow != null) {
                shortLinkRow.setText(link.getUrlShort());

                shortLinkRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent openLinksIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link.getUrlFull()));
                        context.startActivity(openLinksIntent);
                    }
                });

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://urlshortener-android-17eb1-default-rtdb.firebaseio.com/");
                final DatabaseReference table = database.getReference("Link");
                final ImageView btn_delete = convertView.findViewById(R.id.btn_delete);

                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = new AlertDialog.Builder(context)
                                .setTitle("Are you sure?")
                                        .setMessage("Would you like to delete this link?")
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        table.child(allKeys.get(position)).removeValue();
                                                    }
                                                })
                                .setNegativeButton("No", null)
                                .create();
                        dialog.show();
                    }
                });
            }
        }
        return convertView;
    }
}