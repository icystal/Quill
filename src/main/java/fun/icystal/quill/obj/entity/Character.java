package fun.icystal.quill.obj.entity;

import fun.icystal.quill.fundamental.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Character {

    @Comment(desc = "姓名", prompt = "姓名")
    private String name;

    @Comment(desc = "性别: 男/女", prompt = "性别")
    private String gender;

    @Comment(desc = "年龄", prompt = "年龄")
    private Integer age;

    @Comment(desc = "身份", prompt = "身份")
    private String identity;

    @Comment(desc = "目标: 是具体、可量化的短期或中期追求，通常有明确的计划和行动步骤\t人物的目标通常是为了实现抱负所作出的计划", prompt = "目标")
    private String goal;

    @Comment(desc = "抱负: 是宏观、抽象的长远愿景，体现个人对自我实现的深层渴望\t人物的抱负的精神内核是价值观", prompt = "抱负")
    private String ambition;

    @Comment(desc = "价值观: 人物对「什么值得我坚持」这一问题的深层信念 需要填写2~4个价值观元素", prompt = "价值观")
    private List<String> values;

    @Comment(desc = "人物概括: 简短地描述人物形象, 需要体现人物的价值观、抱负、目标, 以及与故事情节相关的行为逻辑", prompt = "人物概括")
    private String profile;

}
