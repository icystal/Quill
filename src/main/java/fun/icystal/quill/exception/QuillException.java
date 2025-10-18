package fun.icystal.quill.exception;

import fun.icystal.quill.constant.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class QuillException extends RuntimeException {

    private ResponseCode responseCode;

    private String msg;

}
