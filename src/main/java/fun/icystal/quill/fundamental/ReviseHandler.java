package fun.icystal.quill.fundamental;

import fun.icystal.quill.constant.BookStep;
import fun.icystal.quill.obj.entity.Book;
import fun.icystal.quill.obj.request.QuillRequest;

public interface ReviseHandler {
    Book handle(QuillRequest request, Book book);

    BookStep step();
}
