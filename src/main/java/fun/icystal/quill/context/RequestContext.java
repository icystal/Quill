package fun.icystal.quill.context;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RequestContext {

    private Long userId;

    private Long bookId;

    private Long ctxId;

    private Map<String, Object> extMap = new HashMap<>();

}
