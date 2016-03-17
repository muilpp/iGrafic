package controller;

import java.util.List;

import model.channels.ChannelData;
import model.chats.ChatData;
import model.messages.MessageData;

public interface IMeteorDesk {

    public String login();

    /**
     * Channels are phone numbers
     * @param token
     * @return channel id (phone number)
     */
    public List<ChannelData> getChannels(String token);
    public List<ChatData> getChats(String token, String channelID);
    public List<MessageData> getMessages(String token, String chatID);
}