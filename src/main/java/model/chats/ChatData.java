package model.chats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatData {
    private String chatId;
    private String userPhoneNumber;
    private long lastUserMessage;
    
    public String getChatId() {
        return chatId;
    }
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
    @JsonProperty("chatUser")
    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }
    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
    public long getLastUserMessage() {
        return lastUserMessage;
    }
    public void setLastUserMessage(long lastUserMessage) {
        this.lastUserMessage = lastUserMessage;
    }
}