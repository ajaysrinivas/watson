package org.ssatguru.watson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Matti Tahvonen
 */
public class CdiConfig {

    private JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();


    private ObjectMapper objectMapper = provider.locateMapper(Object.class,
            MediaType.APPLICATION_JSON_TYPE).configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    public ObjectMapper getObjectMapper(){
    	return objectMapper;
    }

    public Client createJaxRsClient() {
        return ClientBuilder.newBuilder()
                .register(provider)
                .build();
    }

    //TODO figure out when to call this
    public void closeClient(Client client) {
        client.close();
    }

}
