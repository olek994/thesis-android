package mobile.thesis.aleksnader.thesis_android.Utils;

import android.util.Base64;
import android.widget.Toast;

import com.google.gson.Gson;
import mobile.thesis.aleksnader.thesis_android.Entity.Token;
import mobile.thesis.aleksnader.thesis_android.Entity.User;
import mobile.thesis.aleksnader.thesis_android.Static.StaticValues;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Aleksander on 12.11.2017.
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2017r.
 */
public class HttpRestUtils {

    public static Object httpPost(String url,Object obj, Class classOfObject){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(obj,httpHeaders);
        HttpEntity<Object> respone =  restTemplate.exchange(url, HttpMethod.POST,entity,classOfObject);
        return respone;
    }

    public static Object httpGet(String url,Class classOfObject){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        Object object = restTemplate.getForObject(url,classOfObject);

        return object;
    }

    public static Token getUserAccessToken(String username, String password) {
        String client_id = "thesis-android";
        String secret = "thesis";
        int statusCode;
        byte[] data;
        Token token = null;
        try {
            data = "thesis-android:thesis".getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            URL url = new URL(StaticValues.URLIP + "/oauth/token?grant_type=password&username=" + username + "&password=" + password);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.addRequestProperty("Content-Type", "application/json");
            conn.addRequestProperty("Authorization", "Basic "+base64);
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.connect();

            statusCode = conn.getResponseCode();

            if (statusCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                Gson gson = new Gson();
                token = gson.fromJson(sb.toString(), Token.class);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return token;
    }

}
