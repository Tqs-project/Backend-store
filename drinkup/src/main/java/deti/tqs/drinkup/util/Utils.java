package deti.tqs.drinkup.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class Utils {
    private Utils() { }

    public JSONObject getWeDeliverAPI(String endpoint) throws IOException, InterruptedException {

        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("http://webmarket-314811.oa.r.appspot.com"+endpoint))
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        try {
            var jsonResponse = new JSONObject(response.body());
            String status = (String) jsonResponse.get("status");
            if(status.equals("error")){
                return null;
            }
            return jsonResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public JSONObject postWeDeliverAPI(String endpoint, Map<String, String> params) throws IOException, InterruptedException {

        var objectMapper = new ObjectMapper();
        var requestBody = objectMapper.writeValueAsString(params);

        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("http://webmarket-314811.oa.r.appspot.com/api"+endpoint))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        try {
            var jsonResponse = new JSONObject(response.body());
            String status = (String) jsonResponse.get("status");
            if(status.equals("error")){
                return null;
            }
            return jsonResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public String getAuthToken() throws IOException, InterruptedException, JSONException {

        var body = new HashMap<String, String>();
        body.put("username", "DrinkUp");
        body.put("email", "drinkup@gmail.com");
        body.put("password", "password");

        JSONObject jsonResponse = postWeDeliverAPI("/customer/signin", body);

        if (jsonResponse==null){
            return null;
        }else{
            return (String) jsonResponse.get("token");
        }
    }

}