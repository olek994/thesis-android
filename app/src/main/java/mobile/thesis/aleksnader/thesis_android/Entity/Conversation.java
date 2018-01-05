package mobile.thesis.aleksnader.thesis_android.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Aleksander on 05.01.2018.
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2017r.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Conversation {
    private Long id;
    private List<Long> messagesId;
    private Long firstInterlocutorId;
    private Long secondInterlocutorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getMessagesId() {
        return messagesId;
    }

    public void setMessagesId(List<Long> messagesId) {
        this.messagesId = messagesId;
    }

    public Long getFirstInterlocutorId() {
        return firstInterlocutorId;
    }

    public void setFirstInterlocutorId(Long firstInterlocutorId) {
        this.firstInterlocutorId = firstInterlocutorId;
    }

    public Long getSecondInterlocutorId() {
        return secondInterlocutorId;
    }

    public void setSecondInterlocutorId(Long secondInterlocutorId) {
        this.secondInterlocutorId = secondInterlocutorId;
    }
}
