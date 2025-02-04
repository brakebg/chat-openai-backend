package com.demo.chat.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class OpenAIClient {
    private static final Logger logger = LoggerFactory.getLogger(OpenAIClient.class);

    private final OkHttpClient client;
    private final String apiKey;

    @Autowired
    public OpenAIClient(Environment env) {
        this.apiKey = env.getProperty("OPENAI_API_KEY");
        this.client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    }

    @Value("${openai.api.url}")
    private String apiUrl;

    public String getChatResponse(String message) throws IOException {
        try {
            JSONObject json = new JSONObject();
            json.put("model", "gpt-4-turbo");
            json.put("messages", new JSONObject[]{ new JSONObject().put("role", "user").put("content", message) });

            RequestBody body = RequestBody.create(json.toString(), okhttp3.MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    logger.error("Unexpected response code: " + response);
                    throw new IOException("Unexpected response code: " + response.code());
                }

                JSONObject responseBody = new JSONObject(response.body().string());
                return responseBody.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            }
        } catch (Exception e) {
            logger.error("Error communicating with OpenAI API", e);
            throw new IOException("Error communicating with OpenAI API", e);
        }
    }
}
