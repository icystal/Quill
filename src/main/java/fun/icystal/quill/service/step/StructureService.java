package fun.icystal.quill.service.step;

import fun.icystal.quill.constant.BookStep;
import fun.icystal.quill.constant.ResponseCode;
import fun.icystal.quill.exception.QuillException;
import fun.icystal.quill.factory.PromptFactory;
import fun.icystal.quill.fundamental.GenerateHandler;
import fun.icystal.quill.obj.entity.Book;
import fun.icystal.quill.obj.entity.BookStructure;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class StructureService implements GenerateHandler {

    private final ChatClient chatClient;

    public StructureService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public Book handle(Book book) {
        if (book == null || book.getBookDetail() == null || book.getBookDetail().getBrief() == null) {
            throw new QuillException(ResponseCode.STEP_EXCEPTION, null);
        }

        BookStructure structure = chatClient.prompt()
                .system(PromptFactory.structureSystemPrompt())
                .user(PromptFactory.structureUserPrompt(book.getBookDetail().getBrief()))
                .call()
                .entity(BookStructure.class);
        book.getBookDetail().setStructure(structure);
        return book;
    }

    @Override
    public BookStep step() {
        return BookStep.STRUCTURE;
    }
}
