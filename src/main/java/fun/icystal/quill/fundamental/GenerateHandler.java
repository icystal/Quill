package fun.icystal.quill.fundamental;

import fun.icystal.quill.constant.BookStep;
import fun.icystal.quill.obj.entity.Book;

public interface GenerateHandler {

    Book handle(Book book);

    BookStep step();

}
