package fun.icystal.quill.service;

import fun.icystal.quill.mapper.BookMapper;
import fun.icystal.quill.obj.entity.Book;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import util.JsonUtil;
import util.SnowFlake;


@Service
@Slf4j
public class BookService {

    @Resource
    private BookMapper bookMapper;

    public Book createBook() {
        Book book = new Book();
        book.setId(SnowFlake.flake());

        int insert = bookMapper.insert(book);
        log.info("create {} book: {}", insert, JsonUtil.toJSONString(book));
        return book;
    }

    public Book save(Book book) {
        assert book != null && book.getId() != null;
        bookMapper.update(book);
        return book;
    }

}
