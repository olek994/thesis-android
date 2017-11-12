package mobile.thesis.aleksnader.thesis_android.Utils;

import mobile.thesis.aleksnader.thesis_android.Entity.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Aleksander on 12.11.2017.
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2017r.
 */
public class HttpRestUtils {

    public static Object httpPost(String url,Object obj, Class classOfObject){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(obj,httpHeaders);
        HttpEntity<Object> respone =  restTemplate.exchange(url, HttpMethod.POST,entity,classOfObject);
        return respone;
    }

    public static Object httpGet(String url,Class classOfObject){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        Object object = restTemplate.getForObject(url,classOfObject);

        return object;
    }
}
