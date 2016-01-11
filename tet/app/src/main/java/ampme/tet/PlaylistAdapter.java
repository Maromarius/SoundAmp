package ampme.tet;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Model.Playlist;

/**
 * Created by ramik on 1/11/2016.
 */
public class PlaylistAdapter extends ArrayAdapter<Playlist> {

    private ArrayList<Playlist> playlists;

    public PlaylistAdapter(Context context, int resource, ArrayList<Playlist> playlists) {
        super(context, resource, playlists);
        this.playlists = playlists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater vi = LayoutInflater.from((getContext()));
            v = vi.inflate(R.layout.list_item_playlists, null);

        }

        Playlist p = getItem(position);

        if(p != null){
            ImageView imageView = (ImageView) v.findViewById(R.id.playlist_coverArt);
            TextView textView = (TextView) v.findViewById(R.id.playlist_title);

            if(imageView != null){
                Picasso.with(getContext()).load(p.getCoverArtURL()).into(imageView);
            }
            if(textView != null){
                textView.setText(p.getTitle());
            }

        }
        return v;
    }

    @Override
    public void clear() {
        playlists.clear();
    }
}
