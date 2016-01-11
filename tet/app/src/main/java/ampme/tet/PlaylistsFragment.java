package ampme.tet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Client.Services.SoundCloudService;
import Model.Playlist;

/**
 * Created by ramik on 1/10/2016.
 */
public class PlaylistsFragment extends Fragment {

    private PlaylistAdapter mPlaylistAdapter;
    private ListView listView;
    private Button getMusicButton;
    private ArrayList<Playlist> playlists;
    private SoundCloudService soundCloudService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) rootView.findViewById(R.id.listview_playlists);
        soundCloudService = new SoundCloudService();
        getMusicButton = (Button) rootView.findViewById(R.id.button);
        playlists = new ArrayList<>();
        playlists.add(new Playlist("https://i1.sndcdn.com/artworks-000125441484-1kt12w-large.jpg", "TEST"));


        getMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlaylistAdapter.clear();
                Toast.makeText(getContext(), "Getting your Playlists...", Toast.LENGTH_SHORT).show();
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {
                                try {
                                    JSONObject userJson = object.getJSONObject("music");
                                    JSONArray musicArray = userJson.getJSONArray("data");
                                    ArrayList<String> musicLikesList = new ArrayList<>();
                                    for (int i = 0; i < musicArray.length(); i++) {
                                        userJson = musicArray.getJSONObject(i);
                                        musicLikesList.add(userJson.getString("name"));
                                    }
                                    playlists.addAll(soundCloudService.GeneratePlaylists(musicLikesList));
                                    mPlaylistAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "music");
                request.setParameters(parameters);
                request.executeAsync();
            }
        });

        mPlaylistAdapter = new PlaylistAdapter(getContext(), R.layout.list_item_playlists, playlists);
        listView.setAdapter(mPlaylistAdapter);
        return rootView;
    }

}

