package Commands.Post;

import Commands.Command;
import Models.Playlists;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;

public class PostPlaylists extends Command{
    @Override
    protected void execute() {
        HashMap<String, Object> props = parameters;
        Channel channel = (Channel) props.get("channel");
        JSONParser parser = new JSONParser();
        JSONObject playlist = null;

        try {
            JSONObject body = (JSONObject) parser.parse((String) props.get("body"));
            JSONObject params = (JSONObject) parser.parse(body.get("parameters").toString());

            playlist = (JSONObject)params.get("playlist");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        AMQP.BasicProperties properties = (AMQP.BasicProperties) props.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) props.get("replyProps");
        Envelope envelope = (Envelope) props.get("envelope");

        System.out.println("Posting Playlist");

        JSONArray videos = (JSONArray)playlist.get("videos");
        String response = Playlists.postPlaylistByID((int)playlist.get("id"),
                (String)playlist.get("title"),(int)playlist.get("channel_id"),
                (String)playlist.get("description"), (int)playlist.get("views_count"),
                (int)playlist.get("videos_count"), (String)playlist.get("privacy"),
                (String)playlist.get("playlist_type"),videos);

        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
