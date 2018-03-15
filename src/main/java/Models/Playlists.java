package Models;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

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


    public static String deletePlaylistByID(int id) {
        ArangoDB arangoDB = new ArangoDB.Builder().build();
        String dbName = "playlists";
        String collectionName = "firstPlaylist";

        ArrayList<Long> ids = new ArrayList<>();
        BaseDocument myDocument2 = arangoDB.db(dbName).collection(collectionName).getDocument("" + id,
                BaseDocument.class);

        ids.addAll((ArrayList<Long>) myDocument2.getAttribute("id"));
        myDocument2.updateAttribute("id", ids);
        try {
            arangoDB.db(dbName).collection(collectionName).deleteDocument("" + id);
        } catch (final ArangoDBException e) {
            return "Failed to delete document";
        }

        return "Successfully deleted document.";
    }

    /**
     * Add/Update a playlist in the database based on validity of input id
     *
     * @param id
     * @param title
     * @param channel_id
     * @param description
     * @param views_count
     * @param videos_count
     * @param privacy
     * @param playlist_type
     * @param videos
     * @return
     */
    public static String postPlaylistByID(int id, String title, int channel_id, String description, int views_count, int videos_count, String privacy, String playlist_type, JSONArray videos) {
        ArangoDB arangoDB = new ArangoDB.Builder().build();
        //Check if document whit id exist
        if (arangoDB.db(dbName).collection(collectionName).getDocument("" + id,
                BaseDocument.class) == null) {
            //It doesn't exist, create and add
            //Create a new document
            BaseDocument myObject = new BaseDocument();
            //Add attributes
            myObject.setKey(id + "");
            myObject.addAttribute("videos", videos);
            myObject.addAttribute("title", title);
            myObject.addAttribute("channel_id", channel_id);
            myObject.addAttribute("description", description);
            myObject.addAttribute("views_count", views_count);
            myObject.addAttribute("videos_count", videos_count);
            myObject.addAttribute("privacy", privacy);
            myObject.addAttribute("playlist_type", playlist_type);
            String dateNow = getDateNowISO();
            myObject.addAttribute("created_on", dateNow);
            myObject.addAttribute("last_updated", dateNow);
            //Try adding to database
            try {
                arangoDB.db(dbName).collection(collectionName).insertDocument(myObject);
                System.out.println("Document created");
            } catch (ArangoDBException e) {
                System.err.println("Failed to create document. " + e.getMessage());
            }
            return myObject.toString();
        } else {
            //Document already exist so get it, update it and re-insert it
            BaseDocument myDocument2 = arangoDB.db(dbName).collection(collectionName).getDocument("" + id,
                    BaseDocument.class);
            //Update the attributes needed
            myDocument2.updateAttribute("videos", videos);
            myDocument2.updateAttribute("title", title);
            myDocument2.updateAttribute("channel_id", channel_id);
            myDocument2.updateAttribute("description", description);
            myDocument2.updateAttribute("views_count", views_count);
            myDocument2.updateAttribute("videos_count", videos_count);
            myDocument2.updateAttribute("privacy", privacy);
            myDocument2.updateAttribute("playlist_type", playlist_type);
            myDocument2.updateAttribute("last_updated", getDateNowISO());
            //Delete old document from database
            arangoDB.db(dbName).collection(collectionName).deleteDocument("" + id);
            //Add the new one to the database
            arangoDB.db(dbName).collection(collectionName).insertDocument(myDocument2);
            return myDocument2.toString();
        }
    }

    /**
     * Get date now in ISO 8601 format
     *
     * @return
     */
    private static String getDateNowISO() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(new Date());
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
