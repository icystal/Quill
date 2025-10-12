package fun.icystal.quill.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookStatus {

    START(0, "初始化"),

    BRIEF(1, "一句话概要");

    private final Integer code;

    private final String desc;

}
