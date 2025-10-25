package fun.icystal.quill.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookStep {

    START( "start", 0, "初始化"),

    BRIEF( "brief",1, "概要"),

    STRUCTURE( "structure",2, "结构"),

    CHARACTER( "character",3, "人物简介"),

    ABSTRACT( "abstract",4, "摘要"),

    BIOGRAPHY("biography", 5, "人物小传")
    ;

    private final String code;

    private final Integer sort;

    private final String desc;

}
