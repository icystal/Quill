package fun.icystal.quill.obj.wrapper;

import fun.icystal.quill.constant.ResponseCode;
import lombok.Data;

@Data
public class QuillResponse<T> {

    private String code;

    private String message;

    private T body;

    public static <T> QuillResponse<T> success(T data) {
        QuillResponse<T> response = new QuillResponse<>();
        response.setBody(data);
        response.setMessage("成功");
        response.setCode(ResponseCode.OK.getCode());
        return response;
    }
}
