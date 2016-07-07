package controller;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Helpers;
import model.IMeteorDesk;
import model.MeteorDeskService;
import model.channels.ChannelData;
import model.chats.ChatData;
import model.messages.MessageData;

public class ImagesParser {
    private static final Logger LOGGER = Logger.getLogger(ImagesParser.class.getName());

    public static void main(String[] args) {
        IMeteorDesk meteorService = new MeteorDeskService();

        String token = meteorService.login();

        if (!(isNullOrEmpty(token))) {
            List<ChannelData> channelDataList = meteorService.getChannels(token);

            for (ChannelData channelData : channelDataList) {
                if (!isNullOrEmpty(channelData.getChannelId())) {
                    List<ChatData> chatList = meteorService.getChats(token, channelData.getChannelId());

                    for (ChatData chatData : chatList) {
                        List<MessageData> messageList = meteorService.getMessages(token, chatData.getChatId());

                        int imgNum = 1;
                        File file = new File(channelData.getPhoneNumber()+"/"+chatData.getUserPhoneNumber());
                        if (file.exists())
                            imgNum = Helpers.getLastPictureNumber(file);

                        int imgCounter = 0;
                        for (MessageData message : messageList) {
                            //TODO Comprovar que les imatges son sempre .jpg
                            String destinationFile = new StringBuilder(channelData.getPhoneNumber()).append("/")
                                    .append(chatData.getUserPhoneNumber()).append("/").append(imgNum).append(".jpg").toString();

                            Path imgPath = Paths.get(destinationFile);
                            if (!isNullOrEmpty(message.getImgURL()) && !Files.exists(imgPath, LinkOption.NOFOLLOW_LINKS)) {
                                imgCounter++;
                                Helpers.saveImage(message.getImgURL(), destinationFile);
                                imgNum++;
                            }
                        }
                        LOGGER.log(Level.INFO, "S'han guardat " + imgCounter + " imatges.");
                    }
                }
            }
        }
    }
}