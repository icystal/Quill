package fun.icystal.quill.service.step;

import com.google.common.collect.Lists;
import fun.icystal.quill.constant.BookStep;
import fun.icystal.quill.constant.ResponseCode;
import fun.icystal.quill.exception.QuillException;
import fun.icystal.quill.factory.PromptFactory;
import fun.icystal.quill.fundamental.GenerateHandler;
import fun.icystal.quill.obj.entity.Book;
import fun.icystal.quill.obj.entity.BookBrief;
import fun.icystal.quill.obj.entity.BookDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BriefService implements GenerateHandler {

    private final ChatClient chatClient;

    public BriefService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    private static final List<String> genres = Lists.newArrayList("悬疑", "爱情", "科幻", "修仙", "历史", "童话", "悬疑爱情", "奇幻", "社会", "情色", "文学");

    @Override
    public Book handle(Book book) {
        if (book == null) {
            throw new QuillException(ResponseCode.STEP_EXCEPTION, null);
        }
        if (book.getBookDetail() == null) {
            book.setBookDetail(new BookDetail());
        }

        BookBrief brief = book.getBookDetail().getBrief();
        if (brief == null) {
            brief = new BookBrief();
            book.getBookDetail().setBrief(brief);
        }

        if (StringUtils.isBlank(brief.getGenre())) {
            String genre = genres.get((int) (System.currentTimeMillis() % genres.size()));
            brief.setGenre(genre);
        }

        String content = chatClient.prompt()
                .system(String.format(PromptFactory.briefSystemPrompt(brief.getGenre(), brief.getCharacter())))
                .call().content();
        brief.setBrief(content);

        return book;
    }

    @Override
    public BookStep step() {
        return BookStep.BRIEF;
    }
}
