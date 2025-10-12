package fun.icystal.quill.obj.response;

import fun.icystal.quill.constant.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookNode {

    private Integer status;

    private String desc;

    public BookNode(BookStatus bookStatus) {
        status = bookStatus.getCode();
        desc = bookStatus.getDesc();
    }
}
