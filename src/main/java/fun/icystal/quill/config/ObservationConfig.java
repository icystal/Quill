package fun.icystal.quill.config;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.observation.ChatClientObservationContext;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.observation.ChatModelObservationContext;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.observation.EmbeddingModelObservationContext;
import org.springframework.ai.observation.AiOperationMetadata;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.observation.ToolCallingObservationContext;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import util.JsonUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


@Configuration
public class ObservationConfig {

    private static final Logger logger = LoggerFactory.getLogger(ObservationConfig.class);

    @Bean
    @ConditionalOnMissingBean(name = "observationRegistry")
    public ObservationRegistry observationRegistry(
            ObjectProvider<ObservationHandler<?>> observationHandlerObjectProvider) {
        ObservationRegistry observationRegistry = ObservationRegistry.create();
        ObservationRegistry.ObservationConfig observationConfig = observationRegistry.observationConfig();
        observationHandlerObjectProvider.orderedStream().forEach(handler -> {
            Type[] genericInterfaces = handler.getClass().getGenericInterfaces();
            for (Type type : genericInterfaces) {
                if (type instanceof ParameterizedType parameterizedType
                        && parameterizedType.getRawType() instanceof Class<?> clazz
                        && ObservationHandler.class.isAssignableFrom(clazz)) {

                    Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
                    logger.info("load observation handler, supports context type: {}", actualTypeArgument);
                }
            }

            // 将handler添加到observationRegistry中
            observationConfig.observationHandler(handler);
        });
        return observationRegistry;
    }

    /**
     * 监听chat client调用
     */
    @Bean
    ObservationHandler<ChatClientObservationContext> chatClientObservationContextObservationHandler() {
        logger.info("ChatClientObservation start");
        return new ObservationHandler<>() {

            @Override
            public boolean supportsContext(@NotNull Observation.Context context) {
                return context instanceof ChatClientObservationContext;
            }

            @Override
            public void onStart(@NotNull ChatClientObservationContext context) {
                ChatClientRequest request = context.getRequest();
                List<? extends Advisor> advisors = context.getAdvisors();
                boolean stream = context.isStream();
                logger.info("💬ChatClientObservation start: ChatClientRequest : {}, Advisors : {}, stream : {}",
                        JsonUtil.toJSONString(request), JsonUtil.toJSONString(advisors), JsonUtil.toJSONString(stream));
            }

            @Override
            public void onStop(@NotNull ChatClientObservationContext context) {
                ObservationHandler.super.onStop(context);
            }
        };
    }

    /**
     * 监听chat model调用
     */
    @Bean
    ObservationHandler<ChatModelObservationContext> chatModelObservationContextObservationHandler() {
        logger.info("ChatModelObservation start");
        return new ObservationHandler<>() {

            @Override
            public boolean supportsContext(@NotNull Observation.Context context) {
                return context instanceof ChatModelObservationContext;
            }

            @Override
            public void onStart(@NotNull ChatModelObservationContext context) {
                AiOperationMetadata operationMetadata = context.getOperationMetadata();
                Prompt request = context.getRequest();
                logger.info("🤖ChatModelObservation start: AiOperationMetadata : {}",
                        JsonUtil.toJSONString(operationMetadata));
                logger.info("🤖ChatModelObservation start: Prompt : {}",
                        JsonUtil.toJSONString(request));
            }

            @Override
            public void onStop(@NotNull ChatModelObservationContext context) {
                ChatResponse response = context.getResponse();
                logger.info("🤖ChatModelObservation start: ChatResponse : {}",
                        JsonUtil.toJSONString(response));
            }
        };
    }

    /**
     * 监听工具调用
     */
    @Bean
    public ObservationHandler<ToolCallingObservationContext> toolCallingObservationContextObservationHandler() {
        logger.info("ToolCallingObservation start");
        return new ObservationHandler<>() {
            @Override
            public boolean supportsContext(@NotNull Observation.Context context) {
                return context instanceof ToolCallingObservationContext;
            }

            @Override
            public void onStart(@NotNull ToolCallingObservationContext context) {
                ToolDefinition toolDefinition = context.getToolDefinition();
                logger.info("🔨ToolCalling start: {} - {}", toolDefinition.name(), context.getToolCallArguments());
            }

            @Override
            public void onStop(@NotNull ToolCallingObservationContext context) {
                ToolDefinition toolDefinition = context.getToolDefinition();
                logger.info("✅ToolCalling done: {} - {}", toolDefinition.name(), context.getToolCallResult());
            }
        };
    }

    /**
     * 监听embedding model调用
     */
    @Bean
    public ObservationHandler<EmbeddingModelObservationContext> embeddingModelObservationContextObservationHandler() {
        logger.info("EmbeddingModelObservation start");
        return new ObservationHandler<>() {
            @Override
            public boolean supportsContext(@NotNull Observation.Context context) {
                return context instanceof EmbeddingModelObservationContext;
            }

            @Override
            public void onStart(@NotNull EmbeddingModelObservationContext context) {
                logger.info("📚EmbeddingModelObservation start: {} - {}", context.getOperationMetadata().operationType(),
                        context.getOperationMetadata().provider());
            }
        };
    }


}
