package fun.icystal.quill.factory;

import fun.icystal.quill.fundamental.Comment;
import fun.icystal.quill.obj.entity.Book;
import fun.icystal.quill.obj.entity.BookBrief;
import fun.icystal.quill.obj.entity.BookStructure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import util.JsonUtil;

import java.lang.reflect.Field;

@Slf4j
public class PromptFactory {

    public static String briefSystemPrompt(String genre, String character) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个拥有无数奇思妙想的小说作家. 你正在创作一本").append(genre).append("小说, 现在你必须根据以下信息, 用一句话概括你正在创作的故事. \n设定信息:");
        if (StringUtils.isNotEmpty(genre)) {
            sb.append(" - 主题: ").append(genre).append("\n");
        }
        if (StringUtils.isNotEmpty(character)) {
            sb.append(" - 主角身份: ").append(character).append("\n");
        }
        sb.append("""
              要求:
               - 必须有始有终地描述一个故事, 而非一个静态的场景。必须包含故事起因、经过和结果三个要素
               - 概要内容必须符合主题, 体现主角身份和他们的目标
               - 无须交代故事背景, 无须为角色命名
               - 尽量简短, 但并非越短越好
               - 涉及的情节和设定应该具体
               - 禁止以「刚刚开始」、「正要开始」、「还未开始」一类的语句作为结尾
              """);
        return sb.toString();
    }

    public static String structureSystemPrompt() {
        return "你是一个拥有无数奇思妙想,并且擅长使用三幕式结构设计小说结构的作家. 你需要根据一句精简的小说概要，设计出小说的结构. \n" +
                """
                        小说结构包含五个部分:
                         a. 交代背景, 介绍主角和重要角色;
                         b. 概括故事的第一部分内容, 以第一个灾难性事件结束, 这一事件迫使主角完全投入到故事中;
                         c. 概括故事的第二部分内容, 以第二个灾难性事件结束, 主角扭转了原先的思想或行为模式, 并按照新的思路开启之后的故事;
                         d. 概括故事的第三部分内容, 以第三个灾难性事件结束, 这一事件推动主角走向结局;
                         e. 概括故事的第四部分内容, 主角面临最后的对抗, 或输或赢, 亦或有输有赢.
                        """ +
                """
                        提示:
                         - 灾难性事件是打破平静故事, 为主角设下难题, 刺激主角做出新决定, 为故事发展提供新方向的事件. 它可以是一个现实的巨大变故, 也可以是人物心理的激烈挣扎.
                         - 重点关注灾难性事件的描写, 以及之后主角所做的决定, 无须为角色命名.
                         - 无须考虑如何解决主角面临的难题, 只关注故事发展的大方向即可.
                         - 无须描写场景细节, 但小说四个部分之间应该有逻辑关联.
                        """;
    }

    public static String structureUserPrompt(BookBrief brief) {
        return "故事概要: " + brief.getBrief();
    }

    public static String characterSystemPrompt() {
        return "你是一个擅长刻画人物形象的小说家. 你正在创作一本新作, 需要根据小说的三幕式结构, 完成小说中3~5名主要角色的人物简介. \n" +
                """
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

    public static String characterUserPrompt(BookStructure structure) {
        return "小说结构\n - 故事背景: " + structure.getBackground() +
                "\n - 第一部分: " + structure.getFirstPart() +
                "\n - 第二部分: " + structure.getSecondPart() +
                "\n - 第三部分: " + structure.getThirdPart() +
                "\n - 第四部分: " + structure.getFourthPart();
    }

    public static String abstractSystemPrompt() {
        return """
                你是一个擅长提炼故事大纲的小说家, 正在创作一部新的小说. 你已经设计好了故事结构, 明确了故事背景和每一部分的故事内容，并且刻画了故事中出现的主要人物形象. 现在你需要根据故事结构和主要人物形象, 完成故事摘要.
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
               """;
    }

    public static String abstractUserPrompt(Book book) {
        StringBuilder sb = new StringBuilder();
        sb.append("故事结构:\n");
        sb.append(" 故事背景: ").append(book.getBookDetail().getStructure().getBackground()).append("\n");
        sb.append(" 第一部分: ").append(book.getBookDetail().getStructure().getFirstPart()).append("\n");
        sb.append(" 第二部分: ").append(book.getBookDetail().getStructure().getSecondPart()).append("\n");
        sb.append(" 第三部分: ").append(book.getBookDetail().getStructure().getThirdPart()).append("\n");
        sb.append(" 第四部分: ").append(book.getBookDetail().getStructure().getFourthPart()).append("\n");

        sb.append("\n人物形象:\n");
        for (int i = 0; i < book.getBookDetail().getCharacters().size(); i++) {
            sb.append(" 人物").append(i + 1).append("\n");
            sb.append(buildEntityPrompt(book.getBookDetail().getCharacters().get(i)));
        }

        return sb.toString();
    }

    public static String buildEntityPrompt(@NotNull Object entity) {
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        for (Field field : declaredFields) {
            Comment comment = field.getAnnotation(Comment.class);
            if (comment == null || StringUtils.isBlank(comment.prompt())) {
                continue;
            }

            field.setAccessible(true);
            try {
                Object o = field.get(entity);
                if (o == null || StringUtils.isBlank(JsonUtil.toJSONString(o))) {
                    continue;
                }
                sb.append(comment.prompt()).append(": ").append(JsonUtil.toJSONString(o)).append("\n");
            } catch (IllegalAccessException e) {
                log.warn("从实体类获取提示词异常, class: {}, field: {}", entity.getClass().getName(), field.getName());
            }
        }
        return sb.toString();
    }

}


