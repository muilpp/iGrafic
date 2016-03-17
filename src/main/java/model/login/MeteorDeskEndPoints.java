package model.login;

public abstract class MeteorDeskEndPoints {
    private static final String METEOR_API = "https://data.meteordesk.com";
    public static final String LOGIN_ENDPOINT = METEOR_API + "/login";
    public static final String CHANNELS_ENDPOINT = METEOR_API + "/channels";
    public static final String CHATS_ENDPOINT = METEOR_API + "/channel/{channelID}/chats";
    public static final String MESSAGES_ENDPOINT = METEOR_API + "/chat/{chatID}/messages";
}