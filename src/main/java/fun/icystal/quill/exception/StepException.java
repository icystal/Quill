package fun.icystal.quill.exception;

import fun.icystal.quill.constant.ResponseCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StepException extends QuillException {
    public StepException(ResponseCode responseCode, String msg) {
        super(responseCode, msg);
    }
}
