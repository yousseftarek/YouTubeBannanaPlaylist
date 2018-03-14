package Models;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Playlists {
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
