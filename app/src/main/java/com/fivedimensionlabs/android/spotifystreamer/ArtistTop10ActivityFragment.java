package com.fivedimensionlabs.android.spotifystreamer;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


public class ArtistTop10ActivityFragment extends Fragment {

    private final String LOG_TAG = ArtistTop10ActivityFragment.class.getSimpleName();

    private TrackCustomAdapter topTenAdapter;
    private static List<Track> trackList;
    private static String lastArtistId;

    public ArtistTop10ActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artist_top10, container, false);

        String artistId = getActivity().getIntent().getStringExtra("artistId");

        if (trackList==null) {
            trackList = new ArrayList<Track>();
        }

        topTenAdapter = new TrackCustomAdapter(getActivity(), trackList);

        ListView listView = (ListView)rootView.findViewById(R.id.listview_artists_top10);
        listView.setAdapter(topTenAdapter);

        if (artistId!=lastArtistId) {
            new FetchTop10ResultsTask().execute(artistId);
            lastArtistId=artistId;
        }
        return rootView;
    }

    public class FetchTop10ResultsTask extends AsyncTask<String, Void, List<Track>> {

        private Boolean errorFetchingData = false;

        @Override
        protected List<Track> doInBackground(String... params) {
            if (params==null || params.length==0 || params[0].length()==0) {
                return null;
            }

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotify = spotifyApi.getService();

            Map<String, Object> map = new HashMap<>();
            map.put("country", "US");

            try {
                Tracks topTracks = spotify.getArtistTopTrack(params[0], map);
                return topTracks.tracks;
            } catch (Exception ex) {
                lastArtistId = "";
                Log.e(LOG_TAG, "Error fetching Spotify data: " + ex.getMessage());
                errorFetchingData = true;
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Track> tracks) {

            topTenAdapter.clear();

            if (errorFetchingData) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_fetching_spotify_data, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            if (tracks==null || tracks.size()==0) {
                Toast toast = Toast.makeText(getActivity(), R.string.topten_tracks_no_result, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            topTenAdapter.addRange(tracks);
        }
    }

}
