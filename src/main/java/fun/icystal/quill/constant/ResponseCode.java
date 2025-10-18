package fun.icystal.quill.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    OK("0", "成功"),

    STEP_EXCEPTION("1", "请完成前置步骤"),

    UNKNOWN("-1", "未知异常");

    private final String code;

    private final String desc;
}
