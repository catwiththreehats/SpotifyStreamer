package com.fivedimensionlabs.android.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fivedimensionlabs.android.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

public class ArtistCustomAdapter extends BaseAdapter {
    private List<Artist> artistList;
    private LayoutInflater layoutInflater;

    public class ViewHolder {
        public TextView textView;
        public ImageView imageView;
    }

    public ArtistCustomAdapter(Context context, List<Artist> artists) {
        artistList = artists;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return artistList.size();
    }

    @Override
    public Object getItem(int position) {
        return artistList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView==null) {
            convertView = layoutInflater.inflate(R.layout.list_item_artist, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView)convertView.findViewById(R.id.listitem_artist_textView);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.listitem_artist_imageview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Artist artist = artistList.get(position);
        viewHolder.textView.setText(artist.name);

        if (artist.images.size() > 0) {
            Picasso.with(layoutInflater.getContext()).load(artist.images.get(0).url).into(viewHolder.imageView);
        }

        return convertView;
    }

    public void clear() {
        artistList.clear();
        super.notifyDataSetChanged();
    }

    public void add(Artist artist) {
        artistList.add(artist);
        super.notifyDataSetChanged();
    }

    public void addRange(List<Artist> artists) {
        for (Artist artist: artists) {
            artistList.add(artist);
        }
        super.notifyDataSetChanged();
    }
}
