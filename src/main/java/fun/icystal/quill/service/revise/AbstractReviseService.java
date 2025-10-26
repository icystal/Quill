package fun.icystal.quill.service.revise;

import fun.icystal.quill.constant.BookStep;
import fun.icystal.quill.constant.ResponseCode;
import fun.icystal.quill.exception.QuillException;
import fun.icystal.quill.fundamental.CustomBeanOutputConverter;
import fun.icystal.quill.fundamental.ReviseHandler;
import fun.icystal.quill.obj.entity.Book;
import fun.icystal.quill.obj.entity.BookAbstract;
import fun.icystal.quill.obj.entity.Revise;
import fun.icystal.quill.obj.request.QuillRequest;
import fun.icystal.quill.service.LogService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static fun.icystal.quill.factory.PromptFactory.buildEntityPrompt;

@Service
public class AbstractReviseService implements ReviseHandler {

    private final ChatClient chatClient;

    private final CustomBeanOutputConverter<BookAbstract> converter;

    private final LogService logService;

    public AbstractReviseService(ChatClient.Builder builder, LogService logService) {
        this.chatClient = builder
                .build();
        this.converter = new CustomBeanOutputConverter<>(new ParameterizedTypeReference<BookAbstract>() {
        });

        this.logService = logService;
    }

    @Override
    public Book handle(QuillRequest request, Book book) {

        if (book == null || book.getBookDetail() == null || book.getBookDetail().getBookAbstract() == null || CollectionUtils.isEmpty(book.getBookDetail().getCharacters())) {
            throw new QuillException(ResponseCode.STEP_EXCEPTION, null);
        }

        String content = chatClient.prompt()
                .messages(buildPrompt(request, book))
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

    private List<Message> buildPrompt(QuillRequest request, Book book) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(systemPrompt + "\n" + converter.getFormat()));

        StringBuilder sb = new StringBuilder();
        sb.append("已经确定的人物形象:\n");
        for (int i = 0; i < book.getBookDetail().getCharacters().size(); i++) {
            sb.append("**[人物").append(i + 1).append("]\n");
            sb.append(buildEntityPrompt(book.getBookDetail().getCharacters().get(i)));
        }
        messages.add(new SystemMessage(sb.toString()));

        messages.add(new UserMessage(abstractReviseUserPrompt(request, book)));

        return messages;
    }


    private String abstractReviseUserPrompt(QuillRequest request, Book book) {
        StringBuilder sb = new StringBuilder();

        List<Revise> revises = logService.queryRevise();
        if (!CollectionUtils.isEmpty(revises)) {
            sb.append("在本次修改之前, 你可能需要了解之前的修改建议. 你已经按照这些建议完成了修改.");
            for (int i = 0; i < revises.size(); i++) {
                sb.append(" [历史建议").append(i + 1).append("] ").append(revises.get(i).content()).append("\n");
            }
        }

        sb.append("\n需要修改的故事摘要:\n").append(book.getBookDetail().getBookAbstract().contentString());
        sb.append("\n修改建议: ").append(request.getAdvice());
        return sb.toString();
    }

    private static final String systemPrompt = """
                你是一个擅长提炼故事大纲的小说家, 正在创作一部新的小说. 你已经根据主要人物形象, 完成了故事摘要. 现在, 你必须根据编辑的修改建议, 结合设定好的主要角色形象, 对故事摘要进行修改.
                故事摘要包含以下五个段落:
                 a. 交代背景(150~300字): 需要明确故事发生的时间和地点, 介绍故事发生的原因, 以及主角是如何被卷入故事, 在故事中处于何种境遇.
                 b. 总结故事的第一部分(150~300字): 第一部分需要有一个引人入胜, 通常是带有悬念的开端, 交代故事的起因, 并且引出故事的第一个灾难性事件.
                 c. 总结故事的第二部分(150~300字): 需要描绘面对第一个灾难性事件时, 主角陷入的困境、主角心理或行为上的反应、以及主角最终如走出困境并做出何种决定 得到历练的主角开始引导故事走向. 第二部分需要交代主角的目标、为了达成目标所面对的冲突和矛盾, 和由此引发的第二个灾难性事件, 以及这一事件是如何改变了主角的思想和行为模式.
                 d. 总结故事的第三部分(150~300字): 在经历了第二个灾难性事件之后, 新的思想和道德重塑了主角. 第三部分需要描述主角新的行为模式, 以及第三个灾难性事件, 将故事推向结局.
                 e. 总结故事的第四部分(150~300字): 主角面临最后的对抗, 故事走向结局. 主角可能赢得了胜利, 可能最终失败, 也可能有得有失.
                \s
                提示:
                 - 灾难性事件是打破平静故事, 为主角设下难题, 刺激主角做出新决定, 为故事发展提供新方向的事件. 它可以是一个现实的巨大变故, 也可以是人物心理的激烈挣扎.
                 - 在故事结构的基础上, 结合人物形象, 为故事填充细节. 必须考虑主角如何解决面临的难题, 以及解决难题时的心理斗争.
                 - 禁止胡乱修改故事结构规定的发展方向
                 - 人物行动应该符合人物形象, 禁止设计不符合人物形象的情节
                 - 五个段落应该描述一个连贯的故事, 故事的主要角色都必须出现.
                 - 所有出现的角色都应该合理, 必须描述其身份和出现原因, 禁止生硬地将任务嵌入故事中.
               """;
}
