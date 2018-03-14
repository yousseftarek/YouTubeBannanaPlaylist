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

    public static String deletePlaylistByID(int id, int subID){
        ArangoDB arangoDB = new ArangoDB.Builder().build();
        String dbName = "playlists";
        String collectionName = "firstPlaylist";

        ArrayList<Long> ids = new ArrayList<>();
        //Delete sub from Document
        //Case 1: not the only subscription
        BaseDocument myDocument2 = arangoDB.db(dbName).collection(collectionName).getDocument("" + id,
                BaseDocument.class);

        ids.addAll((ArrayList<Long>)myDocument2.getAttribute("id"));
        ids.remove(Long.valueOf(subID));
        myDocument2.updateAttribute("id",ids);
        arangoDB.db(dbName).collection(collectionName).deleteDocument("" + id);
        arangoDB.db(dbName).collection(collectionName).insertDocument(myDocument2);

        return true+"";
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
