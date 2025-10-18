package fun.icystal.quill.service.step;

import fun.icystal.quill.constant.BookStep;
import fun.icystal.quill.fundamental.GenerateHandler;
import fun.icystal.quill.obj.entity.Book;
import fun.icystal.quill.service.BookService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class StartService implements GenerateHandler {

    @Resource
    private BookService bookService;

    @Override
    public Book handle(Book book) {
        book = bookService.createBook();
        return book;
    }

    @Override
    public BookStep step() {
        return BookStep.START;
    }
}
