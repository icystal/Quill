package fun.icystal.quill.fundamental;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.ai.util.JacksonUtils;
import org.springframework.ai.util.LoggingMarkers;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.NonNull;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;


/**
 * 增强 org.springframework.ai.converter.BeanOutputConverter
 */
public class CustomBeanOutputConverter<T> implements StructuredOutputConverter<T> {
    private final Logger logger;
    private final Type type;
    private final ObjectMapper objectMapper;
    private String jsonSchema;

    public CustomBeanOutputConverter(Class<T> clazz) {
        this(ParameterizedTypeReference.forType(clazz));
    }

    public CustomBeanOutputConverter(Class<T> clazz, ObjectMapper objectMapper) {
        this(ParameterizedTypeReference.forType(clazz), objectMapper);
    }

    public CustomBeanOutputConverter(ParameterizedTypeReference<T> typeRef) {
        this(typeRef.getType(), (ObjectMapper)null);
    }

    public CustomBeanOutputConverter(ParameterizedTypeReference<T> typeRef, ObjectMapper objectMapper) {
        this(typeRef.getType(), objectMapper);
    }

    private CustomBeanOutputConverter(Type type, ObjectMapper objectMapper) {
        this.logger = LoggerFactory.getLogger(org.springframework.ai.converter.BeanOutputConverter.class);
        Objects.requireNonNull(type, "Type cannot be null;");
        this.type = type;
        this.objectMapper = objectMapper != null ? objectMapper : this.getObjectMapper();
        this.generateSchema();
    }

    private void generateSchema() {
        JacksonModule jacksonModule = new JacksonModule(new JacksonOption[]{JacksonOption.RESPECT_JSONPROPERTY_REQUIRED, JacksonOption.RESPECT_JSONPROPERTY_ORDER});
        SchemaGeneratorConfigBuilder configBuilder = (new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)).with(jacksonModule).with(Option.FORBIDDEN_ADDITIONAL_PROPERTIES_BY_DEFAULT, new Option[0]);

        // 识别自定义注解
        configBuilder.forFields()
                .withIgnoreCheck(field -> {
                    Comment comment = field.getAnnotation(Comment.class);
                    return comment == null;
                })
                .withDescriptionResolver(field -> {
                    Comment comment = field.getAnnotation(Comment.class);
                    if (comment != null && StringUtils.isNotBlank(comment.desc())) {
                        return comment.desc();
                    } else {
                        return null;
                    }
                });

        SchemaGeneratorConfig config = configBuilder.build();
        SchemaGenerator generator = new SchemaGenerator(config);
        JsonNode jsonNode = generator.generateSchema(this.type, new Type[0]);
        ObjectWriter objectWriter = this.objectMapper.writer((new DefaultPrettyPrinter()).withObjectIndenter((new DefaultIndenter()).withLinefeed(System.lineSeparator())));

        try {
            this.jsonSchema = objectWriter.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            this.logger.error("Could not pretty print json schema for jsonNode: {}", jsonNode);
            throw new RuntimeException("Could not pretty print json schema for " + String.valueOf(this.type), e);
        }
    }

    public T convert(@NonNull String text) {
        try {
            text = text.trim();
            if (text.startsWith("```") && text.endsWith("```")) {
                String[] lines = text.split("\n", 2);
                if (lines[0].trim().equalsIgnoreCase("```json")) {
                    text = lines.length > 1 ? lines[1] : "";
                } else {
                    text = text.substring(3);
                }

                text = text.substring(0, text.length() - 3);
                text = text.trim();
            }

            return (T)this.objectMapper.readValue(text, this.objectMapper.constructType(this.type));
        } catch (JsonProcessingException e) {
            this.logger.error(LoggingMarkers.SENSITIVE_DATA_MARKER, "Could not parse the given text to the desired target type: \"{}\" into {}", text, this.type);
            throw new RuntimeException(e);
        }
    }

    protected ObjectMapper getObjectMapper() {
        return ((JsonMapper.Builder)((JsonMapper.Builder)JsonMapper.builder().addModules(JacksonUtils.instantiateAvailableModules())).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).build();
    }

    public String getFormat() {
        String template = "Your response should be in JSON format.\nDo not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.\nDo not include markdown code blocks in your response.\nRemove the ```json markdown from the output.\nHere is the JSON Schema instance your output must adhere to:\n```%s```\n";
        return String.format(template, this.jsonSchema);
    }

    public String getJsonSchema() {
        return this.jsonSchema;
    }

    public Map<String, Object> getJsonSchemaMap() {
        try {
            return (Map)this.objectMapper.readValue(this.jsonSchema, Map.class);
        } catch (JsonProcessingException ex) {
            this.logger.error("Could not parse the JSON Schema to a Map object", ex);
            throw new IllegalStateException(ex);
        }
    }
}

