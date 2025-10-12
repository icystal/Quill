package fun.icystal.quill.controller;

import com.google.common.collect.Lists;
import fun.icystal.quill.constant.BookStatus;
import fun.icystal.quill.context.ContextHolder;
import fun.icystal.quill.context.RequestContext;
import fun.icystal.quill.obj.entity.Book;
import fun.icystal.quill.obj.response.BookNode;
import fun.icystal.quill.obj.response.BookVO;
import fun.icystal.quill.obj.wrapper.QuillResponse;
import fun.icystal.quill.service.BookService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookService bookService;

    @PostMapping("/create")
    public QuillResponse<BookVO> create() {

        Book book = bookService.createBook();

        RequestContext ctx = ContextHolder.get();
        ctx.setBookId(book.getId());

        BookVO vo = new BookVO();
        vo.setBookId(book.getId());
        vo.setCurrentNode(new BookNode(BookStatus.START));
        vo.setNextNode(Lists.newArrayList(new BookNode(BookStatus.BRIEF)));

        return QuillResponse.success(vo);
    }

}
