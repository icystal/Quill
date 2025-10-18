package fun.icystal.quill.obj.wrapper;

import fun.icystal.quill.constant.ResponseCode;
import fun.icystal.quill.factory.WorkFlowFactory;
import fun.icystal.quill.obj.response.BookNode;
import lombok.Data;

import java.util.List;

@Data
public class QuillResponse<T> {

    private String code;

    private String message;

    private T body;

    /**
     * 工作流
     */
    private List<BookNode> wokeFlow;

    public static <T> QuillResponse<T> success(T data) {
        QuillResponse<T> response = new QuillResponse<>();
        response.setBody(data);
        response.setMessage("成功");
        response.setCode(ResponseCode.OK.getCode());
        response.setWokeFlow(WorkFlowFactory.WORK_FLOW);
        return response;
    }
}
