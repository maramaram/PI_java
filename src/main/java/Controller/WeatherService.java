package Controller;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WeatherService {
    private static final String API_KEY = "a9d704a63dae7fdd6c8ecb4709c52e5e";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static JSONObject getWeatherData(String city) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(BASE_URL + "?q=" + city + "&appid=" + API_KEY);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                return new JSONObject(result);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
