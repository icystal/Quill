package fun.icystal.quill.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuillMode {

    GENERATE(0, "生成"),

    REVISE(1, "修改"),

    DECIDE(2, "决定");

    private final Integer mode;

    private final String desc;

}
