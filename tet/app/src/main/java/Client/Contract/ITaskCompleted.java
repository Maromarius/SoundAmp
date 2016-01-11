package Client.Contract;

import java.util.ArrayList;

import Model.Playlist;

/**
 * Created by ramik on 1/11/2016.
 */
public interface ITaskCompleted {

     void onTaskComplete(ArrayList<Playlist> result);
}
