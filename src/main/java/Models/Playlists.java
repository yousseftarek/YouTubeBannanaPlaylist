package Models;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Playlists {

    final static String dbName = "playlists";
    final static String collectionName = "firstPlaylist";

    //returns  a specific playlist by id
    public static String getPlaylistById(int id) {
        ArangoDB arangoDB = new ArangoDB.Builder().build();
        JSONObject playlistObject = new JSONObject();

        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionName).getDocument("" + id,
                    BaseDocument.class);
            playlistObject.put("id", myDocument.getId());
            playlistObject.put("title", myDocument.getAttribute("title"));
            playlistObject.put("channel_id", myDocument.getAttribute("channel_id"));
            playlistObject.put("description", myDocument.getAttribute("description"));
            playlistObject.put("views_count", myDocument.getAttribute("views_count"));
            playlistObject.put("videos_count", myDocument.getAttribute("videos_count"));
            playlistObject.put("videos", myDocument.getAttribute("videos"));
            playlistObject.put("created_on", myDocument.getAttribute("created_on"));
            playlistObject.put("last_updated_on", myDocument.getAttribute("last_updated_on"));
            playlistObject.put("privacy", myDocument.getAttribute("privacy"));
            playlistObject.put("playlist_type", myDocument.getAttribute("playlist_type"));

        } catch (ArangoDBException e) {
            e.printStackTrace();
        } finally {
            //arangoDB.shutdown();
        }

        return playlistObject.toString();



    }

    public static String deletePlaylistByID ( int id){
        ArangoDB arangoDB = new ArangoDB.Builder().build();
        String dbName = "playlists";
        String collectionName = "firstPlaylist";

        ArrayList<Long> ids = new ArrayList<>();
        //Delete sub from Document
        //Case 1: not the only subscription
        BaseDocument myDocument2 = arangoDB.db(dbName).collection(collectionName).getDocument("" + id,
                BaseDocument.class);

        ids.addAll((ArrayList<Long>) myDocument2.getAttribute("id"));
        myDocument2.updateAttribute("id", ids);
        arangoDB.db(dbName).collection(collectionName).deleteDocument("" + id);
        arangoDB.db(dbName).collection(collectionName).insertDocument(myDocument2);

        return true + "";
    }
}

/*
"Playlist"​ :{
    "id":​ _id​ ,
    "title": ​ String​ ,
    "channel_id": ​ _id​ ,
    "description": ​ String​ ,
    "views_count": ​ Number​ ,
    "videos_count": ​ Number​ ,
    "videos": [{ "id":​ Number​ , "title": ​ String​ , "duration": ​ Number​ }],
    "created_on": { "type": ​ Date​ , "default": ​ Date.now​ },
    "last_updated_on": { "type": ​ Date​ , "default": ​ Date.now​ },
    "privacy": ​ enum​ ,
    "playlist_type": ​ enum
}
*/
