package com.study.urlshortener_android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class LinksAdapter extends ArrayAdapter<Link> {

    private LayoutInflater layoutInflater;
    private List<Link> links;
    private int layoutListRow;
    private Context context;

    public LinksAdapter(@NonNull Context context, int resource, @NonNull List<Link> objects) {
        super(context, resource, objects);
        links = objects;
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

            }
        }
        return convertView;
    }
}