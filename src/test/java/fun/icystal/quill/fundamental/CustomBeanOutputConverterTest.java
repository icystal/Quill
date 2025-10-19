package fun.icystal.quill.fundamental;

import fun.icystal.quill.obj.dto.CharacterChatDTO;
import org.junit.jupiter.api.Test;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomBeanOutputConverterTest {

    @Test
    public void jsonSchemaTest() {
        BeanOutputConverter<CharacterChatDTO> converter = new BeanOutputConverter<>(CharacterChatDTO.class);
        String jsonSchema = converter.getJsonSchema();
        assert !jsonSchema.contains("description");

        CustomBeanOutputConverter<CharacterChatDTO> customBeanOutputConverter = new CustomBeanOutputConverter<>(CharacterChatDTO.class);
        String customJsonSchema = customBeanOutputConverter.getJsonSchema();
        assert customJsonSchema.contains("description");
    }
}
