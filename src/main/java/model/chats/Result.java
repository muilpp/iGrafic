package model.chats;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    private List<ChatData> data;

    @JsonProperty("data")
    public List<ChatData> getData() {
        return data;
    }

    public void setData(List<ChatData> data) {
        this.data = data;
    }
}