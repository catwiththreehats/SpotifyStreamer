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
import kaaes.spotify.webapi.android.models.Track;

public class TrackCustomAdapter extends BaseAdapter {

    private List<Track> trackList;
    LayoutInflater layoutInflater;

    public TrackCustomAdapter(Context context, List<Track> tracks) {
        trackList = tracks;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return trackList.size();
    }

    @Override
    public Object getItem(int position) {
        return trackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = layoutInflater.inflate(R.layout.list_item_song, null);

        Track track = trackList.get(position);

        TextView textViewTrack = (TextView)listItemView.findViewById(R.id.listitem_top10_album_textView);
        textViewTrack.setText(track.album.name);

        TextView textViewAlbum = (TextView)listItemView.findViewById(R.id.listitem_top10_song_textView);
        textViewAlbum.setText(track.name);

        ImageView imageView = (ImageView)listItemView.findViewById(R.id.listitem_top10_imageview);

        if (track.album.images.size()>0) {
            Picasso.with(layoutInflater.getContext()).load(track.album.images.get(0).url).into(imageView);
        }

        return listItemView;
    }

    public void clear() {
        trackList.clear();
        super.notifyDataSetChanged();
    }

    public void add(Track track) {
        trackList.add(track);
        super.notifyDataSetChanged();
    }

    public void addRange(List<Track> tracks) {
        for (Track track: tracks) {
            trackList.add(track);
        }
        super.notifyDataSetChanged();
    }
}
