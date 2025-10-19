package fun.icystal.quill.controller;

import fun.icystal.quill.constant.QuillMode;
import fun.icystal.quill.fundamental.GenerateHandler;
import fun.icystal.quill.obj.entity.Book;
import fun.icystal.quill.obj.wrapper.QuillResponse;
import fun.icystal.quill.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    private final Map<String, GenerateHandler> generateHandlers;

    public BookController(BookService bookService, List<GenerateHandler> generateHandlers) {
        this.bookService = bookService;
        this.generateHandlers = new HashMap<>();
        for (GenerateHandler generateHandler : generateHandlers) {
            this.generateHandlers.put(generateHandler.step().getCode(), generateHandler);
        }
    }


    @PostMapping("/quill/{code}/{mode}")
    public QuillResponse<?> quill(@RequestBody(required = false) Book book, @PathVariable String code, @PathVariable Integer mode) {
        if (Objects.equals(mode, QuillMode.GENERATE.getMode())) {
            book = generateHandlers.get(code).handle(book);
        }

        if (book != null && book.getBookDetail() != null) {
            book = bookService.save(book);
        }
        return QuillResponse.success(book);
    }

    @GetMapping("/query/{bookId}")
    public QuillResponse<?> query(@PathVariable Long bookId) {
        Book book = bookService.findById(bookId);
        return QuillResponse.success(book);
    }

}
