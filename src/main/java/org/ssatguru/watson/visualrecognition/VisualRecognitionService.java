package org.ssatguru.watson.visualrecognition;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.ssatguru.watson.BluemixServices;
import org.ssatguru.watson.CdiConfig;
import org.ssatguru.watson.GenericCredentials;
import org.ssatguru.watson.visualrecognition.response.ImageData;
import org.ssatguru.watson.visualrecognition.response.VisualRecognitionResponse;

/**
 * A Question and Answer service facade. If VCAP_SERVICES don't contain your
 * credentials, call init method manually before usage.
 */

public class VisualRecognitionService {


    ObjectMapper objectMapper;

    private GenericCredentials credentials;
    private String uri;


    public VisualRecognitionService() {
    	objectMapper = new CdiConfig().getObjectMapper();
        credentials = BluemixServices
                .getVisualRecognitionConfig().getCredentials();
        uri = credentials.getUrl() + "/v1/tag/recognize";
    }

    public ImageData recognize(byte[] jpeg) {


        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("imgFile", jpeg, ContentType.DEFAULT_BINARY,
                "cam.jpg");

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(uri);
        post.addHeader("Authorization", credentials.
                createAuthorizationHeaderValue());
        post.setEntity(builder.build());

        try {
            HttpResponse response = httpclient.execute(post);
            VisualRecognitionResponse r = objectMapper.readValue(
                    EntityUtils.toString(response.getEntity()),
                    VisualRecognitionResponse.class);
            return r.getImages().get(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
