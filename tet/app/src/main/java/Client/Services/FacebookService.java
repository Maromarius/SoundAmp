package Client.Services;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Client.Contract.IService;

/**
 * Created by ramik on 1/10/2016.
 */
public class FacebookService implements IService{

    private void setMusicLikesList(ArrayList<String> musicLikesList) {
        this.musicLikesList = musicLikesList;
    }

    public ArrayList<String> getMusicLikesList() {
        return musicLikesList;
    }

    private ArrayList<String> musicLikesList;

    public void GetMusicLikes(){

        musicLikesList = new ArrayList<>();

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        try {
                            JSONObject user = object.getJSONObject("music");
                            JSONArray js = user.getJSONArray("data");
                            for(int i = 0 ; i < js.length() ; i++) {
                                user = js.getJSONObject(i);
                                musicLikesList.add(user.getString("name"));
                            }
                            setMusicLikesList(musicLikesList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "music");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
