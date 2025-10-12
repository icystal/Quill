package fun.icystal.quill.obj.entity;

import lombok.Data;

@Data
public class Book {

    private Long id;

    private Integer status;

    private BookDetail bookDetail;

}
