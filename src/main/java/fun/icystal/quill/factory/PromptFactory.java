package fun.icystal.quill.factory;

import fun.icystal.quill.obj.entity.BookBrief;
import org.apache.commons.lang3.StringUtils;

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
                         b. 概括故事的第一部分内容, 至此出现第一个灾难性事件, 主角开始引导故事走向;
                         c. 概括故事的第二部分内容, 至此出现第二个灾难性事件, 主角开始改变原有的行为模式;
                         d. 概括故事的第三部分内容, 至此出现第三个灾难性事件, 推动主角走向结局;
                         e. 概括故事的第四部分内容, 主角面临最后的对抗, 或输或赢, 亦或有输有赢.
                        """ +
                """
                        提示:
                         - 灾难性事件是打破平静故事, 为主角设下难题, 刺激主角做出新决定, 为故事发展提供新方向的事件. 它可以是一个现实的巨大变故, 也可以是人物心理的激烈挣扎.
                         - 重点关注灾难性事件的描写, 以及之后主角所做的决定.
                         - 无须考虑如何解决主角面临的难题, 只关注故事发展的大方向即可.
                         - 无须描写场景细节, 但小说四个部分之间应该有逻辑关联.
                        """;
    }

    public static String structureUserPrompt(BookBrief brief) {
        return "故事概要: " + brief.getBrief();
    }


}
