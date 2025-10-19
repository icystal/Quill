package fun.icystal.quill.service.step;

import fun.icystal.quill.constant.BookStep;
import fun.icystal.quill.constant.ResponseCode;
import fun.icystal.quill.exception.QuillException;
import fun.icystal.quill.factory.PromptFactory;
import fun.icystal.quill.fundamental.CustomBeanOutputConverter;
import fun.icystal.quill.fundamental.GenerateHandler;
import fun.icystal.quill.obj.dto.CharacterChatDTO;
import fun.icystal.quill.obj.entity.Book;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class CharacterService implements GenerateHandler {

    private final ChatClient chatClient;

    private final CustomBeanOutputConverter<CharacterChatDTO> converter;

    public CharacterService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
        this.converter = new CustomBeanOutputConverter<>(new ParameterizedTypeReference<CharacterChatDTO>() {
        });
    }

    @Override
    public Book handle(Book book) {
        if (book == null || book.getBookDetail() == null || book.getBookDetail().getStructure() == null) {
            throw new QuillException(ResponseCode.STEP_EXCEPTION, null);
        }

        String objText = chatClient.prompt()
                .system(PromptFactory.characterSystemPrompt() + "\n" + converter.getFormat())
                .user(PromptFactory.characterUserPrompt(book.getBookDetail().getStructure()))
                .call().content();
        if (objText == null) {
            throw new QuillException(ResponseCode.MODEL_RESPONSE_EXCEPTION, null);
        }

        CharacterChatDTO chatDTO;
        try {
            chatDTO = converter.convert(objText);
        } catch (Exception e) {
            throw new QuillException(ResponseCode.MODEL_RESPONSE_EXCEPTION, null);
        }
        if (chatDTO == null || CollectionUtils.isEmpty(chatDTO.getCharacters())) {
            throw new QuillException(ResponseCode.MODEL_RESPONSE_EXCEPTION, null);
        }

        book.getBookDetail().setCharacters(chatDTO.getCharacters());
        return book;
    }

    @Override
    public BookStep step() {
        return BookStep.CHARACTER;
    }
}
