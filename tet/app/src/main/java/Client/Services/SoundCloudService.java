package Client.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Client.Contract.IService;
import Model.Playlist;

/**
 * Created by ramik on 1/10/2016.
 */
public class SoundCloudService implements IService{

    private ArrayList<Playlist> playlists;
    private ArrayList<Playlist> tempplaylists;

    public ArrayList<Playlist>  GeneratePlaylists(ArrayList<String> musicLikeList) throws ExecutionException, InterruptedException {

        playlists = new ArrayList<>();
        for (String musicLike:musicLikeList) {
            RetreivePlaylistTask getplaylistTask = new RetreivePlaylistTask();
            tempplaylists = getplaylistTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, musicLike).get();
            playlists.addAll(tempplaylists);
        }

        return playlists;
    }
}

class RetreivePlaylistTask extends AsyncTask<String,Void,ArrayList<Playlist>> {

    private final String LOG_TAG = RetreivePlaylistTask.class.getSimpleName();
    private URL url;
    private BufferedReader reader;
    private String playlistsJsonStr;
    ArrayList<Playlist> playlists;


    @Override
    protected void onPostExecute(ArrayList<Playlist> playlists) {
        super.onPostExecute(playlists);
    }

    @Override
    protected ArrayList<Playlist> doInBackground(String... params) {

        if(params.length == 0){
            return null;
        }

        HttpURLConnection urlConnection = null;
        String playlistJsonStr = null;

        final String SOUNDCLOUD_BASE_URL = "http://api.soundcloud.com/tracks?";
        final String QUERY_PARAM = "q";
        final String CLIENT_ID = "client_id";

        Uri queryURI = Uri.parse(SOUNDCLOUD_BASE_URL).buildUpon()
                .appendQueryParameter(CLIENT_ID, "7a0e2c3784b9c4ec4c4cf8f1f0267380")
                .appendQueryParameter(QUERY_PARAM, params[0])
                .build();


        try {
            url = new URL(queryURI.toString());


            Log.v(LOG_TAG, "Built URI " + queryURI.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            playlistsJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Playlists str" + playlistsJsonStr);
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the playlists data no need to parse.
            return null;
        }
        finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
    }

    try
    {
        getPlaylistDataFromJson(playlistsJsonStr);
        return playlists;
    }
    catch(JSONException e)
    {
        Log.e(LOG_TAG, e.getMessage(), e);
        e.printStackTrace();

    }
        return null;
    }

    private void getPlaylistDataFromJson(String playlistJsonStr) throws JSONException{
        JSONArray playlistArray = new JSONArray(playlistJsonStr);
        JSONObject playlist;
        playlists = new ArrayList<>();
        for (int i = 0; i<playlistArray.length(); i++) {

            playlist = playlistArray.getJSONObject(i);
            String artwork_url = playlist.getString("artwork_url");
            String title = playlist.getString("title");
            playlists.add(new Playlist(artwork_url,title));
        }
    }
}
