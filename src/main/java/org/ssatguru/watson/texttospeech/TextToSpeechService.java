package org.ssatguru.watson.texttospeech;

import java.util.Collections;
import java.util.List;
import java.io.InputStream;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.ssatguru.watson.BluemixServices;
import org.ssatguru.watson.CdiConfig;
import org.ssatguru.watson.GenericCredentials;

/**
 * A Question and Answer service facade. 
 */

public class TextToSpeechService {

    private Client client;
    private WebTarget target;


    public TextToSpeechService() {

        final GenericCredentials credentials = BluemixServices
                .getTextToSpeechConfig().getCredentials();
        
        client = new CdiConfig().createJaxRsClient();

        client.register(credentials);

        target = client.target(credentials.getUrl()).path("v1").path(
                "synthesize");
    }

    public List<String> getAvailableVoices() {
        return Collections.singletonList("VoiceEnUsMichael");
    }

    public byte[] textToOgg(String text, String voice) {
        try {


            WebTarget queryParam = target.queryParam("voice", voice).queryParam(
                    "text", text);
            Response response = queryParam.request("audio/ogg; codecs=opus").
                    get();
            try (InputStream in = response.readEntity(InputStream.class)) {
                return IOUtils.toByteArray(in);
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
