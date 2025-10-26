package fun.icystal.quill.service.revise;

import fun.icystal.quill.constant.BookStep;
import fun.icystal.quill.constant.ResponseCode;
import fun.icystal.quill.exception.QuillException;
import fun.icystal.quill.fundamental.CustomBeanOutputConverter;
import fun.icystal.quill.fundamental.ReviseHandler;
import fun.icystal.quill.obj.dto.CharacterChatDTO;
import fun.icystal.quill.obj.entity.Book;
import fun.icystal.quill.obj.entity.BookStructure;
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
public class CharacterReviseService implements ReviseHandler {

    private final ChatClient chatClient;

    private final CustomBeanOutputConverter<CharacterChatDTO> converter;

    private final LogService logService;

    public CharacterReviseService(ChatClient.Builder builder, LogService logService) {
        this.chatClient = builder.build();
        this.logService = logService;
        this.converter = new CustomBeanOutputConverter<>(new ParameterizedTypeReference<CharacterChatDTO>() {
        });
    }

    @Override
    public Book handle(QuillRequest request, Book book) {
        if (book == null || book.getBookDetail() == null || book.getBookDetail().getStructure() == null || CollectionUtils.isEmpty(book.getBookDetail().getCharacters())) {
            throw new QuillException(ResponseCode.STEP_EXCEPTION, null);
        }

        String content = chatClient.prompt()
                .messages(buildPrompt(request, book))
                .call().content();
        if (content == null) {
            throw new QuillException(ResponseCode.MODEL_RESPONSE_EXCEPTION, null);
        }

        CharacterChatDTO chatDTO;
        try {
            chatDTO = converter.convert(content);
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

    private List<Message> buildPrompt(QuillRequest request, Book book) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(systemPrompt + "\n" + converter.getFormat()));


        BookStructure structure = book.getBookDetail().getStructure();

        String structurePrompt = "已经确定的小说结构:" +
                "\n [故事背景] " + structure.getBackground() +
                "\n [第一部分] " + structure.getFirstPart() +
                "\n [第二部分] " + structure.getSecondPart() +
                "\n [第三部分] " + structure.getThirdPart() +
                "\n [第四部分] " + structure.getFourthPart();
        messages.add(new SystemMessage(structurePrompt));

        messages.add(new UserMessage(characterReviseUserPrompt(request, book)));

        return messages;
    }



    private String characterReviseUserPrompt(QuillRequest param, Book book) {
        StringBuilder sb = new StringBuilder();
        List<Revise> revises = logService.queryRevise();
        if (!CollectionUtils.isEmpty(revises)) {
            sb.append("在本次修改之前, 你可能需要了解关于人物形象的历史修改建议. 你已经按照这些建议完成了修改.");
            for (int i = 0; i < revises.size(); i++) {
                sb.append(" [历史建议").append(i + 1).append("] ").append(revises.get(i).content()).append("\n");
            }
        }

        sb.append("需要修改的人物形象:\n");
        for (int i = 0; i < book.getBookDetail().getCharacters().size(); i++) {
            sb.append("[人物").append(i + 1).append("]\n");
            sb.append(buildEntityPrompt(book.getBookDetail().getCharacters().get(i)));
            sb.append("\n");
        }

        sb.append("修改建议: ").append(param.getAdvice());
        return sb.toString();
    }

    private static final String systemPrompt = """
                        你是一个擅长刻画人物形象的小说家, 正在创作一本新作. 你已经设计好了小说结构, 并且刻画了小说的主要角色形象. 现在你必须根据编辑的修改建议, 对主要角色形象进行修改, 得到最终的主要人物简介.
                        每个人物的简介包括:
                         a. 姓名
                         b. 性别
                         c. 年龄
                         d. 身份
                         e. 目标: 具体、可量化的短期或中期追求，通常有明确的计划和行动步骤. 人物的目标通常是为了实现抱负所作出的计划
                         f. 抱负: 宏观、抽象的长远愿景，体现个人对自我实现的深层渴望. 人物的抱负的精神内核是价值观
                         g: 价值观: 人物对「什么值得我坚持」这一问题的深层信念. 需要填写2~4个价值观元素
                         h: 人物概括: 简短地描述人物形象、在故事中人物的行动、他的行动造成的后果, 以及他的结局
                        """;
}
