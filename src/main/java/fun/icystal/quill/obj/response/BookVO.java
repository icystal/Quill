package fun.icystal.quill.obj.response;

import lombok.Data;

import java.util.List;

@Data
public class BookVO {

    private Long bookId;

    /**
     * 当前完成的节点
     */
    private BookNode currentNode;

    /**
     * 下一个节点
     */
    private List<BookNode> nextNode;

}
