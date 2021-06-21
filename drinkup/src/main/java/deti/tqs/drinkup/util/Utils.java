package deti.tqs.drinkup.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
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

@Log4j2
public class Utils {
    private static final String URI = "http://webmarket-314811.oa.r.appspot.com/";

    public Utils() {
        //Utilities for requests
    }

    public JSONObject requestWeDeliverAPI(HttpRequest request) throws IOException, InterruptedException {

        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        try {
            return new JSONObject(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public JSONArray arrayRequestWeDeliverAPI(HttpRequest request) throws IOException, InterruptedException {

        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        try {
            return new JSONArray(response.body());
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

        var objectMapper = new ObjectMapper();
        var requestBody = objectMapper.writeValueAsString(body);

        HttpRequest request = null;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(URI+"api/customer/signin"))
                    .setHeader("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        JSONObject jsonResponse = requestWeDeliverAPI(request);

        if (jsonResponse==null){
            return null;
        }else{
            return (String) jsonResponse.get("token");
        }
    }

}