package com.fivedimensionlabs.android.spotifystreamer;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


public class ArtistSearchFragment extends Fragment {

    private final String LOG_TAG = ArtistSearchFragment.class.getSimpleName();

    private ArtistCustomAdapter artistsAdapter;

    private static List<Artist> artistList;

    private FetchArtistResultsTask fetchArtistsTask;

    private EditText editTextSearchArtist;

    private static String lastSearchString = "";

    public ArtistSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        editTextSearchArtist = (EditText)rootView.findViewById(R.id.edittext_artist_search);

        editTextSearchArtist.setText(lastSearchString);

        editTextSearchArtist.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            String searchString = v.getText().toString();
                            if (fetchArtistsTask!=null) {
                                fetchArtistsTask.cancel(true);
                            }
                            fetchArtistsTask = new FetchArtistResultsTask();
                            fetchArtistsTask.execute(searchString);
                            lastSearchString = searchString;
                        }
                        return false;
                    }
                });

        if (artistList==null) {
            artistList = new ArrayList<Artist>();
        }

        artistsAdapter = new ArtistCustomAdapter(getActivity(), artistList);

        ListView listView = (ListView)rootView.findViewById(R.id.listview_artists);
        listView.setAdapter(artistsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = (Artist) artistsAdapter.getItem(position);
                Intent artistTopTenIntent = new Intent(getActivity(), ArtistTop10Activity.class);
                artistTopTenIntent.putExtra("artistId", artist.id);
                artistTopTenIntent.putExtra("artistName", artist.name);
                getActivity().startActivity(artistTopTenIntent);
            }
        });

        return rootView;
    }

    public class FetchArtistResultsTask extends AsyncTask<String, Void, List<Artist>> {

        private Boolean errorFetchingData = false;

        @Override
        protected List<Artist> doInBackground(String... params) {
            if (params==null || params.length==0 || params[0].length()==0) {
                return null;
            }

            SpotifyApi spotifyApi = new SpotifyApi();

            SpotifyService spotify = spotifyApi.getService();
            try {
                ArtistsPager artistPager = spotify.searchArtists(params[0]);
                return artistPager.artists.items;
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Error fetching Spotify data: " + ex.getMessage());
                errorFetchingData = true;
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {

            artistsAdapter.clear();

            if (errorFetchingData) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_fetching_spotify_data, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            if (artists==null || artists.size()==0) {

                Toast toast = Toast.makeText(getActivity(), R.string.artist_search_no_results, Toast.LENGTH_SHORT);
                toast.show();

                return;
            }

            artistsAdapter.addRange(artists);
        }
    }
}
