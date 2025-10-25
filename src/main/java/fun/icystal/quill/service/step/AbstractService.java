package fun.icystal.quill.service.step;

import fun.icystal.quill.constant.BookStep;
import fun.icystal.quill.constant.ResponseCode;
import fun.icystal.quill.exception.QuillException;
import fun.icystal.quill.factory.PromptFactory;
import fun.icystal.quill.fundamental.CustomBeanOutputConverter;
import fun.icystal.quill.fundamental.GenerateHandler;
import fun.icystal.quill.obj.entity.Book;
import fun.icystal.quill.obj.entity.BookAbstract;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
public class AbstractService implements GenerateHandler {

    private final ChatClient chatClient;

    private final CustomBeanOutputConverter<BookAbstract> converter;

    public AbstractService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
        this.converter = new CustomBeanOutputConverter<>(new ParameterizedTypeReference<BookAbstract>() {
        });
    }

    @Override
    public Book handle(Book book) {
        if (book == null || book.getBookDetail() == null
                || book.getBookDetail().getStructure() == null
                || book.getBookDetail().getCharacters() == null) {
            throw new QuillException(ResponseCode.STEP_EXCEPTION, null);
        }

        String content = chatClient.prompt()
                .system(PromptFactory.abstractSystemPrompt() + "\n" + converter.getFormat())
                .user(PromptFactory.abstractUserPrompt(book))
                .call()
                .content();

        if (content == null) {
            throw new QuillException(ResponseCode.MODEL_RESPONSE_EXCEPTION, null);
        }

        BookAbstract bookAbstract;
        try {
            bookAbstract = converter.convert(content);
        } catch (Exception e) {
            throw new QuillException(ResponseCode.MODEL_RESPONSE_EXCEPTION, null);
        }
        if (bookAbstract == null) {
            throw new QuillException(ResponseCode.MODEL_RESPONSE_EXCEPTION, null);
        }

        book.getBookDetail().setBookAbstract(bookAbstract);
        return book;
    }

    @Override
    public BookStep step() {
        return BookStep.ABSTRACT;
    }
}
