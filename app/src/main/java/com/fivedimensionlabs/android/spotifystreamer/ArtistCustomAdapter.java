package com.fivedimensionlabs.android.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

public class ArtistCustomAdapter extends BaseAdapter {
    List<Artist> artistList;
    LayoutInflater layoutInflater;

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
        View listItemView = layoutInflater.inflate(R.layout.list_item_artist, null);

        Artist artist = artistList.get(position);

        TextView textView = (TextView)listItemView.findViewById(R.id.listitem_artist_textView);
        textView.setText(artist.name);

        ImageView imageView = (ImageView)listItemView.findViewById(R.id.listitem_artist_imageview);

        if (artist.images.size()>0) {
            Picasso.with(layoutInflater.getContext()).load(artist.images.get(0).url).into(imageView);
        }

        return listItemView;
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
