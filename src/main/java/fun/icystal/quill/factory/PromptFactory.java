package fun.icystal.quill.factory;

import org.apache.commons.lang3.StringUtils;

public class PromptFactory {

    public static String briefSystemPrompt(String genre, String character) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个拥有无数奇思妙想的小说作家。你正在创作一本").append(genre).append("小说, 现在你必须根据以下信息, 用一句话概括你正在创作的故事。\n设定信息:");
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


}
