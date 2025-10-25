package fun.icystal.quill.service.step;

import fun.icystal.quill.constant.BookStep;
import fun.icystal.quill.fundamental.GenerateHandler;
import fun.icystal.quill.obj.entity.Book;
import org.springframework.stereotype.Service;

@Service
public class BiographyService implements GenerateHandler {
    @Override
    public Book handle(Book book) {
        return null;
    }

    @Override
    public BookStep step() {
        return BookStep.BIOGRAPHY;
    }
}
