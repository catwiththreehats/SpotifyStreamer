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

import kaaes.spotify.webapi.android.models.Track;

public class TrackCustomAdapter extends BaseAdapter {

    private List<Track> trackList;
    private LayoutInflater layoutInflater;

    public class ViewHolder {
        public TextView textViewTrack;
        public TextView textViewAlbum;
        public ImageView imageView;
    }

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

        ViewHolder viewHolder;

        if (convertView==null) {
            convertView = layoutInflater.inflate(R.layout.list_item_song, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewTrack = (TextView)convertView.findViewById(R.id.listitem_top10_album_textView);
            viewHolder.textViewAlbum = (TextView)convertView.findViewById(R.id.listitem_top10_song_textView);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.listitem_top10_imageview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Track track = trackList.get(position);
        viewHolder.textViewTrack.setText(track.album.name);
        viewHolder.textViewAlbum.setText(track.name);
        if (track.album.images.size()>0) {
            Picasso.with(layoutInflater.getContext()).load(track.album.images.get(0).url).into(viewHolder.imageView);
        }

        return convertView;
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
