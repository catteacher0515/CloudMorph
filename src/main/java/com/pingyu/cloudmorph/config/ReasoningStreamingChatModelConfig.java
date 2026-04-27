package com.pingyu.cloudmorph.config;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
@ConfigurationProperties(prefix = "langchain4j.open-ai.chat-model")
@Data
public class ReasoningStreamingChatModelConfig {

    private String baseUrl;
    private String apiKey;

    @Bean
    public StreamingChatModel reasoningStreamingChatModel(
            @Qualifier("openAiStreamingChatModelHttpClientBuilder") HttpClientBuilder httpClientBuilder) {
        // 开发调试使用普通对话模型，生产环境换 deepseek-reasoner + maxTokens=32768
        final String modelName = "deepseek-chat";
        final int maxTokens = 8192;
        return OpenAiStreamingChatModel.builder()
                .httpClientBuilder(httpClientBuilder)
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .maxTokens(maxTokens)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
