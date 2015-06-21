package com.fivedimensionlabs.android.spotifystreamer;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fivedimensionlabs.android.spotifystreamer.adapters.ArtistCustomAdapter;
import com.fivedimensionlabs.android.spotifystreamer.helpers.NetworkUtility;

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

    public ArtistSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_artist_search, container, false);

        final SearchView searchText = (SearchView) rootView.findViewById(R.id.searchview_artist_search);

        searchText.setIconifiedByDefault(false);
        searchText.setQueryHint(getResources().getString(R.string.artist_search_hint));
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchString = searchText.getQuery().toString();
                if (NetworkUtility.hasNetworkConnectivity(getActivity())) {
                    if (fetchArtistsTask != null) {
                        fetchArtistsTask.cancel(true);
                    }
                    fetchArtistsTask = new FetchArtistResultsTask();
                    fetchArtistsTask.execute(searchString);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    artistsAdapter.clear();
                }
                return false;
            }
        });

        if (artistList == null)
        {
            artistList = new ArrayList<Artist>();
        }

        artistsAdapter = new ArtistCustomAdapter(getActivity(), artistList);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_artists);
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
            if (params == null || params.length == 0 || params[0].length() == 0) {
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

            if (artists == null || artists.size() == 0) {

                Toast toast = Toast.makeText(getActivity(), R.string.artist_search_no_results, Toast.LENGTH_SHORT);
                toast.show();

                return;
            }

            artistsAdapter.addRange(artists);
        }
    }
}
