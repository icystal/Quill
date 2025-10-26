package fun.icystal.quill.controller;

import fun.icystal.quill.constant.QuillMode;
import fun.icystal.quill.context.ContextHolder;
import fun.icystal.quill.fundamental.GenerateHandler;
import fun.icystal.quill.fundamental.ReviseHandler;
import fun.icystal.quill.obj.entity.Book;
import fun.icystal.quill.obj.request.QuillRequest;
import fun.icystal.quill.obj.wrapper.QuillResponse;
import fun.icystal.quill.service.BookService;
import fun.icystal.quill.service.LogService;
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

    private final Map<String, ReviseHandler> reviseHandlers;

    private final LogService logService;

    public BookController(BookService bookService, List<GenerateHandler> generateHandlers, List<ReviseHandler> reviseHandlers, LogService logService) {
        this.bookService = bookService;
        this.logService = logService;

        this.generateHandlers = new HashMap<>();
        for (GenerateHandler generateHandler : generateHandlers) {
            this.generateHandlers.put(generateHandler.step().getCode(), generateHandler);
        }

        this.reviseHandlers = new HashMap<>();
        for (ReviseHandler reviseHandler : reviseHandlers) {
            this.reviseHandlers.put(reviseHandler.step().getCode(), reviseHandler);
        }
    }


    @PostMapping("/quill/{code}/{mode}")
    public QuillResponse<?> quill(@RequestBody(required = false) QuillRequest request, @PathVariable String code, @PathVariable Integer mode) {
        Book book = bookService.findById(ContextHolder.get().getBookId());
        if (Objects.equals(mode, QuillMode.GENERATE.getMode())) {
            book = generateHandlers.get(code).handle(book);
        } else if (Objects.equals(mode, QuillMode.REVISE.getMode())) {
            book = reviseHandlers.get(code).handle(request, book);
            logService.saveRevise(request);
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

    @PutMapping("/quill/decide")
    public QuillResponse<?> decide(@RequestBody Book book) {
        book = bookService.save(book);
        return QuillResponse.success(book);
    }

}
