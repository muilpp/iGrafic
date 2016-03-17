package model.channels;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    private List<ChannelData> data;

    @JsonProperty("data")
    public List<ChannelData> getData() {
        return data;
    }
    public void setData(List<ChannelData> dataList) {
        this.data = dataList;
    }
}