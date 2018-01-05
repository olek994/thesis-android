package mobile.thesis.aleksnader.thesis_android.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Aleksander on 10.11.2017.
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2017r.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {

    private Long id;
    private String name;
    private String subname;
    private String email;
    private String password;
    private List<Long> conversationsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getConversationsId() {
        return conversationsId;
    }

    public void setConversationsId(List<Long> conversationsId) {
        this.conversationsId = conversationsId;
    }
}
