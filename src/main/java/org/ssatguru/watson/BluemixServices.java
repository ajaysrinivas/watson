package org.ssatguru.watson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class BluemixServices {

	private static String configJson = System.getenv("VCAP_SERVICES");
	private static JsonNode jsonTree = null;
	private static ObjectMapper objectMapper = new ObjectMapper();

	private static GenericServiceConfig textToSpeechConfig = null;
	private static GenericServiceConfig visualRecognitionConfig = null;

	public static GenericServiceConfig getTextToSpeechConfig() {
		if (textToSpeechConfig == null) {
			textToSpeechConfig = getGenericServiceConfigs("text_to_speech").get(0);
		}
		return textToSpeechConfig;
	}

	public static GenericServiceConfig getVisualRecognitionConfig() {
		if (visualRecognitionConfig == null) {
			visualRecognitionConfig = getGenericServiceConfigs("visual_recognition").get(0);
		}
		return visualRecognitionConfig;

	}

	private static List<GenericServiceConfig> getGenericServiceConfigs(String key) {
		if (configJson == null) {
			try {
				InputStream in = BluemixServices.class.getClassLoader().getResourceAsStream("VCAP_SERVICES.properties");
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				StringBuilder out = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					out.append(line);
				}
				configJson = out.toString();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
		if (jsonTree == null) {
			try {
				jsonTree = objectMapper.readTree(configJson);
			} catch (Exception ex) {
				throw new RuntimeException();
			}
		}
		JsonNode node = jsonTree.get(key);
		JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, GenericServiceConfig.class);
		return objectMapper.convertValue(node, type);
	}

}
