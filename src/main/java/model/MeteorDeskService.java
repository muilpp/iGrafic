package model;

import static model.login.MeteorDeskEndPoints.CHANNELS_ENDPOINT;
import static model.login.MeteorDeskEndPoints.CHATS_ENDPOINT;
import static model.login.MeteorDeskEndPoints.LOGIN_ENDPOINT;
import static model.login.MeteorDeskEndPoints.MESSAGES_ENDPOINT;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import model.channels.ChannelData;
import model.channels.ChannelsResult;
import model.chats.ChatData;
import model.chats.ChatsResult;
import model.login.LoginResult;
import model.messages.MessageData;
import model.messages.MessageResult;

public class MeteorDeskService implements IMeteorDesk{
    private static Logger LOGGER = Logger.getLogger(MeteorDeskService.class.getName());
    private Client client;

    public MeteorDeskService() {
        try {
            client = IgnoreSSLClient();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public Client IgnoreSSLClient() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, new TrustManager[] { new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
            }

            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

        } }, new java.security.SecureRandom());
        HostnameVerifier allHostsValid = new InsecureHostnameVerifier();

        return ClientBuilder.newBuilder().sslContext(sslcontext).hostnameVerifier(allHostsValid).build();
    }

    public class InsecureHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    
    @Override
    public String login() {
        WebTarget webTarget = client.target(LOGIN_ENDPOINT);

        //TODO User i password estan hardcoded a saco, igual es podrien posar en una variable d'entorn o alguna cosa per l'estil
        Response response = webTarget.request()
                .post(Entity.json("{\"logindata\":{\"user\":\"xxx\",\"password\":\"yyy\"}}"));

        if (response.getStatus() != 200) {
            LOGGER.log(Level.INFO, "Failed : HTTP error code : " + response.getStatus());
        } else {
            LoginResult loginResult = response.readEntity(LoginResult.class);

            return loginResult.getResult().getToken();
        }

        return null;
    }

    @Override
    public List<ChannelData> getChannels(final String token) {
        WebTarget webTarget = client.target(CHANNELS_ENDPOINT);

        Response response = webTarget.request().header("Token", token).get();

        if (response.getStatus() != 200) {
            LOGGER.log(Level.INFO, "Failed : HTTP error code : " + response.getStatus());
        } else {
            ChannelsResult channelsResult = response.readEntity(ChannelsResult.class);
            List<ChannelData> channelDataList = new ArrayList<>();

            for (ChannelData data : channelsResult.getResult().getData()) {
                channelDataList.add(data);
            }

            return channelDataList;
        }

        return Collections.emptyList();
    }

    @Override
    public List<ChatData> getChats(final String token, final String channelID) {
        WebTarget webTarget = client.target(CHATS_ENDPOINT.replace("{channelID}", channelID));

        Response response = webTarget.request().header("Token", token).get();

        if (response.getStatus() != 200) {
            LOGGER.log(Level.INFO, "Failed : HTTP error code : " + response.getStatus());
        } else {
            ChatsResult chatsResult = response.readEntity(ChatsResult.class);
            List<ChatData> chatList = new ArrayList<>();

            for (ChatData chat : chatsResult.getResult().getData()) {
                chatList.add(chat);
            }

            return chatList;
        }

        return Collections.emptyList();
    }

    @Override
    public List<MessageData> getMessages(final String token, final String chatID) {
        WebTarget webTarget = client.target(MESSAGES_ENDPOINT.replace("{chatID}", chatID));

        Response response = webTarget.request().header("Token", token).get();

        if (response.getStatus() != 200) {
            LOGGER.log(Level.INFO, "Failed : HTTP error code : " + response.getStatus());
        } else {
            MessageResult messageResult = response.readEntity(MessageResult.class);
            List<MessageData> messageList = new ArrayList<>();

            // TODO Aquí es on podries comprovar que el hash que arriba no s'ha descarregat i així no hi haurien fotos duplicades. 
            // Tot i això, estaria bé que no es dupliquessin per defecte.
            for (MessageData message : messageResult.getResult().getData()) {
                messageList.add(message);
            }

            return messageList;
        }

        return Collections.emptyList();
    }
}