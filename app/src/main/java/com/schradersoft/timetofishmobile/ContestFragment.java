package com.schradersoft.timetofishmobile;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.schradersoft.timetofishmobile.core.Api;
import com.schradersoft.timetofishmobile.domain.models.Contestant;
import com.schradersoft.timetofishmobile.domain.models.Photo;
import com.schradersoft.timetofishmobile.loaders.ContestantLoader;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContestFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<List<Contestant>> {

    ListView _contestantList;
    ArrayAdapter<Contestant> _contestantAdapter;
    Hashtable<String, Bitmap> _imageCache;

    public ContestFragment() {
        _imageCache = new Hashtable<>();
    }

    @Override
    public void onActivityCreated (final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _contestantAdapter = new ArrayAdapter<Contestant>(getActivity(), R.layout.contestant_list_item) {
            @Override
            public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View adapterView = convertView;
                if (adapterView == null) {
                    adapterView = getLayoutInflater(savedInstanceState).inflate(R.layout.contestant_list_item, parent, false);
                }

                ImageView picView = (ImageView) adapterView.findViewById(R.id.contestant_profile_picture);
                TextView nameView = (TextView) adapterView.findViewById(R.id.contestant_profile_username);
                TextView scoreView = (TextView) adapterView.findViewById(R.id.contestant_profile_score);

                Contestant contestant = _contestantAdapter.getItem(position);

                String url = contestant.getAvatarUri();
                if (!_imageCache.containsKey(url)) {
                    Photo.PhotoDownloader imageLoader = new Photo.PhotoDownloader(url, picView, getContext());
                    imageLoader.execute(_imageCache);
                } else {
                    picView.setImageBitmap(_imageCache.get(url));
                }

                nameView.setText(contestant.getUsername());
                scoreView.setText(contestant.getScoreText());

                return adapterView;
            }
        };

        _contestantList = (ListView) getActivity().findViewById(R.id.contestant_list);
        _contestantList.setAdapter(_contestantAdapter);

        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contest, container, false);
    }

    @Override
    public Loader<List<Contestant>> onCreateLoader(int id, Bundle args) {
        Loader<List<Contestant>> loader = new ContestantLoader(getActivity());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Contestant>> loader, List<Contestant> data) {
        _contestantAdapter.clear();
        _contestantAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Contestant>> loader) {
        _contestantAdapter.clear();
    }
}
